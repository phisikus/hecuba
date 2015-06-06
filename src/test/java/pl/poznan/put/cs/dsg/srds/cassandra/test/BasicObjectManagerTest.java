package pl.poznan.put.cs.dsg.srds.cassandra.test;

import org.junit.Test;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.ExampleSharedObject;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic.BasicObjectManager;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class BasicObjectManagerTest extends GenericTest {

    @Inject
    private BasicObjectManager objectManager;

    @Test
    public void createDeleteObjectTest() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExampleSharedObject object = new ExampleSharedObject();
        object.setFirstName("John");
        object.setLastName("Doe");
        object.setAge(30);
        UUID id = objectManager.create(object);
        ExampleSharedObject exampleSharedObject = (ExampleSharedObject) objectManager.get(id);
        assert object.equals(exampleSharedObject);
        objectManager.delete(id);
    }

    @Test
    public void createUpdateDeleteObjectTest() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExampleSharedObject object = new ExampleSharedObject();
        object.setFirstName("John");
        object.setLastName("Doe");
        object.setAge(30);
        UUID id = objectManager.create(object);
        object.setAge(11);
        objectManager.update(id, object);
        ExampleSharedObject exampleSharedObject = (ExampleSharedObject) objectManager.get(id);
        assert object.equals(exampleSharedObject);
        objectManager.delete(id);
    }
}
