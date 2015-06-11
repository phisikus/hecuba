package pl.poznan.put.cs.dsg.srds.cassandra;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.poznan.put.cs.dsg.srds.cassandra.railroad.TrainManager;

public class Main {

    // mvn compile exec:java -Dexec.mainClass="pl.poznan.put.cs.dsg.srds.cassandra.Main" -Dexec.args="127.0.0.1 2"
    public static void main(String[] args) {
        try {
            Config.address = args[0];
            new AnnotationConfigApplicationContext("pl.poznan.put.cs.dsg").getBean(TrainManager.class).init(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
