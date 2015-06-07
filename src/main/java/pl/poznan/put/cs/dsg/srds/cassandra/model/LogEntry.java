package pl.poznan.put.cs.dsg.srds.cassandra.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;
import java.util.UUID;


@Table(value = "LogEntries")
public class LogEntry {

    @PrimaryKey
    private UUID id;
    @Column
    private String logType;
    @Column
    private String authorId;
    @Column
    private Long timeCreated;
    @Column
    private List<UUID> targets;
    @Column
    private UUID parent;

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", logType=" + logType +
                ", authorId='" + authorId + '\'' +
                ", timeCreated=" + timeCreated +
                ", targets=" + targets +
                ", parent=" + parent +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
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

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        if (getId() != null ? !getId().equals(logEntry.getId()) : logEntry.getId() != null) return false;
        if (getLogType() != null ? !getLogType().equals(logEntry.getLogType()) : logEntry.getLogType() != null)
            return false;
        if (getAuthorId() != null ? !getAuthorId().equals(logEntry.getAuthorId()) : logEntry.getAuthorId() != null)
            return false;
        if (getTimeCreated() != null ? !getTimeCreated().equals(logEntry.getTimeCreated()) : logEntry.getTimeCreated() != null)
            return false;
        if (getTargets() != null ? !getTargets().equals(logEntry.getTargets()) : logEntry.getTargets() != null)
            return false;
        return !(getParent() != null ? !getParent().equals(logEntry.getParent()) : logEntry.getParent() != null);

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getLogType() != null ? getLogType().hashCode() : 0);
        result = 31 * result + (getAuthorId() != null ? getAuthorId().hashCode() : 0);
        result = 31 * result + (getTimeCreated() != null ? getTimeCreated().hashCode() : 0);
        result = 31 * result + (getTargets() != null ? getTargets().hashCode() : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        return result;
    }
}
