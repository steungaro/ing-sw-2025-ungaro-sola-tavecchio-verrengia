package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

/**
 * Represents the card acceptance menu state within the game. This menu is used
 * by the player to decide whether to accept or reject a card.
 * Implements the MenuState interface.
 */
public class CardAcceptanceMenu implements MenuState{
    private final String message;

    public CardAcceptanceMenu(String message) {
        this.message = message;
    }
    /**
     * Displays the card acceptance menu to the user. This menu allows the user
     * to choose whether to accept or reject a card or to view other game options.
     * The method formats the menu title and prompts the user for input.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1mCard Acceptance Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Accept the card");
        System.out.println("2. Reject the card");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the card acceptance menu with an error message.
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
     * Handles user input for the card acceptance menu.
     * This method processes the user's choice, allowing them to accept or reject a card,
     * end their move, view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        // Handle user input for the card acceptance menu
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                ClientGameModel.getInstance().getClient().acceptCard(ClientGameModel.getInstance().getUsername());
                break;
            case "2":
                ClientGameModel.getInstance().getClient().endMove(ClientGameModel.getInstance().getUsername());
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                break;
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    @Override
    public String getStateName() {
        return "Abandon Ship Menu";
    }
}
