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
        System.out.println("Populate Ship Menu");
        System.out.println("1. End population phase, all other cabins will be filled with astronauts");
        if(!ClientGameModel.getInstance().getShip(username).isLearner) {
            System.out.println("2. Add alien to a Cabin");
        }
        System.out.print(" > ");
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                // End population phase
                ClientGameModel.getInstance().getClient().endMove(ClientGameModel.getInstance().getUsername());
                break;
            case "2":
                if (!ClientGameModel.getInstance().getShip(username).isLearner) {
                    // Add alien to a cabin
                    System.out.println("Type the coordinates of the cabin you want to add the alien to (x y):");
                    System.out.print(" > ");
                    String componentName = scanner.nextLine().trim();
                    int x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                    int y = Integer.parseInt(componentName.split(" ")[1]) - 4;
                    Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                    System.out.println("Type the color of the alien you want to add (BROWN or PURPLE):");
                    System.out.print(" > ");
                    String alienColor = scanner.nextLine().trim();
                    AlienColor color = AlienColor.valueOf(alienColor.toUpperCase());
                    ClientGameModel.getInstance().getClient().addAlien(username, color, coordinates);
                }
                break;
            case "q":
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
    public String getStateName(){
        return "Populate Ship Menu";
    }
}
