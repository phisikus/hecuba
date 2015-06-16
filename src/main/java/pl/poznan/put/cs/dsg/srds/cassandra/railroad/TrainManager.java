package pl.poznan.put.cs.dsg.srds.cassandra.railroad;

import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.SharedObject;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.basic.BasicObjectManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class TrainManager {

    protected Lock lock = new ReentrantLock();
    @Inject
    private BasicObjectManager objectManager;
    private String[] firstNamesToChoose = {"Adam", "Andrzej", "Anna", "Abercjusz", "Abraham", "Achilles", "Ada", "Adelinda", "Ademar", "Adolf", "Adolfa", "Adolfina", "Adrian", "Adrianna", "Jacek", "Jacenty", "Jacław", "Jaczemir", "Jaczewoj", "Jadwiga", "Jagna", "Jagoda", "Jakert", "Jaktor", "Jakub", "Jakubina", "Jan"};
    private String[] secondNamesToChoose = {"Nowak", "Wójcik", "Kowalczyk", "Woźniak", "Kaczmarek", "Mazur", "Krawczyk", "Adamczyk", "Dudek", "Zając", "Wieczorek", "Król", "Wróbel", "Pawlak", "Walczak", "Stępień", "Michalak", "Sikora", "Baran", "Duda", "Szewczyk", "Pietrzak", "Marciniak", "Bąk", "Włodarczyk", "Kubiak", "Wilk", "Lis", "Mazurek", "Kaźmierczak", "Sobczak", "Cieślak", "Kołodziej", "Szymczak", "Szulc", "Błaszczyk", "Mróz"};
    private int randomUpBound = 2000;
    private int randomDownBound = 1000;
    private Train activeTrain;

    public void init(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        objectManager.getCriticalSectionManager().setNumberOfNodes(Integer.parseInt(args[1]));
        // GET COMMANDS
        while (true) try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("?> ");
            String fullcommand = bufferRead.readLine();
            String[] commandWords = fullcommand.split(" ");

            switch (commandWords[0]) {
                case "":
                    break;
                case "act":
                    if (commandWords.length == 2) {
                        this.activeTrain = (Train) objectManager.get(UUID.fromString(commandWords[1]));
                        System.out.println("Aktywny pociąg to \"" + this.activeTrain.getTrainName() + "\"");
                    } else
                        throw new IOException("Zła liczba argumentów.");
                    break;
                case "add":
                    switch (commandWords[1]) {
                        case "train":
                            if (!commandWords[2].matches("^[0-9]+$"))
                                throw new IOException("Ilość siedzeń musi być liczbą!");

                            String trainName = getStringFromQuotes(fullcommand);

                            if (trainName.equals(""))
                                throw new IOException("Zła nazwa pociągu.");
                            else {
                                Train newTrain = new Train(trainName, Integer.parseInt(commandWords[2]));
                                System.out.println(newTrain.toString());
                                objectManager.create(newTrain);
                            }
                            break;
                        case "seat":
                            if (this.activeTrain == null)
                                throw new IOException("Brak aktywnego pociągu.");

                            this.activeTrain = (Train) objectManager.get(this.activeTrain.getId());

                            if (!commandWords[2].matches("^-?[0-9]+$")) {
                                String passengerName = getStringFromQuotes(fullcommand);

                                if (this.activeTrain.addSeat(passengerName)) {
                                    objectManager.update(this.activeTrain.getId(), this.activeTrain);
                                    System.out.println("Dodano pasażera: " + passengerName);
                                } else {
                                    System.out.println("Nie udało się dodać miejsca dla " + passengerName);
                                }
                            } else if (commandWords.length > 3) {
                                if (!commandWords[2].matches("^[0-9]+$"))
                                    throw new IOException("Numer siedzenia musi być liczbą.");

                                String passengerName = getStringFromQuotes(fullcommand);

                                if (this.activeTrain.addSeat(Integer.parseInt(commandWords[2]), passengerName)) {
                                    objectManager.update(this.activeTrain.getId(), this.activeTrain);
                                    System.out.println("Dodano pasażera: " + passengerName + " na miejsce " + commandWords[2]);
                                } else {
                                    System.out.println("Nie udało się dodać miejsca dla " + passengerName);
                                }
                            } else
                                throw new IOException("Zła komenda. Spróbuj \"add seat <seatID> \"<passenger>\"\".");
                            break;
                        default:
                            throw new IOException("Zła komenda.");
                    }
                    break;
                case "delete":
                    if (commandWords[1].equals("trains") && commandWords.length == 2) {

                        // DELETING ALL OF THE TRAINS
                        List<UUID> trainsToDelete = new ArrayList<>();
                        List<SharedObject> trains = objectManager.getAllByType(Train.class);
                        for (SharedObject train : trains) {
                            Train temp = (Train) train;
                            trainsToDelete.add(temp.getId());
                        }
                        objectManager.delete(trainsToDelete);
                        this.activeTrain = null;
                    } else if (commandWords[1].equals("train")) {

                        // DELETING TRAIN
                        objectManager.delete(UUID.fromString(commandWords[2]));
                    } else if (commandWords[1].equals("seats")) {

                        // DELETING ALL OF THE TICKETS
                        List<SharedObject> trains = objectManager.getAllByType(Train.class);
                        for (SharedObject train : trains) {
                            Train temp = (Train) train;
                            if (temp.getSeats().size() == 0)
                                continue;
                            Map<Integer, String> emptySeats = new HashMap<>();
                            temp.setSeats(emptySeats);
                            objectManager.update(train.getId(), train);
                            this.activeTrain = null;
                        }
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
                case "randomadd":
                    Train train = (Train) objectManager.get(UUID.fromString(commandWords[1]));
                    Random gen = new Random();

                    while (true) {
                        Thread.sleep(gen.nextInt(this.randomUpBound - this.randomDownBound) + this.randomDownBound);
                        String passenger = this.firstNamesToChoose[gen.nextInt(this.firstNamesToChoose.length)] + " " +
                                this.secondNamesToChoose[gen.nextInt(this.secondNamesToChoose.length)];
                        //lock.lock();

                        int seat = train.addFreeRandomSeat(passenger);
                        if (seat > -1) {
                            objectManager.update(train.getId(), train);
                            System.out.println("Dodano pasażera " + passenger + " na miejsce " + seat);
                        } else {
                            System.out.println("Nie udało się dodać pasażera " + passenger);
                        }
                        //lock.unlock();
                    }
                case "randomdel":
                    Train trainDel = (Train) objectManager.get(UUID.fromString(commandWords[1]));
                    Random genDel = new Random();
                    while (true) {
                        Thread.sleep(genDel.nextInt(this.randomUpBound - this.randomDownBound) + this.randomDownBound);

                        int seat = trainDel.delRandomSeat();
                    }
                default:
                    System.out.println("Nieznana komenda");
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            continue;
        } catch (NullPointerException e) {
            System.out.println("Najprawdopodobniej ten obiekt nie istnieje.");
            e.printStackTrace();
            continue;
        } catch (IllegalArgumentException e) {
            System.out.println("Najprawdopodobniej zły identyfikator UUID.");
            e.printStackTrace();
            continue;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Za mało argumentów.");
            e.printStackTrace();
            continue;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printHelp() {
        System.out.println("Dostępne komendy:" +
                        "\n  act <trainID>                            - set train active" +
                        "\n  add seat <seatID> \"<passenger>\"          - add new ticket at pointed place" +
                        "\n  add seat \"<passenger>\"                   - add new ticket where nobody sits" +
                        "\n  add train <numberOfSeats> \"<trainName>\"  - add new train" +
                        "\n  delete train <trainID>                   - delete train" +
                        "\n  delete trains                            - delete all trains" +
                        "\n  get train <trainID>                      - get train" +
                        "\n  get trains                               - get all trains" +
                        "\n  randomadd <trainID>                      - time-randomized reserving seats" +
                        "\n  randomdel <trainID>                      - time-randomized deleting seats"
        );
    }

    // RETURNS THE NAME OF THE TRAIN (FROM QUOTES) OR "" IF WRONG NAME
    private String getStringFromQuotes(String fullcommand) throws IOException {
        if (!fullcommand.matches("^[^\"]+\"[^\"]+\"$"))
            throw new IOException("Nazwa musi być ujęta w cudzysłów.");

        Pattern trainNamePattern = Pattern.compile("\"[A-Za-z0-9żźćńółęąśŻŹĆĄŚĘŁÓŃ\\s]+\"$");
        Matcher matcher = trainNamePattern.matcher(fullcommand);

        if (!matcher.find())
            return "";
        else
            return matcher.group().replaceAll("\"", "");
    }
}
