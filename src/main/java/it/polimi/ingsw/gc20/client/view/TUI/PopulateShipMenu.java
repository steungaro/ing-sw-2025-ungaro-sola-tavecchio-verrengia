package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Scanner;

public class PopulateShipMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public PopulateShipMenu() {
    }
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("\u001B[1mPopulate Ship Menu\u001B[22m");
        System.out.println("1. End population phase, all empty cabins will be filled with astronauts.");
        if(!ClientGameModel.getInstance().getShip(username).isLearner) {
            System.out.println("2. Add alien to a Cabin");
        }
        System.out.println("v. Viewing game options");
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                // End population phase
                ClientGameModel.getInstance().getClient().endMove(ClientGameModel.getInstance().getUsername());
                break;
            case "2":
                if (!ClientGameModel.getInstance().getShip(username).isLearner) {
                    int x;
                    int y;
                    do {
                        // Add alien to a cabin
                        System.out.println("Type the coordinates of the cabin you want to add the alien to (row col):");
                        System.out.print(" > ");
                        String componentName = scanner.nextLine().trim();
                        if (componentName.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return false;
                        }
                        try {
                            x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                            y = Integer.parseInt(componentName.split(" ")[1]) - 4;
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                            x = -1;
                            y = -1;
                        }
                    } while (x < 0 || x > 4 || y < 0 || y > 6);
                    Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                    AlienColor color;
                    do {
                        System.out.println("Type the color of the alien you want to add (BROWN or PURPLE):");
                        System.out.print(" > ");
                        String alienColor = scanner.nextLine().trim();
                        if (alienColor.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return false;
                        }
                        try {
                            color = AlienColor.valueOf(alienColor.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter a valid alien color (BROWN or PURPLE).\u001B[0m");
                            color = null;
                        }
                    } while (color == null);
                    ClientGameModel.getInstance().getClient().addAlien(username, color, coordinates);
                }
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                return false;
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
    public String getStateName(){
        return "Populate Ship Menu";
    }
}
