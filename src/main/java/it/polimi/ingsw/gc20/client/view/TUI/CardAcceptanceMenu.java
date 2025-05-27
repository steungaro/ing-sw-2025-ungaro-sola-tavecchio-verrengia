package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

public class CardAcceptanceMenu implements MenuState{
    private final String message;

    public CardAcceptanceMenu(String message) {
        this.message = message;
    }
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        System.out.println("\u001B[1mCard Acceptance Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Accept the card");
        System.out.println("2. Reject the card");
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
    public String getStateName() {
        return "Abandon Ship Menu";
    }
}
