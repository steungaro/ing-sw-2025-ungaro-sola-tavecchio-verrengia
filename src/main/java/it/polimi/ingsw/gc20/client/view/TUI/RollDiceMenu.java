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
     * Displays the current menu to the player
     */
    @Override
    public void displayMenu() {
        System.out.println("\u001B[1m" + message + "\u001B[22m");
        System.out.println("1. Roll the dice");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the current menu
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
