package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ValidationMenu implements MenuState{
    public final Scanner scanner = new Scanner(System.in);
    public String username = ClientGameModel.getInstance().getUsername();


    public ValidationMenu(){
    }

    public void displayMenu(){
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

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the current menu
     */
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        if (ClientGameModel.getInstance().getShip(username).isValid()){
            // If the ship is already valid, wait for other players
            System.out.println("\u001B[32mShip is already valid! Wait for other players before going to the next phase.\u001B[0m");
            ClientGameModel.getInstance().setFree();
            return;
        }
        // Handle user input from the validation menu
        switch (choice) {
            case "1":
                // Validate ship
                ClientGameModel.getInstance().getClient().validateShip(ClientGameModel.getInstance().getUsername());
                break;
            case "2":
                // Remove a component from the ship
                int x, y;
                do {
                    System.out.println("Type the coordinates of the component you want to remove (row col):");
                    System.out.print(" > ");
                    String componentName = scanner.nextLine().trim();
                    if (componentName.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    try {
                        x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                        y = Integer.parseInt(componentName.split(" ")[1]) - 4;
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                        x = -1;
                        y = -1;
                    }
                } while (x < 0 || x > 4 || y < 0 || y > 6);
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
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Validation Menu";
    }
}
