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

    // return: if true then added, if false then rejected
    public boolean addSeat(String passenger) {
        if (this.seats.size() == this.numberOfSeats)
            return false;

        boolean result = false;

        // SEARCH FOR FIRST EMPTY SEAT
        for (int i = 0; i < this.numberOfSeats; i++) {
            if (!this.seats.containsKey(i)) {
                this.seats.put(i, passenger);
                result = true;
                break;
            }
        }

        return result;
    }

    // return: if true then added, if false then rejected
    public boolean addSeat(int seat, String passenger) {
        if (seat > this.numberOfSeats - 1 || seat < 0)
            return false;

        if (this.seats.containsKey(seat))
            return false;

        this.seats.put(seat, passenger);

        return true;
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
