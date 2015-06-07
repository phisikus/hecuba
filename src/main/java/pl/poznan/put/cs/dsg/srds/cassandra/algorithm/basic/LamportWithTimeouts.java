package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import org.springframework.beans.factory.annotation.Value;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.LogEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Named
public class LamportWithTimeouts implements CriticalSectionManager, Runnable {

    protected Lock listLock = new ReentrantLock();
    protected Map<UUID, Lock> listOfManagedObjectsIds = new HashMap<UUID, Lock>();
    protected Map<List<UUID>, LogEntry> requestLogEntries = new HashMap<List<UUID>, LogEntry>();
    protected String nodeId;

    @Inject
    protected LogEntryDAO logEntryDAO;

    @Value("${hecuba.numberOfNodes}")
    protected Integer numberOfNodes;

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void mainLoop() {
        while (true) {
            listLock.lock();
            listLock.unlock();
        }
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
    }

    private void waitForAgreements(LogEntry requestLogEntry, Integer numberOfNodes) {
        Integer collectedAgreements = 0;
        while (collectedAgreements != (numberOfNodes - 1)) {
            List<LogEntry> entries = logEntryDAO.getAllByParentId(requestLogEntry.getId());
            collectedAgreements = 0;
            for(LogEntry entry : entries) {
                if(entry.getLogType().equals("AGREE")) {
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
        logEntryDAO.delete(logEntry);
    }

    public void run() {
        this.mainLoop();
    }

}
