package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Triplet;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class CardAcceptanceMenu implements MenuState{
    private final Terminal terminal;
    private final LineReader lineReader;
    private final String message;

    public CardAcceptanceMenu(Terminal terminal, String message) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        this.message = message;
    }
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        TUI.clearConsole(terminal);
        terminal.writer().println("Card Acceptance Menu");
        terminal.writer().println(message);
        terminal.writer().println("1. Accept the card");
        terminal.writer().println("2. Reject the card");
        terminal.flush();
    }

    /**
     * Handles user input for the current menu
     *
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        // Hide cursor
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        // Show cursor
        TUI.showCursor(terminal);
        // Handle user input for the card acceptance menu
        switch (choice) {
            case 1:
                ClientGameModel.getInstance().getClient().acceptCard(ClientGameModel.getInstance().getUsername());
                break;
            case 2:
                ClientGameModel.getInstance().getClient().endMove(ClientGameModel.getInstance().getUsername());
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
     * @return State name
     */
    public String getStateName() {
        return "Abandon Ship Menu";
    }
}
