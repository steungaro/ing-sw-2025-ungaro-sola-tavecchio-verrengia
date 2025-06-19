package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

/**
 * Represents the IdleMenu state in the game, which is a paused state
 * allowing the player to view game options or exit the game.
 * Implements the MenuState interface to define behavior for the
 * specific menu state.
 */
public class IdleMenu implements MenuState {
    private final String message;

    public IdleMenu(String message) {
        this.message = message;
    }

    /**
     * Displays the IdleMenu to the user.
     * This method prints the menu title and options available to the user.
     * It includes options to view game options or quit the game.
     */
    @Override
    public void displayMenu() {
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1m" + message + "\u001B[22m");
        if (ClientGameModel.getInstance().getBoard().assemblingState && !ClientGameModel.getInstance().getBoard().isLearner) {
            System.out.println("1. Turn hourglass");
        }
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the IdleMenu with an error message.
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
     * Handles user input for the IdleMenu.
     * This method processes the user's choice, allowing them to view options or quit the game.
     * It sets the game model to busy while processing input and then sets it back to free.
     *
     * @param choice The user's input choice
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        if (ClientGameModel.getInstance().getBoard().assemblingState) {
            if (choice.equalsIgnoreCase("1")) {
                ClientGameModel.getInstance().getClient().turnHourglass(ClientGameModel.getInstance().getUsername());
            }
        }
        // Continue in the same menu
        if (choice.equalsIgnoreCase("v")) {
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
        return "Paused Menu";
    }
}
