package pl.poznan.put.cs.dsg.srds.cassandra.test;

import org.junit.Test;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.LogEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.LogEntry;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogEntryDAOTest extends GenericTest {
    @Inject
    private LogEntryDAO logEntryDAO;

    @Test
    public void insertDeleteTest() {
        long unixTime = System.currentTimeMillis() / 1000L;
        List<UUID> uuids = new ArrayList<UUID>();
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        LogEntry logEntry = new LogEntry();
        logEntry.setId(UUID.randomUUID());
        logEntry.setLogType("REQUEST");
        logEntry.setAuthorId("node1");
        logEntry.setTimeCreated(unixTime);
        logEntry.setTransaction("{}");
        logEntry.setTargets(uuids);
        logEntryDAO.create(logEntry);
        LogEntry retrievedObject = logEntryDAO.get(logEntry.getId());
        assert retrievedObject.equals(logEntry);
        logEntryDAO.delete(logEntry);
        assert logEntryDAO.get(logEntry.getId()) == null;
    }

    @Test
    public void insertUpdateDeleteTest() {
        long unixTime = System.currentTimeMillis() / 1000L;
        List<UUID> uuids = new ArrayList<UUID>();
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        LogEntry logEntry = new LogEntry();
        logEntry.setId(UUID.randomUUID());
        logEntry.setLogType("REQUEST");
        logEntry.setAuthorId("node1");
        logEntry.setTimeCreated(unixTime);
        logEntry.setTransaction("{}");
        logEntry.setTargets(uuids);
        logEntryDAO.create(logEntry);
        logEntry.setTimeCreated(0L);
        logEntryDAO.update(logEntry);
        LogEntry retrievedObject = logEntryDAO.get(logEntry.getId());
        assert retrievedObject.equals(logEntry);
        logEntryDAO.delete(logEntry);
        assert logEntryDAO.get(logEntry.getId()) == null;
    }
}
