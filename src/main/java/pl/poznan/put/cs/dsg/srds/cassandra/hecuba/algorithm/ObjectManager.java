package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface ObjectManager {
    String getNodeId();

    UUID create(SharedObject object) throws IOException;

    SharedObject get(UUID objectId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    void update(UUID objectId, SharedObject object) throws IOException;

    void delete(UUID objectId);

    List<UUID> create(List<SharedObject> objects) throws IOException;

    List<SharedObject> get(List<UUID> objectIds) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    void update(Map<UUID, SharedObject> idsAndNewObjects) throws IOException;

    void delete(List<UUID> objectIds);

    List<SharedObject> getAllByType(Class type) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException;
}
