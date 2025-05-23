package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Scanner;

public class ValidationMenu implements MenuState{
    public final Scanner scanner = new Scanner(System.in);
    public String username = ClientGameModel.getInstance().getUsername();


    public ValidationMenu(){
    }

    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("\u001B[1mValidation Menu\u001B[22m");
        if(ClientGameModel.getInstance().getShip(username).isValid()){
            System.out.println("\u001B[32mShip is already valid! Wait for other players before going to the next phase.\u001B[0m");
        } else {
            System.out.println("Ship is not valid");
            System.out.println("1. Validate ship");
            System.out.println("2. Remove a component from the ship with the arguments");
            System.out.print(" > ");
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        if (ClientGameModel.getInstance().getShip(username).isValid()){
            // If the ship is already valid, wait for other players
            return true;
        }
        // Handle user input for the validation menu
        switch (choice) {
            case "1":
                // Validate ship
                ClientGameModel.getInstance().getClient().validateShip(ClientGameModel.getInstance().getUsername());
                break;
            case "2":
                // Remove a component from the ship
                System.out.println("Type the coordinates of the component you want to remove (x y):");
                System.out.print(" > ");
                String componentName = scanner.nextLine().trim();
                int x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                int y = Integer.parseInt(componentName.split(" ")[1]) - 4;
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                ClientGameModel.getInstance().getClient().removeComponentFromShip(ClientGameModel.getInstance().getUsername(), coordinates);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
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
        return "Validation Menu";
    }
}
