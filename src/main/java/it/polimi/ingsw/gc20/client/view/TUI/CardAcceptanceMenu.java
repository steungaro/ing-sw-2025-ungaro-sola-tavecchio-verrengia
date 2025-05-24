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
        System.out.println("\u001B[1mCard Acceptance Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Accept the card");
        System.out.println("2. Reject the card");
        System.out.println("v. Viewing game options");
    }

    /**
     * Handles user input for the current menu
     *
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String choice = scanner.nextLine().trim();
        // Handle user input for the card acceptance menu
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
                return false;
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
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
