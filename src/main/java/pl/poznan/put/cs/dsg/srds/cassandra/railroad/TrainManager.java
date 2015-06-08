package pl.poznan.put.cs.dsg.srds.cassandra.railroad;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import pl.poznan.put.cs.dsg.srds.cassandra.hecuba.algorithm.ObjectManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class TrainManager {

    private final String setPlainText = "\033[0;0m";
    private final String setBoldText = "\033[0;1m";


    @Inject
    private ObjectManager objectManager;

    public void init(String[] args) throws IOException {
        Train train = new Train("Pociąg Batory", 100);
        train.getSeats().put("A1", "Tomasz Nowak");
        objectManager.create(train);
        train.getSeats().put("A2", "Waldemar Krzak");
        objectManager.update(train.getId(), train);

    }
}
