package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.SharedObject;

import javax.inject.Named;
import java.util.UUID;

@Named
public class BasicObjectManager implements ObjectManager, Runnable {

    private String nodeId;

    public BasicObjectManager() {
        nodeId = UUID.randomUUID().toString();
        Thread lamportThread = new Thread(this, nodeId);
        lamportThread.start();
    }

    public String getNodeId() {
        return nodeId;
    }

    public UUID create(SharedObject object) {
        return null;
    }

    public SharedObject get(UUID objectId) {
        return null;
    }

    public void update(SharedObject object) {

    }

    public void delete(UUID objectId) {

    }

    public void run() {
    }
}
