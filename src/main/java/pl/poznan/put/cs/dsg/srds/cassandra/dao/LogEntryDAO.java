package pl.poznan.put.cs.dsg.srds.cassandra.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraTemplate;
import pl.poznan.put.cs.dsg.srds.cassandra.model.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LogEntryDAO extends AbstractCassandraDAO<LogEntry> {


    private static final Logger LOG = LoggerFactory.getLogger(LogEntryDAO.class);

    @Inject
    public LogEntryDAO(CassandraTemplate cassandraTemplate) {
        super(cassandraTemplate, LogEntry.class);
    }
}
