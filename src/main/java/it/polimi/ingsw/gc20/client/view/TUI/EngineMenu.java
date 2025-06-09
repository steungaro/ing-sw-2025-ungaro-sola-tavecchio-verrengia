package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the menu for controlling engine-related actions in the game.
 * This class provides functionalities for displaying the menu, handling user inputs,
 * and managing the state of engine and battery activations.
 * It implements the {@link MenuState} interface to define the behavior of the Engine menu.
 */
public class EngineMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Pair<Integer, Integer>> engines = new ArrayList<>();
    private final List<Pair<Integer, Integer>> batteries = new ArrayList<>();
    private final String message;
    private final String username = ClientGameModel.getInstance().getUsername();

    public EngineMenu(String message) {
        this.message = message;
    }

    /**
     * Displays the menu for engine-related actions.
     * The menu includes options to activate engines, not activate engines,
     * and view game options.
     * This method uses console output to print the menu options and does not return any value.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1mEngines Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate engines");
        System.out.println("2. Do not activate engines");
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
     * Handles user input for the Engine menu.
     * This method processes the user's choice, allowing them to activate engines,
     * skip activation, view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        // Handle user input from the engine menu
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                boolean inputOk = true;
                ClientGameModel.getInstance().printShip(username);
                do {
                    System.out.println("Type the coordinates of the engines you want to activate separated by spaces (for example, row1 col1 row2 col2) or [b] to go back:");
                    System.out.print(" > ");
                    engines.clear();
                    String engineInput = scanner.nextLine().trim();
                    if (engineInput.equals("b")) {
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (engineInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    String[] engineCoordinates = engineInput.split(" ");
                    if (engineCoordinates.length % 2 != 0) {
                        System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                        inputOk = false;
                    }
                    for (int i = 0; inputOk && i < engineCoordinates.length; i += 2) {
                        try {
                            Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(engineCoordinates[i]) - 5, Integer.parseInt(engineCoordinates[i + 1]) - (ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()).isLearner ? 5 : 4));
                            engines.add(coordinates);
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                do {
                    System.out.println("Type the coordinates of the batteries you want to activate separated by spaces (for example, row1 col1 row2 col2) or [b] to go back:");
                    System.out.print(" > ");
                    batteries.clear();
                    String batteryInput = scanner.nextLine().trim();
                    if (batteryInput.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (batteryInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    String[] batteryCoordinates = batteryInput.split(" ");
                    if (batteryCoordinates.length % 2 != 0) {
                        System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                        inputOk = false;
                    }
                    for (int i = 0; inputOk && i < batteryCoordinates.length; i += 2) {
                        try {
                            Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(batteryCoordinates[i]) - 5, Integer.parseInt(batteryCoordinates[i + 1]) - 4);
                            batteries.add(coordinates);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                ClientGameModel.getInstance().getClient().activateEngines(username, engines, batteries);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateEngines(username, new ArrayList<>(), new ArrayList<>());
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                System.out.print(" > ");
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
