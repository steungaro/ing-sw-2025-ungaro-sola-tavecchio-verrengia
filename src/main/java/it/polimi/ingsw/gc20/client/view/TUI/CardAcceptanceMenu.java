package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.io.IOException;
import java.util.Scanner;

public class CardAcceptanceMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String message;

    public CardAcceptanceMenu(String message) {
        this.message = message;
    }
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("Card Acceptance Menu");
        System.out.println(message);
        System.out.println("1. Accept the card");
        System.out.println("2. Reject the card");
    }

    /**
     * Handles user input for the current menu
     *
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        int choice = scanner.nextInt();
        // Handle user input for the card acceptance menu
        switch (choice) {
            case 1:
                ClientGameModel.getInstance().getClient().acceptCard(ClientGameModel.getInstance().getUsername());
                break;
            case 2:
                ClientGameModel.getInstance().getClient().endMove(ClientGameModel.getInstance().getUsername());
                break;
            case 'q':
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName() {
        return "Abandon Ship Menu";
    }
}
