package pl.poznan.put.cs.dsg.srds.cassandra.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.algorithm.basic.BasicObjectManager;

import javax.inject.Inject;
import javax.inject.Named;


public class Hecuba {

    @Inject
    private static ObjectManager objectManager;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/spring-context.xml");
        objectManager = context.getBean(BasicObjectManager.class);
        objectManager.getNodeId();
    }
}
