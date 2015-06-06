package pl.poznan.put.cs.dsg.srds.cassandra.algorithm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


public interface ObjectManager {
    String getNodeId();
    UUID create(SharedObject object) throws IOException;
    SharedObject get(UUID objectId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException;
    void update(UUID objectId, SharedObject object) throws IOException;
    void delete(UUID objectId);
}
