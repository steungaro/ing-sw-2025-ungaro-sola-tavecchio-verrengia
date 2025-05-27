package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.util.Map;
import java.util.Scanner;

public class EndGameMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Integer> scoreboard;
    /**
     * Constructor for the EndGameMenu
     */
    public EndGameMenu(Map<String, Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }
    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("\u001B[1mEnd Game Menu\u001B[22m");
        System.out.println("The game has ended. Thank you for playing!");
        System.out.println("Scoreboard:");
        System.out.println("--------------------------------------------------");
        for (Map.Entry<String, Integer> entry : scoreboard.entrySet()) {
            System.out.printf("%-20s: %d points%n", entry.getKey(), entry.getValue());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("You will be disconnected from the game in a few seconds.");
        System.out.println("Press any key to exit now.");
        System.out.print(" > ");
    }

    @Override
    public void handleInput(String choice) {
        scanner.nextLine(); // Wait for user input to exit
        ClientGameModel.getInstance().shutdown(); // Shutdown the client
    }

    @Override
    public String getStateName() {
        return "End Game Menu";
    }
}
