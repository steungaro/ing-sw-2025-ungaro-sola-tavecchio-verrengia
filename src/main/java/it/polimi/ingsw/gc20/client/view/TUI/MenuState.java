package it.polimi.ingsw.gc20.client.view.TUI;

import java.rmi.RemoteException;

/**
 * Base interface for all menu states in the game
 */
public interface MenuState {
    /**
     * Displays the current menu to the player
     */
    void displayMenu();

    /**
     * Handles user input for the current menu
     */
    void handleInput(String choice) throws RemoteException;

    /**
     * Get the name of the current state
     * @return State name
     */
    String getStateName();
}