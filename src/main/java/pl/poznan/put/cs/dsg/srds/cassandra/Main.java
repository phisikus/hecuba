package pl.poznan.put.cs.dsg.srds.cassandra;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.poznan.put.cs.dsg.srds.cassandra.railroad.TrainManager;

public class Main {

    public static void main(String[] args) {
        try {
            new AnnotationConfigApplicationContext("pl.poznan.put.cs.dsg").getBean(TrainManager.class).init(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
