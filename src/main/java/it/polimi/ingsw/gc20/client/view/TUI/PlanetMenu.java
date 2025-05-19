package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.io.LineNumberReader;
import java.rmi.RemoteException;
import java.util.List;


public class PlanetMenu implements MenuState{
    private final Terminal terminal;
    private final LineNumberReader lineReader;
    private final String username = ClientGameModel.getInstance().getUsername();
    private final List<Planet> planets;

    public PlanetMenu(Terminal terminal, List<Planet> planets) {
        this.terminal = terminal;
        this.lineReader = new LineNumberReader(terminal.reader());
        this.planets = planets;
    }

    public void displayMenu() {
        TUI.clearConsole(terminal);
        terminal.writer().println("Planet Menu");
        terminal.writer().println("1. Land on a planet");
        terminal.writer().println("2. Don't land on a planet");
    }

    public boolean handleInput() throws IOException {
        // Hide cursor
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        // Show cursor
        TUI.showCursor(terminal);
        // Handle user input for the planet menu
        switch (choice) {
            case 1:
                terminal.writer().println("Available planets:");
                for (int i = 0; i < planets.size(); i++) {
                    terminal.writer().println(" " + (i + 1) + ". " + planets.get(i));
                }
                terminal.writer().println("Type the index of the planet you want to land on:");
                terminal.writer().print(" > ");
                String planetInput = lineReader.readLine().trim();
                int planetIndex = Integer.parseInt(planetInput) - 1;
                ClientGameModel.getInstance().getClient().landOnPlanet(username, planetIndex);
                break;
            case 2:
                ClientGameModel.getInstance().getClient().endMove(username);
                break;
            default:
                terminal.writer().println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    public String getStateName() {
        return "Planet Menu";
    }
}
