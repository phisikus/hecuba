package pl.poznan.put.cs.dsg.srds.cassandra.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import javax.inject.Named;

@Named
public class SimpleCassandraOperationsProvider implements CassandraOperationsProvider {

    private Cluster cluster;
    private Session session;
    private CassandraOperations cassandraOperations;

    public SimpleCassandraOperationsProvider() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect("Hecuba");
        cassandraOperations = new CassandraTemplate(session);
    }

    public CassandraOperations getCassandraOperations() {
        return cassandraOperations;
    }
}
