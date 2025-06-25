package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

/**
 * Represents the menu state when a user is in a lobby.
 * This menu allows the user to perform actions specific to the lobby,
 * such as starting a game, leaving a lobby, killing a lobby, or shutting down the client.
 * This class implements the {@link MenuState} interface to define the behavior of the In Lobby menu.
 */
public class InLobbyMenu implements MenuState {
    private final String username = ClientGameModel.getInstance().getUsername();

    public InLobbyMenu() {
    }

    /**
     * Displays the lobby menu to the console.
     * The menu provides options based on the user's role in the lobby:
     * - If the user is the lobby owner, options are provided to start the game or kill the lobby.
     * - If the user is not the lobby owner, the option to leave the lobby is displayed.
     * The current lobby's name and relevant separators are also shown for context.
     * This method prompts the user to make a choice by printing a command-line menu.
     */
    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mLobby menu\u001B[22m");
        System.out.println("╭" + "─".repeat(ClientGameModel.getInstance().getCurrentLobby().toString().length() + 17) + "╮");
        System.out.println("│ \u001B[1mCurrent lobby: \u001B[22m" + ClientGameModel.getInstance().getCurrentLobby() + " │");
        System.out.println("╰" + "─".repeat(ClientGameModel.getInstance().getCurrentLobby().toString().length() + 17) + "╯");
        if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
            System.out.println("1. Start game");
            System.out.println("2. Kill lobby");
        } else {
            System.out.println("1. Leave lobby");
        }
        System.out.print(" > ");
    }

    /**
     * Displays the lobby menu to the console and highlights an error message, if provided.
     *
     * @param errorMessage the error message to be displayed before the menu. If null or empty, no message will be shown.
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the in lobby menu.
     * This method processes the user's choice, allowing them to start the game, leave the lobby,
     * kill the lobby (if they are the owner), or quit the application.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        // Handle user input for the in lobby menu
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().startLobby(username);
                } else {
                    ClientGameModel.getInstance().getClient().leaveLobby(username);
                }
                break;
            case "2":
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().killLobby(username);
                }
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                System.out.println("\u001B[31mInvalid input. Please try again.\u001B[0m");
                System.out.print(" > ");
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Returns the name of the current state.
     * This method provides a string representation of the current menu state,
     * which is useful for debugging or logging purposes.
     *
     * @return The name of the current state, which is "In Lobby Menu".
     */
    @Override
    public String getStateName() {
        return "In Lobby Menu";
    }
}
