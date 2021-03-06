package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.dao;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraTemplate;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.model.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;

@Named
public class LogEntryDAO extends AbstractCassandraDAO<LogEntry> {


    private static final Logger LOG = LoggerFactory.getLogger(LogEntryDAO.class);

    @Inject
    public LogEntryDAO(CassandraTemplate cassandraTemplate) {
        super(cassandraTemplate, LogEntry.class);
    }

    public List<LogEntry> getAllByParentId(UUID parentId) {
        Select query = QueryBuilder.select().from("logentries");
        query.where(QueryBuilder.eq("parent", parentId));
        return cassandraOperations.select(query, LogEntry.class);
    }

    public List<LogEntry> getAllByType(String logType) {
        Select query = QueryBuilder.select().from("logentries");
        query.where(QueryBuilder.eq("logType", logType));
        return cassandraOperations.select(query, LogEntry.class);
    }
}
