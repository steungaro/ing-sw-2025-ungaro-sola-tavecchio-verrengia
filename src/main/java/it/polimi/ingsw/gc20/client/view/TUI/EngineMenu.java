package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EngineMenu implements MenuState {
    private final Terminal terminal;
    private final LineReader lineReader;
    private List<Pair<Integer, Integer>> engines;
    private List<Pair<Integer, Integer>> batteries;
    private final String message;

    public EngineMenu(Terminal terminal, String message) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        this.message = message;
    }

    public void displayMenu(){
        TUI.clearConsole(terminal);
        terminal.writer().println("Engines Menu");
        terminal.writer().println(message);
        terminal.writer().println("1. Activate engines");
        terminal.writer().println("2. Do not activate engines");
        terminal.flush();
    }

    public boolean handleInput() throws IOException {
        // Hide cursor
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        // Show cursor
        TUI.showCursor(terminal);
        // Handle user input for the engine menu
        switch (choice) {
            case 1:
                terminal.writer().println("Type the coordinates of the engines you want to activate (for example, x1 y1 x2 y2...):");
                terminal.writer().print(" > ");
                String engineInput = lineReader.readLine().trim();
                String[] engineCoordinates = engineInput.split(" ");
                for (int i = 0; i < engineCoordinates.length; i += 2) {
                    Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(engineCoordinates[i]) - 5, Integer.parseInt(engineCoordinates[i + 1]) - 4);
                    engines.add(coordinates);
                }
                terminal.writer().println("Type the coordinates of the batteries you want to activate (for example, x1 y1 x2 y2...):");
                terminal.writer().print(" > ");
                String batteryInput = lineReader.readLine().trim();
                String[] batteryCoordinates = batteryInput.split(" ");
                for (int i = 0; i < batteryCoordinates.length; i += 2) {
                    Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(batteryCoordinates[i]) - 5, Integer.parseInt(batteryCoordinates[i + 1]) - 4);
                    batteries.add(coordinates);
                }
                ClientGameModel.getInstance().getClient().activateEngines(ClientGameModel.getInstance().getUsername(), engines, batteries);
                break;
            case 2:
                ClientGameModel.getInstance().getClient().activateEngines(ClientGameModel.getInstance().getUsername(), null, null);
                break;
            case 'q':
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                terminal.writer().println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    /**
     * Get the name of the current state
     *
     * @return State name
     */
    @Override
    public String getStateName() {
        return "Cannons Menu";
    }
}
