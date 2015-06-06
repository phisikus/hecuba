package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;

import java.util.UUID;

/**
 * Created by phisikus on 06.06.15.
 */
public class LamportWithTimeoutsCriticalSection implements CriticalSectionManager, Runnable {

    public void mainLoop() {

    }

    public void acquire(UUID objectId) {

    }

    public void release(UUID objectId) {

    }

    public void run() {
        this.mainLoop();
    }
}
