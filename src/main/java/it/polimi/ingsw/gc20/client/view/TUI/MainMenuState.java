package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.View;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class MainMenuState implements MenuState{
    private final Terminal terminal;
    private final LineReader reader;
    private final String username = View.getInstance().username;

    public MainMenuState(Terminal terminal) {
        this.terminal = terminal;
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    @Override
    public void displayMenu() {
        terminal.writer().println("Welcome to the game! These are the active lobbies:");
        //TODO print lobbies
        terminal.writer().println("1. Join a lobby");
        terminal.writer().println("2. Create a new lobby");
        terminal.writer().println("3. Refresh lobby list");
    }


    @Override
    public boolean handleInput() throws IOException {
        int choice = terminal.reader().read();
        // Handle user input for the main menu
        switch(choice) {
            case 1:
                terminal.writer().println("Type the name of the lobby you want to join:");
                terminal.writer().print(" > ");
                String lobbyName = reader.readLine().trim();
                View.getInstance().getClient().joinLobby(lobbyName, username);
                break;
            case 2:
                terminal.writer().println("Type the name of the lobby you want to create:");
                terminal.writer().print(" > ");
                String lobby = reader.readLine().trim();

                int numPlayers;

                do {
                    terminal.writer().println("Type the number of players that can play [2-4]:");
                    terminal.writer().print(" > ");
                    numPlayers = Integer.parseInt(reader.readLine().trim());
                } while(numPlayers < 2 || numPlayers > 4);

                int level;

                do {
                    terminal.writer().println("Type the level of the game [L/2]:");
                    terminal.writer().print(" > ");
                    level = Objects.equals(reader.readLine().trim(), "L") ? 0 : Integer.parseInt(reader.readLine().trim());
                } while(level != 0 && level != 2);

                View.getInstance().getClient().createLobby(lobby, username, numPlayers, level);
                break;

            case 3:
                //TODO?
                break;
            case 'q':
                View.getInstance().shutdown();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public String getStateName() {
        return "Main Menu";
    }
}
