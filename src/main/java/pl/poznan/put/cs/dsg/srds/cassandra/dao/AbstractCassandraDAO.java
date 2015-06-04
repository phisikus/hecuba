package pl.poznan.put.cs.dsg.srds.cassandra.dao;

import org.springframework.data.cassandra.core.CassandraOperations;
import pl.poznan.put.cs.dsg.srds.cassandra.db.CassandraOperationsProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;

@Named
public abstract class AbstractCassandraDAO<T> {

    protected CassandraOperations cassandraOperations;
    @Inject
    private CassandraOperationsProvider cassandraOperationsProvider;

    private Class<T> entityType;

    public AbstractCassandraDAO(Class<T> entityType) {
        this.cassandraOperations = cassandraOperationsProvider.getCassandraOperations();
        this.entityType = entityType;
    }

    public CassandraOperations getCassandraOperations() {
        return cassandraOperations;
    }

    public T create(T entity) {
        return cassandraOperations.insert(entity);
    }

    public T get(UUID id) {
        return cassandraOperations.selectOne(id.toString(), entityType);
    }

    public void update(T entity) {
        cassandraOperations.update(entity);
    }

    public void delete(T entity) {
        cassandraOperations.delete(entity);
    }

    public List<T> getAll() {
        return cassandraOperations.selectAll(entityType);
    }
}
