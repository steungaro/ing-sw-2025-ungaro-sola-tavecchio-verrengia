package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EngineMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Pair<Integer, Integer>> engines = new ArrayList<>();
    private final List<Pair<Integer, Integer>> batteries = new ArrayList<>();
    private final String message;

    public EngineMenu(String message) {
        this.message = message;
    }

    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("\u001B[1mEngines Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate engines");
        System.out.println("2. Do not activate engines");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    public void handleInput(String choice) throws RemoteException {
        // Handle user input from the engine menu
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                boolean inputOk = true;
                do {
                    System.out.println("Type the coordinates of the engines you want to activate separated by spaces (for example, row1 col1 row2 col2):");
                    System.out.print(" > ");
                    engines.clear();
                    String engineInput = scanner.nextLine().trim();
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
                            Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(engineCoordinates[i]) - 5, Integer.parseInt(engineCoordinates[i + 1]) - 4);
                            engines.add(coordinates);
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                do {
                    System.out.println("Type the coordinates of the batteries you want to activate separated by spaces (for example, row1 col1 row2 col2):");
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
                    for (int i = 0; inputOk && i < batteryCoordinates.length; i += 2) {
                        try {
                            Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(batteryCoordinates[i]) - 5, Integer.parseInt(batteryCoordinates[i + 1]) - 4);
                            batteries.add(coordinates);
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                            inputOk = false;
                        }
                    }
                } while (!inputOk);
                ClientGameModel.getInstance().getClient().activateEngines(ClientGameModel.getInstance().getUsername(), engines, batteries);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateEngines(ClientGameModel.getInstance().getUsername(), null, null);
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
