package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm;

import java.util.List;
import java.util.UUID;

public interface CriticalSectionManager extends Runnable {
    void mainLoop();

    void acquire(UUID objectId);

    void release(UUID objectId);

    void acquire(List<UUID> objectIds);

    void release(List<UUID> objectIds);
}
