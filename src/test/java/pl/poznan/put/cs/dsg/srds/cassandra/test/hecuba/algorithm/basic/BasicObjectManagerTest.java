package pl.poznan.put.cs.dsg.srds.cassandra.test.hecuba.algorithm.basic;

import org.junit.Test;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.basic.BasicObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.test.GenericTest;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicObjectManagerTest extends GenericTest {

    @Inject
    private BasicObjectManager objectManager;

    @Test
    public void createDeleteObjectTest() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExampleSharedObject object = createExampleSharedObject();
        UUID id = objectManager.create(object);
        ExampleSharedObject exampleSharedObject = (ExampleSharedObject) objectManager.get(id);
        assert object.equals(exampleSharedObject);
        objectManager.delete(id);
    }

    private ExampleSharedObject createExampleSharedObject() {
        ExampleSharedObject object = new ExampleSharedObject();
        object.setFirstName("John");
        object.setLastName("Doe");
        object.setAge(30);
        return object;
    }

    @Test
    public void createUpdateDeleteObjectTest() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExampleSharedObject object = createExampleSharedObject();
        objectManager.create(object);
        object.setAge(11);
        objectManager.update(object.getId(), object);
        ExampleSharedObject exampleSharedObject = (ExampleSharedObject) objectManager.get(object.getId());
        assert object.equals(exampleSharedObject);
        objectManager.delete(object.getId());
    }

    @Test
    public void createAndFindByTypeTest() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<SharedObject> objectList = new ArrayList<SharedObject>();
        List<UUID> objectIds = new ArrayList<UUID>();
        for (Integer i = 0; i < 5; i++) {
            SharedObject object = createExampleSharedObject();
            objectList.add(object);
            objectIds.add(object.getId());
        }
        objectManager.create(objectList);
        Integer listSize = objectManager.getAllByType(ExampleSharedObject.class).size();
        assert listSize.equals(5);
        objectManager.delete(objectIds);

    }
}
