package pl.poznan.put.cs.dsg.srds.cassandra.dao;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;

@Named
public abstract class AbstractCassandraDAO<T> {

    protected CassandraOperations cassandraOperations;

    private Class<T> entityType;

    @Inject
    public AbstractCassandraDAO(CassandraTemplate cassandraTemplate, Class<T> entityType) {
        this.cassandraOperations = cassandraTemplate;
        this.entityType = entityType;
    }

    public CassandraOperations getCassandraOperations() {
        return cassandraOperations;
    }

    public T create(T entity) {
        return cassandraOperations.insert(entity);
    }

    public T get(UUID id) {
        return cassandraOperations.selectOneById(entityType, id);
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
