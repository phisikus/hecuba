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
        LogEntry logEntry = createLogEntry();
        logEntryDAO.create(logEntry);
        LogEntry retrievedObject = logEntryDAO.get(logEntry.getId());
        assert retrievedObject.equals(logEntry);
        logEntryDAO.delete(logEntry);
        assert logEntryDAO.get(logEntry.getId()) == null;
    }

    @Test
    public void insertUpdateDeleteTest() {
        LogEntry logEntry = createLogEntry();
        logEntryDAO.create(logEntry);
        logEntry.setTimeCreated(0L);
        logEntryDAO.update(logEntry);
        LogEntry retrievedObject = logEntryDAO.get(logEntry.getId());
        assert retrievedObject.equals(logEntry);
        logEntryDAO.delete(logEntry);
        assert logEntryDAO.get(logEntry.getId()) == null;
    }

    private LogEntry createLogEntry() {
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
        logEntry.setParent(UUID.randomUUID());
        logEntry.setTargets(uuids);
        return logEntry;
    }

    @Test
    public void getAllAndDeleteTest() {
        List<UUID> createdEntitiesId = new ArrayList<UUID>();
        for(Integer i=0;i<5;i++) {
            LogEntry logEntry = createLogEntry();
            createdEntitiesId.add(logEntry.getId());
            logEntryDAO.create(logEntry);
        }
        Integer dataSize = logEntryDAO.getAll().size();
        assert  dataSize.equals(5);
        for(UUID id : createdEntitiesId) {
            logEntryDAO.delete(logEntryDAO.get(id));
        }
    }

    @Test
    public void getAllByParentTest() {
        List<UUID> createdEntitiesId = new ArrayList<UUID>();
        LogEntry parent = createLogEntry();
        for(Integer i=0;i<5;i++) {
            LogEntry logEntry = createLogEntry();
            logEntry.setParent(parent.getId());
            createdEntitiesId.add(logEntry.getId());
            logEntryDAO.create(logEntry);
        }
        Integer dataSize = logEntryDAO.getAllByParentId(parent.getId()).size();
        assert dataSize.equals(5);
        for(UUID id : createdEntitiesId) {
            logEntryDAO.delete(logEntryDAO.get(id));
        }
    }
}
