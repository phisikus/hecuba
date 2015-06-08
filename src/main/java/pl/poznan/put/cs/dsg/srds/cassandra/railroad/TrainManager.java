package pl.poznan.put.cs.dsg.srds.cassandra.railroad;

import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Named
public class TrainManager {

    @Inject
    private ObjectManager objectManager;

    public void init(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        System.out.println("\nStart!\n");

        Train firstTrain = new Train("Pociąg Batory", 150);
        Map<String, String> seats = firstTrain.getSeats();
        seats.put("A1", "Adam Nowak");
        seats.put("A2", "Kasia Nowak");
        seats.put("A3", "Tadeusz Rakowiecki");
        objectManager.create(firstTrain);
        seats.put("A4", "Tosia Kowalska");
        objectManager.update(firstTrain.getId(), firstTrain);

        Train secondTrain = new Train("Pociąg Wielkopolanin", 100);
        Map<String, String> otherSeats = secondTrain.getSeats();
        otherSeats.put("B1", "Zuzanna Nowak");
        otherSeats.put("B2", "Katarzyna Zabłocka");
        otherSeats.put("B3", "Jan Zych");
        objectManager.create(secondTrain);

        otherSeats.put("B4", "N/N");
        seats.put("A5", "N/N");

        Map<UUID, SharedObject> trainsToUpdate = new HashMap<>();
        trainsToUpdate.put(firstTrain.getId(), firstTrain);
        trainsToUpdate.put(secondTrain.getId(), secondTrain);
        objectManager.update(trainsToUpdate);

        List<UUID> trainsToGet = new ArrayList<>();
        trainsToGet.add(firstTrain.getId());
        trainsToGet.add(secondTrain.getId());
        List<SharedObject> trains = objectManager.get(trainsToGet);

        for (SharedObject t : trains) {
            Train train = (Train) t;
            System.out.println("\n" + train.toString() + "\n");
        }

        objectManager.delete(trainsToGet);


    }

}
