package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.SharedObject;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.ObjectEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.ObjectEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.UUID;

@Named
public class BasicObjectManager implements ObjectManager {

    private String nodeId;

    @Inject
    private CriticalSectionManager criticalSectionManager;

    @Inject
    private ObjectEntryDAO objectEntryDAO;

    public BasicObjectManager() {
        nodeId = UUID.randomUUID().toString();
        Thread lamportThread = new Thread(criticalSectionManager, nodeId);
        lamportThread.start();
    }

    public String getNodeId() {
        return nodeId;
    }

    public UUID create(SharedObject object) throws IOException {
        UUID objectId = UUID.randomUUID();
        criticalSectionManager.acquire(objectId);
        long unixTime = System.currentTimeMillis() / 1000L;
        ObjectEntry objectEntry = new ObjectEntry();
        objectEntry.setObjectId(objectId);
        objectEntry.setAuthorId(getNodeId());
        objectEntry.setLastUpdate(unixTime);
        objectEntry.setObjectType(object.getClass().getCanonicalName());
        objectEntry.setVersion(objectEntry.getVersion() + 1);
        objectEntry.setContent(object.serializeToJSON());
        objectEntryDAO.create(objectEntry);
        criticalSectionManager.release(objectId);
        return objectId;
    }

    public SharedObject get(UUID objectId) {
        return null;
    }

    public void update(UUID objectId, SharedObject object) throws IOException {
        criticalSectionManager.acquire(objectId);
        long unixTime = System.currentTimeMillis() / 1000L;
        ObjectEntry objectEntry = new ObjectEntry();
        objectEntry.setObjectId(objectId);
        objectEntry.setAuthorId(getNodeId());
        objectEntry.setLastUpdate(unixTime);
        objectEntry.setObjectType(object.getClass().getCanonicalName());
        objectEntry.setVersion(objectEntry.getVersion() + 1);
        objectEntry.setContent(object.serializeToJSON());
        objectEntryDAO.create(objectEntry);
        criticalSectionManager.release(objectId);
    }

    public void delete(UUID objectId) {
        criticalSectionManager.acquire(objectId);
        objectEntryDAO.delete(objectEntryDAO.get(objectId));
        criticalSectionManager.release(objectId);
    }


}
