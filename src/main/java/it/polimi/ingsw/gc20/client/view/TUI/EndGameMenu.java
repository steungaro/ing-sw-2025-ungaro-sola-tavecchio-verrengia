package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.util.Map;
import java.util.Scanner;

/**
 * Represents the End Game Menu state in the game, responsible for
 * displaying the final scoreboard and handling user interaction
 * at the conclusion of the game.
 * This class implements the {@link MenuState} interface to define
 */
public class EndGameMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Integer> scoreboard;

    public EndGameMenu(Map<String, Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }


    /**
     * Displays the end-game menu to the user.
     * <p>
     * The method retrieves its data from the {@code scoreboard} map and ensures
     * the display is user-friendly, with formatting applied for readability.
     */
    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mEnd Game Menu\u001B[22m");
        System.out.println("The game has ended. Thank you for playing!");
        System.out.println("\u001B[35mYou're all winners, but some are more winners than others!\u001B[0m");
        System.out.println("Scoreboard:");
        System.out.println("──────────────────────────────────────────────────");
        for (Map.Entry<String, Integer> entry : scoreboard.entrySet()) {
            System.out.printf("\u001B[1m%-20s\u001B[22m: %d points%n", entry.getKey(), entry.getValue());
        }
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("You will be disconnected from the game in a few seconds.");
        System.out.println("Press any key to exit now.");
        System.out.print(" > ");
    }

    /**
     * Displays the end-game menu with an error message.
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
     * Handles user input for the end-game menu.
     * This method is used to finalize the game session.
     *
     * @param choice The user's input choice, which is ignored in this implementation
     */
    @Override
    public void handleInput(String choice) {
        ClientGameModel.getInstance().shutdown(); // Shutdown the client
    }

    /**
     * Returns the name of the current state.
     * This method is used to identify the state in the game flow.
     *
     * @return A string representing the name of the end game menu state
     */
    @Override
    public String getStateName() {
        return "End Game Menu";
    }
}
