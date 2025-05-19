package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;

public class ShieldsMenu implements MenuState {
    private final Terminal terminal;
    private final LineReader lineReader;
    private final String message;

    public ShieldsMenu(Terminal terminal, String message) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        this.message = message;
    }

    public void displayMenu(){
        TUI.clearConsole(terminal);
        terminal.writer().println("Shields Menu");
        terminal.writer().println(message);
        terminal.writer().println("1. Activate a shield");
        terminal.writer().println("2. Do not activate a shield");
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
                terminal.writer().println("Type the coordinates of the shield you want to activate (x y):");
                terminal.writer().print(" > ");
                String input = lineReader.readLine().trim();
                String[] inputCoord = input.split(" ");
                int xs = Integer.parseInt(inputCoord[0]) - 5;
                int ys = Integer.parseInt(inputCoord[1]) - 4;
                terminal.writer().println("Type the coordinates of the battery you want to activate (x y):");
                terminal.writer().print(" > ");
                String batteryInput = lineReader.readLine().trim();
                String[] batteryCoord = batteryInput.split(" ");
                int xb = Integer.parseInt(batteryCoord[0]) - 5;
                int yb = Integer.parseInt(batteryCoord[1]) - 4;
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), new Pair<Integer, Integer>(xs, ys), new Pair<Integer, Integer>(xb, yb));
                break;
            case 2:
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), null, null);
                break;
            default:
                terminal.writer().println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    public String getStateName() {
        return "Shields Menu";
    }
}
