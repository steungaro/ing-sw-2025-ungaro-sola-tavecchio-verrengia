package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class EngineMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private List<Pair<Integer, Integer>> engines;
    private List<Pair<Integer, Integer>> batteries;
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

    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        // Handle user input for the engine menu
        switch (choice) {
            case "1":
                System.out.println("Type the coordinates of the engines you want to activate (for example, x1 y1 x2 y2...):");
                System.out.print(" > ");
                String engineInput = scanner.nextLine().trim();
                String[] engineCoordinates = engineInput.split(" ");
                for (int i = 0; i < engineCoordinates.length; i += 2) {
                    Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(engineCoordinates[i]) - 5, Integer.parseInt(engineCoordinates[i + 1]) - 4);
                    engines.add(coordinates);
                }
                System.out.println("Type the coordinates of the batteries you want to activate (for example, x1 y1 x2 y2...):");
                System.out.print(" > ");
                String batteryInput = scanner.nextLine().trim();
                String[] batteryCoordinates = batteryInput.split(" ");
                for (int i = 0; i < batteryCoordinates.length; i += 2) {
                    Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(batteryCoordinates[i]) - 5, Integer.parseInt(batteryCoordinates[i + 1]) - 4);
                    batteries.add(coordinates);
                }
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
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
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
