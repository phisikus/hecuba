package pl.poznan.put.cs.dsg.srds.cassandra.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.poznan.put.cs.dsg.srds.cassandra.dao.ObjectEntryDAO;
import pl.poznan.put.cs.dsg.srds.cassandra.model.ObjectEntry;

import javax.inject.Inject;
import javax.inject.Named;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring-context.xml")
@Named
public class DatabaseTest {

    @Inject
    private ObjectEntryDAO objectEntryDAO;

    @Test
    public void insertTest() {
        objectEntryDAO.create(new ObjectEntry());
    }
}
