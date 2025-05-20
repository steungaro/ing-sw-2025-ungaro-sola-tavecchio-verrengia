package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.jline.terminal.Terminal;

import java.io.IOException;

public class InLobbyMenu implements MenuState {
    private final Terminal terminal;
    private final String username = ClientGameModel.getInstance().getUsername();

    public InLobbyMenu() {
        this.terminal = null;
    }

    public void displayMenu() {
        //TODO
            terminal.writer().println("Lobby menu");
            terminal.writer().println("Current lobby: " + ClientGameModel.getInstance().getCurrentLobby());
        if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
            terminal.writer().println("1. Start game");
            terminal.writer().println("2. Quit lobby");
        } else {
            terminal.writer().println("1. Leave lobby");
        }
    }

    public boolean handleInput() throws IOException {
        int choice = terminal.reader().read();
        // Handle user input for the in lobby menu
        switch (choice) {
            case 1:
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().startLobby(username);
                } else {
                    ClientGameModel.getInstance().getClient().leaveLobby(username);
                }
                break;
            case 2:
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().killLobby(username);
                }
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

    public String getStateName() {
        return "In Lobby Menu";
    }
}
