package pl.poznan.put.cs.dsg.srds.cassandra.algorithm;

import java.util.UUID;


public interface ObjectManager {
    UUID create(SharedObject object);
    SharedObject get(UUID objectId);
    void update(SharedObject object);
    void delete(UUID objectId);
}
