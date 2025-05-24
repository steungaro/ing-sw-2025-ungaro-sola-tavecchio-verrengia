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
                System.out.println("Type the coordinates of the cannons you want to activate separated by blank spaces (for example, row1 col1 row2 col2):");
                System.out.print(" > ");
                cannons.clear();
                String cannonInput = scanner.nextLine().trim();
                String[] cannonCoordinates = cannonInput.split(" ");
                if (cannonCoordinates.length % 2 != 0) {
                    System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                    return false;
                }
                for (int i = 0; i < cannonCoordinates.length; i += 2) {
                    try {
                        int x = Integer.parseInt(cannonCoordinates[i]) - 5;
                        int y = Integer.parseInt(cannonCoordinates[i + 1]) - 4;
                        Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                        cannons.add(coordinates);
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid integer coordinates.\u001B[0m");
                        return false;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please ensure you provide pairs of coordinates.\u001B[0m");
                        return false;
                    }
                }
                System.out.println("Type the coordinates of the batteries you want to activate separated by blank spaces (for example, row1 col1 row2 col2):");
                System.out.print(" > ");
                batteries.clear();
                String batteryInput = scanner.nextLine().trim();
                String[] batteryCoordinates = batteryInput.split(" ");
                if (batteryCoordinates.length % 2 != 0) {
                    System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                    return false;
                }
                for (int i = 0; i < batteryCoordinates.length; i += 2) {
                    try {
                        int x = Integer.parseInt(batteryCoordinates[i]) - 5;
                        int y = Integer.parseInt(batteryCoordinates[i + 1]) - 4;
                        Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                        batteries.add(coordinates);
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid integer coordinates.\u001B[0m");
                        return false;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please ensure you provide pairs of coordinates.\u001B[0m");
                        return false;
                    }
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
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
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
