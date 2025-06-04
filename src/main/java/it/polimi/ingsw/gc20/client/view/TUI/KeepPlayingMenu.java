package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

public class KeepPlayingMenu implements MenuState{
    /**
     * Default constructor for the KeepPlayingMenu class.
     * Initializes the menu state for the player to decide whether to continue playing or give up.
     */
    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mThe next card will be drawn in a few seconds\u001B[22m");
        System.out.println("You can now choose to give up (early landing). Type [give up] to end the game, or wait here to continue playing.");
        System.out.print(" > ");
    }

    /**
     * Displays the Keep Playing menu with an error message.
     * This method prints the provided error message in red text and then displays the menu to the player.
     *
     * @param errorMessage the error message to be displayed before the menu
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles the player's input in the Keep Playing menu.
     * If the player chooses to give up, it sends a request to the game client to end the game.
     * If the input is "give up", it calls the giveUp method on the client with the player's username.
     *
     * @param choice the player's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        if (choice.equalsIgnoreCase("give up")) {
            ClientGameModel.getInstance().getClient().giveUp(ClientGameModel.getInstance().getUsername());
        }
        if (choice.equalsIgnoreCase("q")) {
            ClientGameModel.getInstance().shutdown();
        }
    }

    /**
     * Retrieves the name of the current menu state.
     *
     * @return the name of the current state as a string
     */
    @Override
    public String getStateName() {
        return "KeepPlayingMenu";
    }
}
