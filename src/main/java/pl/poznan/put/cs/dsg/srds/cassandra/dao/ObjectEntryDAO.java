package pl.poznan.put.cs.dsg.srds.cassandra.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.poznan.put.cs.dsg.srds.cassandra.model.ObjectEntry;

public class ObjectEntryDAO extends AbstractCassandraDAO<ObjectEntry> {


    private static final Logger LOG = LoggerFactory.getLogger(ObjectEntryDAO.class);

    public ObjectEntryDAO() {
        super(ObjectEntry.class);
    }
}
