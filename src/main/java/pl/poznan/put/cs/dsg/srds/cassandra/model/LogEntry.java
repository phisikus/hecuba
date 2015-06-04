package pl.poznan.put.cs.dsg.srds.cassandra.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;
import java.util.UUID;


@Table
public class LogEntry {

    @PrimaryKey
    private UUID id;
    private LogType logType;
    private String authorId;
    private Long timeCreated;
    private List<UUID> targets;
    private Transaction transaction;

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", logType=" + logType +
                ", authorId='" + authorId + '\'' +
                ", timeCreated=" + timeCreated +
                ", targets=" + targets +
                ", transaction=" + transaction +
                '}';
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public List<UUID> getTargets() {
        return targets;
    }

    public void setTargets(List<UUID> targets) {
        this.targets = targets;
    }


}
