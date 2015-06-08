package pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public abstract class SharedObject {
    private UUID id = UUID.randomUUID();
    private ObjectMapper mapper = new ObjectMapper();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String serializeToJSON() throws IOException {
        return mapper.writeValueAsString(this);
    }

    public SharedObject fillInFromJSON(String json) throws IOException {
        return mapper.readValue(json, this.getClass());
    }

}
