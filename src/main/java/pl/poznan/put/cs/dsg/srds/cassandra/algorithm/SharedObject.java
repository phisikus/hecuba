package pl.poznan.put.cs.dsg.srds.cassandra.algorithm;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public abstract class SharedObject {
    private ObjectMapper mapper = new ObjectMapper();

    public String serializeToJSON() throws IOException {
        return mapper.writeValueAsString(this);
    }

    public Object fillInFromJSON(String json) throws IOException {
        return mapper.readValue(json, SharedObject.class);
    }

}
