package pl.poznan.put.cs.dsg.srds.cassandra.railroad;

import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class TrainManager {

    @Inject
    private ObjectManager objectManager;

    private String[] firstNamesToChoose = { "Adam", "Andrzej", "Anna", "Abercjusz", "Abraham", "Achilles", "Ada", "Adelinda", "Ademar", "Adolf", "Adolfa", "Adolfina", "Adrian", "Adrianna", "Jacek", "Jacenty", "Jacław", "Jaczemir", "Jaczewoj", "Jadwiga", "Jagna", "Jagoda", "Jakert", "Jaktor", "Jakub", "Jakubina", "Jan" };
    private String[] secondNamesToChoose = { "Nowak", "Wójcik", "Kowalczyk", "Woźniak", "Kaczmarek", "Mazur", "Krawczyk", "Adamczyk", "Dudek", "Zając", "Wieczorek", "Król", "Wróbel", "Pawlak", "Walczak", "Stępień", "Michalak", "Sikora", "Baran", "Duda", "Szewczyk", "Pietrzak", "Marciniak", "Bąk", "Włodarczyk", "Kubiak", "Wilk", "Lis", "Mazurek", "Kaźmierczak", "Sobczak", "Cieślak", "Kołodziej", "Szymczak", "Szulc", "Błaszczyk", "Mróz" };

    private Train activeTrain;

    public void init(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        // GET COMMANDS
        while (true) {
            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String fullcommand = bufferRead.readLine();
                String[] commandWords = fullcommand.split(" ");

                switch (commandWords[0]) {
                    case "act":
                        if (commandWords.length == 2) {
                            this.activeTrain = (Train) objectManager.get(UUID.fromString(commandWords[1]));
                            System.out.println("Set activeTrain = \"" + this.activeTrain.getTrainName() + "\"");
                        }
                        else
                            throw new IOException("Zła liczba argumentów.");
                        break;
                    case "add":
                        switch(commandWords[1]) {
                            case "train":
                                if (!commandWords[2].matches("^[0-9]+$"))
                                    throw new IOException("Ilość siedzeń musi być liczbą!");

                                String trainName = getStringFromQuotes(fullcommand);

                                if(trainName.equals(""))
                                    throw new IOException("Zła nazwa pociągu.");
                                else {
                                    Train newTrain = new Train(trainName, Integer.parseInt(commandWords[2]));
                                    System.out.println(newTrain.toString());
                                    objectManager.create(newTrain);
                                }
                                break;
                            // TODO
                            case "seat":
                                if (this.activeTrain == null)
                                    throw new IOException("Brak aktywnego pociągu.");

                                if (commandWords.length == 3) {
                                    String passengerName = getStringFromQuotes(fullcommand);

                                    System.out.println("free " + passengerName);
                                } else if (commandWords.length == 4) {
                                    if (!commandWords[2].matches("^[0-9]+$"))
                                        throw new IOException("Numer siedzenia musi być liczbą.");

                                    String passengerName = getStringFromQuotes(fullcommand);
                                    System.out.println(commandWords[2] + " " + passengerName);
                                } else
                                    throw new IOException("Zła komenda. Spróbuj \"add seat <seatID> \"<passenger>\"\".");
                                break;
                            default:
                                throw new IOException("Zła komenda.");
                        }
                        break;
                    case "delete":
                        if (commandWords[1].equals("trains") && commandWords.length == 2) {
                            List<UUID> trainsToDelete = new ArrayList<>();
                            List<SharedObject> trains = objectManager.getAllByType(Train.class);
                            for (SharedObject train : trains) {
                                Train temp = (Train) train;
                                trainsToDelete.add(temp.getId());
                            }
                            objectManager.delete(trainsToDelete);
                        } else if (commandWords[1].equals("train")) {
                            objectManager.delete(UUID.fromString(commandWords[2]));
                        } else
                            throw new IOException("Zła komenda. Spróbuj 'delete train <trainID>'.");
                        break;
                    case "get":
                        if (commandWords[1].equals("trains")) {
                            List<SharedObject> trains = objectManager.getAllByType(Train.class);

                            for (SharedObject t : trains) {
                                Train train = (Train) t;
                                System.out.println(train.toString());
                            }
                        } else if (commandWords[1].equals("train")) {
                            Train train = (Train) objectManager.get(UUID.fromString(commandWords[2]));
                            System.out.println(train.toString());
                        } else
                            throw new IOException("Zła komenda. Spróbuj 'get trains' albo 'get train <trainID>'.");
                        break;
                    case "help":
                        this.printHelp();
                        break;
                    default:
                        System.out.println("Nieznana komenda");
                        break;
                }
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            catch(NullPointerException e) {
                System.out.println("Najprawdopodobniej ten obiekt nie istnieje.");
                e.printStackTrace();
                continue;
            }
            catch(IllegalArgumentException e) {
                System.out.println("Najprawdopodobniej zły identyfikator UUID.");
                e.printStackTrace();
                continue;
            }
            catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Za mało argumentów.");
                continue;
            }
        }
/*
        Train firstTrain = new Train("Pociąg Batory", 150);
        Map<String, String> seats = firstTrain.getSeats();
        seats.put("A1", "Adam Nowak");
        seats.put("A2", "Kasia Nowak");
        seats.put("A3", "Tadeusz Rakowiecki");
        objectManager.create(firstTrain);
        seats.put("A4", "Tosia Kowalska");
        objectManager.update(firstTrain.getId(), firstTrain);

        Train secondTrain = new Train("Pociąg Wielkopolanin", 2);
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

        //List<UUID> trainsToGet = new ArrayList<>();
        //trainsToGet.add(UUID.fromString("76f478e6-a3e8-4c0b-97fa-bdf31fd534af"));
        //trainsToGet.add(UUID.fromString("c620aa8c-a480-463b-b97f-bc1d0b528b8e"));
        //List<SharedObject> trains = objectManager.get(trainsToGet);
        List<SharedObject> trains = objectManager.getAllByType(Train.class);

        for (SharedObject t : trains) {
            Train train = (Train) t;
            System.out.println("\n" + train.toString() + "\n");
        }
*/
        //objectManager.delete(trainsToGet);

    }

    private void printHelp() {
        System.out.println("Dostępne komendy:" +
                        "\n\tact <trainID>                              - set train active" +
                        "\n\tadd seat <seatID> \"<passenger>\"            - add new ticket at pointed place" +
                        "\n\tadd seat \"<passenger>\"                - add new ticket where nobody sits" +
                        "\n\tadd train <numberOfSeats> \"<trainName>\"    - add new train" +
                        "\n\tdelete train <trainID>                     - delete train" +
                        "\n\tdelete trains                              - delete all trains" +
                        "\n\tget train <trainID>                        - get train" +
                        "\n\tget trains                                 - get all trains"
        );
    }

    // RETURNS THE NAME OF THE TRAIN (FROM QUOTES) OR "" IF WRONG NAME
    private String getStringFromQuotes(String fullcommand) throws IOException {
        if (!fullcommand.matches("^[^\"]+\"[^\"]+\"$"))
            throw new IOException("Nazwa musi być w ujęta w cudzysłów.");

        Pattern trainNamePattern = Pattern.compile("\"[A-Za-z0-9żźćńółęąśŻŹĆĄŚĘŁÓŃ\\s]+\"$");
        Matcher matcher = trainNamePattern.matcher(fullcommand);

        if(!matcher.find())
            return "";
        else
            return matcher.group().replaceAll("\"", "");
    }
}
