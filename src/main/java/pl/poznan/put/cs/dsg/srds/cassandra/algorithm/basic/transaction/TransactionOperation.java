package pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic.transaction;

import java.util.UUID;

public class TransactionOperation {
    private UUID objectId;
    private Object newValue;

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
}
