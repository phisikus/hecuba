package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic;

import org.springframework.context.annotation.Scope;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.CriticalSectionManager;

import javax.inject.Named;
import java.util.UUID;

/**
 * Created by phisikus on 06.06.15.
 */
@Named
@Scope("prototype")
public class LamportWithTimeouts implements CriticalSectionManager, Runnable {

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
