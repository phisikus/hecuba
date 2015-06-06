package pl.poznan.put.cs.dsg.srds.cassandra.test;

import org.junit.Test;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.ObjectEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.ObjectEntry;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ObjectEntryDAOTest extends GenericTest {

    @Inject
    private ObjectEntryDAO objectEntryDAO;

    @Test
    public void insertDeleteTest() {
        ObjectEntry objectEntry = createObjectEntry();
        objectEntryDAO.create(objectEntry);
        ObjectEntry retrievedObject = objectEntryDAO.get(objectEntry.getObjectId());
        assert retrievedObject.equals(objectEntry);
        objectEntryDAO.delete(objectEntry);
        assert objectEntryDAO.get(objectEntry.getObjectId()) == null;
    }

    private ObjectEntry createObjectEntry() {
        long unixTime = System.currentTimeMillis() / 1000L;
        ObjectEntry objectEntry = new ObjectEntry();
        objectEntry.setObjectId(UUID.randomUUID());
        objectEntry.setObjectType(List.class.getCanonicalName());
        objectEntry.setVersion(1L);
        objectEntry.setAuthorId("node1");
        objectEntry.setContent("{}");
        objectEntry.setLastUpdate(unixTime);
        return objectEntry;
    }

    @Test
    public void insertUpdateDeleteTest() {
        ObjectEntry objectEntry = createObjectEntry();
        objectEntryDAO.create(objectEntry);
        objectEntry.setContent("{test:1}");
        objectEntryDAO.update(objectEntry);
        ObjectEntry retrievedObject = objectEntryDAO.get(objectEntry.getObjectId());
        assert retrievedObject.equals(objectEntry);
        objectEntryDAO.delete(objectEntry);
        assert objectEntryDAO.get(objectEntry.getObjectId()) == null;
    }

    @Test
    public void getAllAndDeleteTest() {
        List<UUID> createdEntitiesId = new ArrayList<UUID>();
        for(Integer i=0;i<5;i++) {
            ObjectEntry objectEntry = createObjectEntry();
            createdEntitiesId.add(objectEntry.getObjectId());
            objectEntryDAO.create(objectEntry);
        }
        objectEntryDAO.getAll();
        for(UUID id : createdEntitiesId) {
            objectEntryDAO.delete(objectEntryDAO.get(id));
        }
    }

}
