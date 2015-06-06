package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.LogEntryDAO;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
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

    public void acquire(List<UUID> objectIds) {
        listLock.lock();
        List<Lock> locks = new ArrayList<Lock>();
        for(UUID id : objectIds) {
            createOrGetLock(id).lock();
        }
        listLock.unlock();

    }

    public void release(List<UUID> objectId) {

    }

    public void run() {
        this.mainLoop();
    }

}
