package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CannonsMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private List<Pair<Integer, Integer>> cannons;
    private List<Pair<Integer, Integer>> batteries;
    private final String message;

    public CannonsMenu(String message) {
        this.message = message;
    }

    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("\u001B[1mCannons Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate cannons");
        System.out.println("2. Do not activate cannons");
        System.out.println("v. Viewing game options");
    }

    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String input = scanner.nextLine().trim();
        // Handle user input from the cannon menu
        switch (input) {
            case "1":
                System.out.println("Type the coordinates of the cannons you want to activate (for example, x1 y1 x2 y2...):");
                System.out.print(" > ");
                String cannonInput = scanner.nextLine().trim();
                String[] cannonCoordinates = cannonInput.split(" ");
                for (int i = 0; i < cannonCoordinates.length; i += 2) {
                    int x = Integer.parseInt(cannonCoordinates[i]) - 5;
                    int y = Integer.parseInt(cannonCoordinates[i + 1]) - 4;
                    Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                    cannons.add(coordinates);
                }
                System.out.println("Type the coordinates of the batteries you want to activate (for example, x1 y1 x2 y2...):");
                System.out.print(" > ");
                String batteryInput = scanner.nextLine().trim();
                String[] batteryCoordinates = batteryInput.split(" ");
                for (int i = 0; i < batteryCoordinates.length; i += 2) {
                    int x = Integer.parseInt(batteryCoordinates[i]) - 5;
                    int y = Integer.parseInt(batteryCoordinates[i + 1]) - 4;
                    Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                    batteries.add(coordinates);
                }
                ClientGameModel.getInstance().getClient().activateCannons(ClientGameModel.getInstance().getUsername(), cannons, batteries);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateCannons(ClientGameModel.getInstance().getUsername(), null, null);
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
