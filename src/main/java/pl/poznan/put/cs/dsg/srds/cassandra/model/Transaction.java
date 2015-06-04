package pl.poznan.put.cs.dsg.srds.cassandra.model;

import java.util.List;

public class Transaction {
    private List<TransactionOperation> operations;

    public Transaction(List<TransactionOperation> operations) {
        this.operations = operations;
    }

    public List<TransactionOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<TransactionOperation> operations) {
        this.operations = operations;
    }
}
