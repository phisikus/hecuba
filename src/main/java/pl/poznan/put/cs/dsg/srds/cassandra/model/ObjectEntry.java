package pl.poznan.put.cs.dsg.srds.cassandra.model;


import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table(value = "ObjectEntries")
public class ObjectEntry {

    @PrimaryKey
    private UUID objectId;
    @Column
    private String objectType;
    @Column
    private String authorId;
    @Column
    private Long lastUpdate;
    @Column
    private Long version;
    @Column
    private String content;

    @Override
    public String toString() {
        return "ObjectEntry{" +
                "objectId='" + objectId + '\'' +
                ", objectType='" + objectType + '\'' +
                ", authorId='" + authorId + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", version=" + version +
                ", content='" + content + '\'' +
                '}';
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectEntry that = (ObjectEntry) o;

        if (getObjectId() != null ? !getObjectId().equals(that.getObjectId()) : that.getObjectId() != null)
            return false;
        if (getObjectType() != null ? !getObjectType().equals(that.getObjectType()) : that.getObjectType() != null)
            return false;
        if (getAuthorId() != null ? !getAuthorId().equals(that.getAuthorId()) : that.getAuthorId() != null)
            return false;
        if (getLastUpdate() != null ? !getLastUpdate().equals(that.getLastUpdate()) : that.getLastUpdate() != null)
            return false;
        if (getVersion() != null ? !getVersion().equals(that.getVersion()) : that.getVersion() != null) return false;
        return !(getContent() != null ? !getContent().equals(that.getContent()) : that.getContent() != null);

    }

    @Override
    public int hashCode() {
        int result = getObjectId() != null ? getObjectId().hashCode() : 0;
        result = 31 * result + (getObjectType() != null ? getObjectType().hashCode() : 0);
        result = 31 * result + (getAuthorId() != null ? getAuthorId().hashCode() : 0);
        result = 31 * result + (getLastUpdate() != null ? getLastUpdate().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        return result;
    }
}
