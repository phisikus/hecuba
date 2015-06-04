package pl.poznan.put.cs.dsg.srds.cassandra.model;


public enum LogType {
    REQUEST("REQUEST"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT"),
    RELEASE("RELEASE");

    LogType(String s) {
        this.type = s;
    }

    String type;

    @Override
    public String toString() {
        return this.type;
    }
}
