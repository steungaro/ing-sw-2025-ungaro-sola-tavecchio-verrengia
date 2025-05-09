package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Context class that manages the current menu state
 */
public class MenuContext {
    private static final Logger LOGGER = Logger.getLogger(MenuContext.class.getName());
    private MenuState currentState;
    private final Client client;
    private final String username;
    private final Scanner scanner;
    private ViewLobby lobby;
    private ViewShip ship;
    public MenuContext(Client client, String username) {
        this.client = client;
        this.username = username;
        this.scanner = new Scanner(System.in);
        // Default starting state
        this.currentState = new MainMenuState(this);
    }

    /**
     * Changes the current menu state
     * @param newState The new state to transition to
     */
    public void changeState(MenuState newState) {
        LOGGER.info("Changing state from " + (currentState != null ? currentState.getStateName() : "null") +
                " to " + newState.getStateName());
        this.currentState = newState;
    }

    /**
     * Runs the menu system
     */
    public void run() throws RemoteException {
        while (client.isConnected() && currentState != null) {
            currentState.displayMenu();
            if (!currentState.handleInput()) {
                break;
            }
        }
    }

    // Getters for dependencies
    public Client getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public Scanner getScanner() {
        return scanner;
    }

    // Helper methods for input handling that can be used by all states
    public int getIntInput(int min, int max) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);

                if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }

        return choice;
    }

    public void setState(MenuState menu) {
        this.currentState = menu;
    }

    public ViewLobby getLobby() {
        return lobby;
    }

    public void setLobby(ViewLobby lobby) {
        this.lobby = lobby;
    }

    public void createLobby(String user, int maxPlayers, int level) throws RemoteException {
        String name = UUID.randomUUID().toString();UUID.randomUUID().toString();
        client.createLobby(name, user, maxPlayers, level);
        lobby = new ViewLobby(name, user, maxPlayers, level);
    }

    public ViewShip getShip() {
        return ship;
    }

    public void setShip(ViewShip ship) {
        this.ship = ship;
    }
}