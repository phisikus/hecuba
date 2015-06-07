package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.LogEntryDAO;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LamportWithTimeouts implements CriticalSectionManager, Runnable {

    protected Lock listLock = new ReentrantLock();
    protected Map<UUID, Lock> listOfManagedObjectsIds = new HashMap<UUID, Lock>();
    protected String nodeId;
    protected LogEntryDAO logEntryDAO;

    public LamportWithTimeouts(String nodeId, LogEntryDAO logEntryDAO) {
        this.nodeId = nodeId;
        this.logEntryDAO = logEntryDAO;
    }

    public void mainLoop() {
        while (true) {
            // sprawdzaj requesty
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
        // wyślij request o sekcję krytyczną i poczekaj na zgody

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
        // wyślij release
        for (UUID objectId : objectIds) {
            Lock objectLock = null;
            if (listOfManagedObjectsIds.containsKey(objectId)) {
                objectLock = listOfManagedObjectsIds.get(objectId);
                listOfManagedObjectsIds.remove(objectId);
                objectLock.unlock();
            }
        }
        listLock.unlock();
    }

    public void run() {
        this.mainLoop();
    }

}
