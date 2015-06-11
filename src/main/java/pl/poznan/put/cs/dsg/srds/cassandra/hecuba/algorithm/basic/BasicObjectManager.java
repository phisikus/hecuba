package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.basic;

import org.springframework.context.annotation.Lazy;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.dao.LogEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.dao.ObjectEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.model.ObjectEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Named
@Lazy
public class BasicObjectManager implements ObjectManager {


    private String nodeId;

    private LamportLikeMutualExclusion criticalSectionManager;

    @Inject
    private ObjectEntryDAO objectEntryDAO;

    @Inject
    private LogEntryDAO logEntryDAO;

    private Thread lamportThread;

    public BasicObjectManager() {
        nodeId = UUID.randomUUID().toString();
    }

    protected void finalize() throws Throwable {
        try {
            if (lamportThread != null) {
                lamportThread.interrupt();
            }
        } finally {
            super.finalize();
        }
    }

    public LamportLikeMutualExclusion getCriticalSectionManager() {
        return criticalSectionManager;
    }

    @Inject
    private void setCriticalSectionManager(LamportLikeMutualExclusion criticalSectionManager) {
        this.criticalSectionManager = criticalSectionManager;
        criticalSectionManager.setNodeId(nodeId);
        lamportThread = new Thread(criticalSectionManager, nodeId);
        lamportThread.start();
    }

    public String getNodeId() {
        return nodeId;
    }

    public UUID create(SharedObject object) throws IOException {
        return this.create(Arrays.asList(object)).get(0);
    }

    public List<UUID> create(List<SharedObject> objects) throws IOException {
        List<UUID> ids = new ArrayList<UUID>();
        for (SharedObject object : objects) {
            ids.add(object.getId());
        }
        for (SharedObject object : objects) {
            createObjectEntry(object, object.getId());
        }
        return ids;
    }

    private void createObjectEntry(SharedObject object, UUID objectId) throws IOException {
        long unixTime = System.currentTimeMillis() / 1000L;
        ObjectEntry objectEntry = new ObjectEntry();
        objectEntry.setObjectId(objectId);
        objectEntry.setAuthorId(getNodeId());
        objectEntry.setLastUpdate(unixTime);
        objectEntry.setObjectType(object.getClass().getCanonicalName());
        objectEntry.setVersion(1L);
        objectEntry.setContent(object.serializeToJSON());
        objectEntryDAO.create(objectEntry);
    }

    public SharedObject get(UUID objectId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        return this.get(Arrays.asList(objectId)).get(0);
    }

    public List<SharedObject> get(List<UUID> objectIds) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        List<SharedObject> objects = new ArrayList<SharedObject>();
        criticalSectionManager.acquire(objectIds);
        for (UUID id : objectIds) {
            objects.add(getSharedObject(id));
        }
        criticalSectionManager.release(objectIds);
        return objects;
    }

    private SharedObject getSharedObject(UUID objectId) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ObjectEntry objectEntry = objectEntryDAO.get(objectId);
        return extractSharedObjectFromObjectEntry(objectEntry);
    }

    private SharedObject extractSharedObjectFromObjectEntry(ObjectEntry objectEntry) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Class objectClass = Class.forName(objectEntry.getObjectType());
        SharedObject object = (SharedObject) objectClass.newInstance();
        object = object.fillInFromJSON(objectEntry.getContent());
        return object;
    }

    public void update(UUID objectId, SharedObject object) throws IOException {
        Map<UUID, SharedObject> idsAndNewObjects = new HashMap<UUID, SharedObject>();
        idsAndNewObjects.put(objectId, object);
        this.update(idsAndNewObjects);
    }

    public void update(Map<UUID, SharedObject> idsAndNewObjects) throws IOException {
        List<UUID> listOfIds = new ArrayList<UUID>();
        for (UUID id : idsAndNewObjects.keySet()) {
            listOfIds.add(id);
        }
        criticalSectionManager.acquire(listOfIds);
        Iterator entries = idsAndNewObjects.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            updateObjectEntry((UUID) entry.getKey(), (SharedObject) entry.getValue());
        }
        criticalSectionManager.release(listOfIds);
    }


    private void updateObjectEntry(UUID objectId, SharedObject object) throws IOException {
        ObjectEntry oldObject = objectEntryDAO.get(objectId);
        long unixTime = System.currentTimeMillis() / 1000L;
        ObjectEntry objectEntry = new ObjectEntry();
        objectEntry.setObjectId(objectId);
        objectEntry.setAuthorId(getNodeId());
        objectEntry.setLastUpdate(unixTime);
        objectEntry.setObjectType(object.getClass().getCanonicalName());
        objectEntry.setVersion(oldObject.getVersion() + 1);
        objectEntry.setContent(object.serializeToJSON());
        objectEntryDAO.update(objectEntry);
    }

    public void delete(UUID objectId) {
        this.delete(Arrays.asList(objectId));
    }

    public void delete(List<UUID> objectIds) {
        criticalSectionManager.acquire(objectIds);
        deleteObjectEntries(objectIds);
        criticalSectionManager.release(objectIds);
    }

    public List<SharedObject> getAllByType(Class type) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        List<SharedObject> sharedObjects = new ArrayList<SharedObject>();
        for (ObjectEntry objectEntry : objectEntryDAO.getAllByType(type.getCanonicalName())) {
            sharedObjects.add(extractSharedObjectFromObjectEntry(objectEntry));
        }
        return sharedObjects;
    }

    private void deleteObjectEntries(List<UUID> objectIds) {
        for (UUID objectId : objectIds) {
            objectEntryDAO.delete(objectEntryDAO.get(objectId));
        }
    }


}
