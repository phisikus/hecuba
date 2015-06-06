package pl.poznan.put.cs.dsg.srds.cassandra.algorithm;

import java.util.UUID;

public interface CriticalSectionManager extends Runnable {
    void mainLoop();
    void acquire(UUID objectId);
    void release(UUID objectId);
}
