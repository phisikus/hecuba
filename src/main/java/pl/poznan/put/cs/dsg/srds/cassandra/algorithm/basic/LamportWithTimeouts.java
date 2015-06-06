package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.LogEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Named
public class LamportWithTimeouts implements CriticalSectionManager, Runnable {

    protected Lock listLock = new ReentrantLock();
    protected Map<UUID, Lock> listOfManagedObjectsIds = new HashMap<UUID, Lock>();

    @Inject
    protected LogEntryDAO logEntryDAO;

    public void mainLoop() {
        while (true) {
        }
    }

    public void acquire(UUID objectId) {
        listLock.lock();
        Lock objectLock;
        if (!listOfManagedObjectsIds.containsKey(objectId)) {
            objectLock = new ReentrantLock();
            listOfManagedObjectsIds.put(objectId, objectLock);
        } else {
            objectLock = listOfManagedObjectsIds.get(objectId);
        }
        listLock.unlock();
        objectLock.lock();

    }

    public void release(UUID objectId) {
        listLock.lock();
        Lock objectLock = null;
        if (listOfManagedObjectsIds.containsKey(objectId)) {
            objectLock = listOfManagedObjectsIds.get(objectId);
            objectLock.lock();
            listOfManagedObjectsIds.remove(objectId);
            listLock.unlock();
            objectLock.unlock();
        } else {
            listLock.unlock();
        }
    }

    public void run() {
        this.mainLoop();
    }

}
