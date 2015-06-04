package pl.poznan.put.cs.dsg.srds.cassandra.model;


import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.lang.reflect.Type;
import java.util.UUID;

@Table
public class ObjectEntry {

    @PrimaryKey
    private UUID objectId;
    private Type objectType;
    private String authorId;
    private Long lastUpdate;
    private Long version;
    private Object content;

    @Override
    public String toString() {
        return "ObjectEntry{" +
                "objectId=" + objectId +
                ", objectType=" + objectType +
                ", authorId='" + authorId + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", version=" + version +
                ", content=" + content +
                '}';
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public Type getObjectType() {
        return objectType;
    }

    public void setObjectType(Type objectType) {
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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
