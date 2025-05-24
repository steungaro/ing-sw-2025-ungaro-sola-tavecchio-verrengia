package it.polimi.ingsw.gc20.client.view.TUI;

import java.io.IOException;
import java.util.Scanner;

public class PausedMenu implements MenuState {
    private final Scanner scanner;

    /**
     * Constructor for the PausedMenu
     */
    public PausedMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the current menu to the player
     */
    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("\u001B[1mEveryone but you disconnected, the game is paused.\u001B[22m");
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
        } else {
            System.out.println("Invalid input. Please try again.");
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
