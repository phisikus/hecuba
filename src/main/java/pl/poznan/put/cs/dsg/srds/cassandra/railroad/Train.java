package pl.poznan.put.cs.dsg.srds.cassandra.railroad;


import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;

import java.util.HashMap;
import java.util.Map;

public class Train extends SharedObject {
    private String trainName;
    private Integer numberOfSeats;
    private Map<Integer, String> seats = new HashMap<>();

    public Train() {
    }

    public Train(String trainName, Integer numberOfSeats) {
        this.trainName = trainName;
        this.numberOfSeats = numberOfSeats;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Map<Integer, String> getSeats() {
        return seats;
    }

    public void setSeats(Map<Integer, String> seats) {
        this.seats = seats;
    }


    @Override
    public String toString() {
        String s =
                "ID              : " + getId().toString() + "\n" +
                "Train name      : " + trainName + '\n' +
                "Number of seats : " + numberOfSeats.toString() + '\n' +
                "Tickets:\n";
        for (Integer key : seats.keySet()) {
            s = s.concat(key.toString() + " -> " + seats.get(key) + '\n');
        }
        return s;
    }
}
