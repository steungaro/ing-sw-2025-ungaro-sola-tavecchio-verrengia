package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The PopulateShipMenu class implements the MenuState interface and represents
 * the game menu used during the ship population phase. This menu allows players
 * to finalize ship population, add aliens to specific cabins, or navigate to other
 * game options.
 * It implements the {@link MenuState} interface to define the behavior of the Populate Ship menu.
 */
public class PopulateShipMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public PopulateShipMenu() {
    }

    /**
     * Displays the menu options available in the Populate Ship phase of the game.
     * The menu includes options for ending the population phase, adding an alien
     * to a cabin (only if the ship is not a learner level one), and viewing game options.
     * Prompts the user for their input.
     */
    @Override
    public void displayMenu(){
        System.out.println("\u001B[1mPopulate Ship Menu\u001B[22m");
        System.out.println("1. End population phase, all empty cabins will be filled with astronauts.");
        if(!ClientGameModel.getInstance().getShip(username).isLearner) {
            System.out.println("2. Add alien to a Cabin");
        }
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the menu with an error message. This method is called when there is an error
     * in user input or processing. It prints the error message in red and then displays the menu again.
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
     * Handles user input for the Populate Ship menu. This method processes the user's choice,
     * allowing them to end the population phase, add an alien to a cabin, view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
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
                            return;
                        }
                        try {
                            x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                            y = Integer.parseInt(componentName.split(" ")[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
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
                            return;
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
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    @Override
    public String getStateName(){
        return "Populate Ship Menu";
    }
}
