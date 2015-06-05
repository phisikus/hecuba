package pl.poznan.put.cs.dsg.srds.cassandra.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraTemplate;
import pl.poznan.put.cs.dsg.srds.cassandra.model.ObjectEntry;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ObjectEntryDAO extends AbstractCassandraDAO<ObjectEntry> {


    private static final Logger LOG = LoggerFactory.getLogger(ObjectEntryDAO.class);

    @Inject
    public ObjectEntryDAO(CassandraTemplate cassandraTemplate) {
        super(cassandraTemplate, ObjectEntry.class);
    }
}
