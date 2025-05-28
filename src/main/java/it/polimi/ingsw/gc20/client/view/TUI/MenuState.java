package it.polimi.ingsw.gc20.client.view.TUI;

import java.rmi.RemoteException;

/**
 * Base interface for all menu states in the game
 */
public interface MenuState {

    /**
     * Displays the main menu or interface for the current state.
     * This method is responsible for rendering the menu elements
     * without including additional details or context-specific information.
     */
    void displayMenu();

    /**
     * Displays the game menu and shows an optional error message if provided.
     *
     * @param errorMessage the error message to display as part of the menu
     */
    void displayMenu(String errorMessage);

    /**
     * Handles the input provided by the user for the current menu state.
     * This method processes the input and performs the appropriate action
     * based on the given choice.
     *
     * @param choice the user's input representing the action to be performed or selection made
     * @throws RemoteException if a remote communication error occurs
     */
    void handleInput(String choice) throws RemoteException;

    /**
     * Retrieves the name of the current menu state.
     *
     * @return the name of the current state as a string
     */
    String getStateName();
}