package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.basic;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.dao.LogEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.model.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Named
public class LamportLikeMutualExclusion implements CriticalSectionManager, Runnable {

    protected Lock listLock = new ReentrantLock();
    protected Map<UUID, Lock> listOfManagedObjectsIds = new HashMap<UUID, Lock>();
    protected Map<List<UUID>, LogEntry> requestLogEntries = new HashMap<List<UUID>, LogEntry>();
    protected List<UUID> listOfAcquiredObjects = new ArrayList<UUID>();
    protected List<LogEntry> logEntriesSent = new ArrayList<>();
    protected String nodeId;

    @Inject
    protected LogEntryDAO logEntryDAO;

    @Value("${hecuba.numberOfNodes}")
    protected Integer numberOfNodes;

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void mainLoop() {
        while (!Thread.interrupted()) {
            List<LogEntry> logEntries = logEntryDAO.getAllByType("REQUEST");
            List<LogEntry> logEntriesNotMine = removeMyEntries(logEntries);
            listLock.lock();
            List<LogEntry> remainingEntries = getRequestsNotConflictingWithAcquiredObjects(logEntriesNotMine);
            agreeToRequestsThatAreLegitimate(remainingEntries);
            listLock.unlock();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<LogEntry> removeMyEntries(List<LogEntry> logEntries) {
        List<LogEntry> filteredList = new ArrayList<>();
        for (LogEntry logEntry : logEntries) {
            if (!logEntry.getAuthorId().equals(nodeId)) {
                filteredList.add(logEntry);
            }
        }
        return filteredList;
    }

    private List<LogEntry> agreeToRequestsThatAreLegitimate(List<LogEntry> remainingEntries) {
        List<LogEntry> listOfNonConflicting = new ArrayList<>();
        Set<UUID> managedIds = listOfManagedObjectsIds.keySet();

        for (LogEntry logEntry : remainingEntries) {
            Set<UUID> targetsIds = Sets.newHashSet(logEntry.getTargets());
            if (Sets.intersection(managedIds, targetsIds).size() == 0) {
                createAgreeLogEntry(logEntry.getId());
            } else {
                Boolean agree = true;
                for (LogEntry request : requestLogEntries.values()) {
                    if (Sets.intersection(Sets.newHashSet(request.getTargets()), targetsIds).size() > 0) {
                        Boolean myRequestWasEarlier = request.getTimeCreated() < logEntry.getTimeCreated();
                        Boolean requestsAtTheSameTimeButMyIdIsSmaller = request.getTimeCreated().equals(logEntry.getTimeCreated()) && request.getAuthorId().compareTo(logEntry.getAuthorId()) < 0;
                        if (myRequestWasEarlier || requestsAtTheSameTimeButMyIdIsSmaller) {
                            agree = false;
                        }
                    }
                }
                if (agree) {
                    createAgreeLogEntry(logEntry.getId());
                }
            }

        }
        return listOfNonConflicting;
    }

    private void createAgreeLogEntry(UUID id) {
        long unixTime = System.currentTimeMillis() / 1000L;
        LogEntry logEntry = new LogEntry();
        logEntry.setAuthorId(nodeId);
        logEntry.setId(UUID.randomUUID());
        logEntry.setLogType("AGREE");
        logEntry.setParent(id);
        logEntry.setTimeCreated(unixTime);
        logEntry.setTargets(null);
        if (!weEnteredThatAlready(logEntry)) {
            logEntryDAO.create(logEntry);
        }
    }

    private boolean weEnteredThatAlready(LogEntry logEntry) {
        for(LogEntry existingEntry : logEntriesSent) {
            if(
                    logEntry.getAuthorId().equals(existingEntry.getAuthorId())&&
                    logEntry.getLogType().equals(existingEntry.getLogType())&&
                    logEntry.getParent().equals(existingEntry.getParent())&&
                    logEntry.getTargets().equals(existingEntry.getTargets())) {
                return true;
            }
        }
        return false;
    }

    private List<LogEntry> getRequestsNotConflictingWithAcquiredObjects(List<LogEntry> logEntries) {
        List<LogEntry> listOfNonConflicting = new ArrayList<>();
        for (LogEntry logEntry : logEntries) {
            if (Sets.intersection(Sets.newHashSet(logEntry.getTargets()), Sets.newHashSet(listOfAcquiredObjects)).size() == 0) {
                listOfNonConflicting.add(logEntry);
            }
        }
        return listOfNonConflicting;
    }


    public void acquire(UUID objectId) {
        this.acquire(Arrays.asList(objectId));
    }

    public void acquire(List<UUID> objectIds) {
        listLock.lock();
        List<Lock> locks = new ArrayList<Lock>();
        for (UUID id : objectIds) {
            locks.add(createOrGetLock(id));
        }
        listLock.unlock();
        for (Lock lock : locks) {
            lock.lock();
        }
        LogEntry requestLogEntry = createLogEntryRequest(objectIds);
        requestLogEntries.put(objectIds, requestLogEntry);
        waitForAgreements(requestLogEntry, numberOfNodes);
        listLock.lock();
        changeRequestStatusToAquired(requestLogEntry);
        listLock.unlock();

    }

    private void changeRequestStatusToAquired(LogEntry requestLogEntry) {
        requestLogEntry.setLogType("ACQUIRED");
        logEntryDAO.update(requestLogEntry);
        listOfAcquiredObjects.addAll(requestLogEntry.getTargets());
    }

    private void waitForAgreements(LogEntry requestLogEntry, Integer numberOfNodes) {
        Integer collectedAgreements = 0;
        while (collectedAgreements != (numberOfNodes - 1)) {
            List<LogEntry> entries = logEntryDAO.getAllByParentId(requestLogEntry.getId());
            collectedAgreements = 0;
            for (LogEntry entry : entries) {
                if (entry.getLogType().equals("AGREE")) {
                    collectedAgreements++;
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private LogEntry createLogEntryRequest(List<UUID> objectIds) {
        long unixTime = System.currentTimeMillis() / 1000L;
        LogEntry requestLogEntry = new LogEntry();
        requestLogEntry.setId(UUID.randomUUID());
        requestLogEntry.setAuthorId(nodeId);
        requestLogEntry.setLogType("REQUEST");
        requestLogEntry.setParent(null);
        requestLogEntry.setTargets(objectIds);
        requestLogEntry.setTimeCreated(unixTime);
        logEntryDAO.create(requestLogEntry);
        return requestLogEntry;
    }

    private Lock createOrGetLock(UUID objectId) {
        Lock objectLock;
        if (!listOfManagedObjectsIds.containsKey(objectId)) {
            objectLock = new ReentrantLock();
            listOfManagedObjectsIds.put(objectId, objectLock);
        } else {
            objectLock = listOfManagedObjectsIds.get(objectId);
        }
        return objectLock;
    }

    public void release(UUID objectId) {
        this.release(Arrays.asList(objectId));
    }


    public void release(List<UUID> objectIds) {
        listLock.lock();
        changeAquiredStatusToReleased(objectIds);
        for (UUID objectId : objectIds) {
            Lock objectLock;
            if (listOfManagedObjectsIds.containsKey(objectId)) {
                objectLock = listOfManagedObjectsIds.get(objectId);
                listOfManagedObjectsIds.remove(objectId);
                objectLock.unlock();
            }
        }
        listLock.unlock();
    }

    private void changeAquiredStatusToReleased(List<UUID> objectIds) {
        LogEntry logEntry = requestLogEntries.get(objectIds);
        requestLogEntries.remove(objectIds);
        listOfAcquiredObjects.removeAll(objectIds);
        logEntryDAO.delete(logEntry);
        logEntryDAO.getAllByParentId(logEntry.getId()).forEach(entry -> logEntryDAO.delete(entry));
    }

    public void run() {
        this.mainLoop();
    }

}
