package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

public class RollDiceMenu implements MenuState {
    private final String message;

    /**
     * Constructor for the RollDiceMenu
     */
    public RollDiceMenu(String message) {
        this.message = message;
    }

    /**
     * Displays the menu for rolling dice.
     * The menu includes options to roll the dice and view game options.
     * This method uses console output to print the menu options and does not return any value.
     * It includes a prompt for user input.
     */
    @Override
    public void displayMenu() {
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1m" + message + "\u001B[22m");
        System.out.println("1. Roll the dice");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the menu with an error message.
     * This method is called when there is an error in user input or processing.
     * It prints the error message in red and then displays the menu again.
     *
     * @param errorMessage The error message to display
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the Roll Dice menu.
     * This method processes the user's choice, allowing them to roll the dice,
     * view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        // Continue in the same menu
        if (choice.equals("1")) {
            // Roll the dice
            ClientGameModel.getInstance().getClient().rollDice(ClientGameModel.getInstance().getUsername());
        } else if (choice.equalsIgnoreCase("v")) {
            // View options menu
            TUI.viewOptionsMenu();
        } else if (choice.equalsIgnoreCase("q")) {
            // If the user wants to quit, shutdown the client
            ClientGameModel.getInstance().shutdown();
        } else {
            System.out.println("\u001B[31mInvalid input. Please try again.\u001B[0m");
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     *
     * @return State name
     */
    @Override
    public String getStateName() {
        return "Roll Dice Menu";
    }
}
