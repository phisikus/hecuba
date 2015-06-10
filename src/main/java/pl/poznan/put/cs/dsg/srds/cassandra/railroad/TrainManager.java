package pl.poznan.put.cs.dsg.srds.cassandra.railroad;

import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.ObjectManager;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Console;
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

    public void init(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        // GET COMMANDS
        while (true) {
            try {
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String fullcommand = bufferRead.readLine();
                String[] commandWords = fullcommand.split(" ");

                switch (commandWords[0]) {
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
                    case "add":
                        if (!commandWords[1].equals("train"))
                            throw new IOException("Zła komenda. Spróbuj \"add train <numberOfSeats> '<trainName>'\".");

                        if (!commandWords[2].matches("^[0-9]+$"))
                            throw new IOException("Ilość siedzeń musi być liczbą!");

                        Pattern trainNamePattern = Pattern.compile("\"[\\w\\s]+\"$");
                        Matcher matcher = trainNamePattern.matcher(fullcommand);

                        if(!matcher.find())
                            throw new IOException("Zła nazwa pociągu.");
                        else {
                            Train newTrain = new Train(matcher.group(), Integer.parseInt(matcher.group()));
                            objectManager.create(newTrain);
                        }

                        break;
                    case "delete":
                        if (!commandWords[1].equals("train"))
                            throw new IOException("Zła komenda. Spróbuj 'delete train <trainID>'.");

                        objectManager.delete(UUID.fromString(commandWords[2]));

                        break;
                    case "help":
                        System.out.println("Dostępne komendy:" +
                                        "\n\tget train <trainID>" +
                                        "\n\tget trains" +
                                        "\n\tadd train <numberOfSeats> \"<trainName>\"" +
                                        "\n\tdelete train <trainID>"
                        );
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
                System.out.println("Najprawdopodobniej zły format identyfikatora UUID.");
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

}
