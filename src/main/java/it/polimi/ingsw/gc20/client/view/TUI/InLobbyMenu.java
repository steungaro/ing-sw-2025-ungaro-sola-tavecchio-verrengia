package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;

import java.awt.*;
import java.rmi.RemoteException;

public class InLobbyMenu implements MenuState {
    private final MenuContext menuContext;
    private final String stateName = "In Lobby Menu";

    public InLobbyMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        if(menuContext.getLobby().getOwner().equals(menuContext.getUsername())){
            System.out.println("1. Start game");
            System.out.println("2 Quit lobby");
        } else {
            System.out.println("1. Leave lobby");
        }
    }

    public boolean handleInput() throws RemoteException {
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the in lobby menu
        switch (choice) {
            case 1:
                if(menuContext.getLobby().getOwner().equals(menuContext.getUsername())){
                    menuContext.getClient().startLobby(menuContext.getLobby().getID());
                    ViewShip ship = new ViewShip();
                    menuContext.setState(new BuildingMenu(menuContext, ship));
                    menuContext.setShip(ship);
                } else {
                    menuContext.getClient().leaveLobby(menuContext.getUsername());
                }
                break;
            case 2:
                if(menuContext.getLobby().getOwner().equals(menuContext.getUsername())){
                    menuContext.getClient().killLobby(menuContext.getUsername());
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
