package pl.poznan.put.cs.dsg.srds.cassandra.db;

import org.springframework.data.cassandra.core.CassandraOperations;

public interface CassandraOperationsProvider {

    CassandraOperations getCassandraOperations();
}
