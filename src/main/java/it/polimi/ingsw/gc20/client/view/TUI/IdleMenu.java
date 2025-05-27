package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.io.IOException;
import java.util.Scanner;

public class IdleMenu implements MenuState {
    private final Scanner scanner;
    private final String message;

    /**
     * Constructor for the IdleMenu
     */
    public IdleMenu(String message) {
        this.scanner = new Scanner(System.in);
        this.message = message;
    }

    /**
     * Displays the current menu to the player
     */
    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("\u001B[1m" + message + "\u001B[22m");
        System.out.println("v. Viewing game options");
    }

    /**
     * Handles user input for the current menu
     *
     * @return true if the menu should continue, false if it should exit
     */
    @Override
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String input = scanner.nextLine().trim();
        // Continue in the same menu
        if (input.equalsIgnoreCase("v")) {
            TUI.viewOptionsMenu();
        } else if (input.equalsIgnoreCase("q")) {
            // If the user wants to quit, shutdown the client
            ClientGameModel.getInstance().shutdown();
            return false; // Exit the menu
        } else {
            System.out.println("\u001B[31mInvalid input. Please try again.\u001B[0m");
        }
        return true; // Continue in the same menu
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
