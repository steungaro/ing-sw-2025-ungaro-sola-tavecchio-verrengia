package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;

public class InLobbyMenu implements MenuState {
    private final Terminal terminal;
    private final LineReader reader;
    private final String username = View.getInstance().username;
    private final String stateName = "In Lobby Menu";

    public InLobbyMenu(Terminal terminal) {
        this.terminal = terminal;
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    public void displayMenu() {
        //TODO
            System.out.println("Lobby menu");
            System.out.println("Current lobby: " + View.getInstance().getLobby());
        if(View.getInstance().getLobby().getOwner().equals(username)){
            System.out.println("1. Start game");
            System.out.println("2. Quit lobby");
        } else {
            System.out.println("1. Leave lobby");
        }
    }

    public boolean handleInput() throws IOException {
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        TUI.showCursor(terminal);
        // Handle user input for the in lobby menu
        switch (choice) {
            case 1:
                //TODO
                if(menuContext.getLobby().getOwner().equals(menuContext.getUsername())){
                    menuContext.getClient().startLobby(menuContext.getLobby().getID());
                    ViewShip ship = new ViewShip();
                    menuContext.setState(new BuildingMenu(menuContext, ship));
                    menuContext.setShip(ship);
                } else {
                    View.getInstance().getClient().leaveLobby(username);
                }
                break;
            case 2:
                //TODO
                if(menuContext.getLobby().getOwner().equals(menuContext.getUsername())){
                    View.getInstance().getClient().killLobby(username);
                    //TODO
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
