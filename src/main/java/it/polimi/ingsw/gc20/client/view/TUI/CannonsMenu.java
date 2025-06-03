package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the Cannons' Menu state in the game, which allows the user to interact with cannon-related options.
 * The menu manages user input for activating or not activating cannons and batteries.
 * This class implements the {@link MenuState} interface to define the behavior of the Cannons menu.
 */
public class CannonsMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Pair<Integer, Integer>> cannons = new ArrayList<>();
    private final List<Pair<Integer, Integer>> batteries = new ArrayList<>();
    private final String message;
    private final String username = ClientGameModel.getInstance().getUsername();


    public CannonsMenu(String message) {
        this.message = message;
    }

    /**
     * Displays the menu for the Cannons feature.
     * The menu includes options to activate or not activate cannons
     * and an option to view game settings.
     * <p>
     * This method uses console output to print the menu options and does not
     * return any value. It includes a prompt for user input.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1mCannons Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate cannons");
        System.out.println("2. Do not activate cannons");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the menu with an error message.
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
     * Handles user input for the Cannons menu.
     * This method processes the user's choice, allowing them to activate cannons and batteries,
     * or skip activation. It also handles input validation and provides feedback for invalid inputs.
     *
     * @param choice The user's choice as a string
     * @throws RemoteException If there is an issue with remote method invocation
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                boolean inputOk = true;
                ClientGameModel.getInstance().printShip(username);
                do {
                    System.out.println("Type the coordinates of the cannons you want to activate separated by blank spaces (for example, row1 col1 row2 col2):");
                    System.out.print(" > ");
                    cannons.clear();
                    String cannonInput = scanner.nextLine().trim();
                    if (cannonInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    String[] cannonCoordinates = cannonInput.split(" ");
                    if (cannonCoordinates.length % 2 != 0) {
                        System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                        inputOk = false;
                    }
                    for (int i = 0; i < cannonCoordinates.length; i += 2) {
                        try {
                            int x = Integer.parseInt(cannonCoordinates[i]) - 5;
                            int y = Integer.parseInt(cannonCoordinates[i + 1]) - 4;
                            Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                            cannons.add(coordinates);
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integer coordinates.\u001B[0m");
                            inputOk = false;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("\u001B[31mInvalid input. Please ensure you provide pairs of coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                do {
                    System.out.println("Type the coordinates of the batteries you want to activate separated by blank spaces (for example, row1 col1 row2 col2):");
                    System.out.print(" > ");
                    batteries.clear();
                    String batteryInput = scanner.nextLine().trim();
                    if (batteryInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    String[] batteryCoordinates = batteryInput.split(" ");
                    if (batteryCoordinates.length % 2 != 0) {
                        System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                        inputOk = false;
                    }
                    for (int i = 0; i < batteryCoordinates.length; i += 2) {
                        try {
                            int x = Integer.parseInt(batteryCoordinates[i]) - 5;
                            int y = Integer.parseInt(batteryCoordinates[i + 1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
                            Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                            batteries.add(coordinates);
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integer coordinates.\u001B[0m");
                            inputOk = false;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("\u001B[31mInvalid input. Please ensure you provide pairs of coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                ClientGameModel.getInstance().getClient().activateCannons(username, cannons, batteries);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateCannons(username, null, null);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                break;
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     *
     * @return State name
     */
    @Override
    public String getStateName() {
        return "Cannons Menu";
    }
}
