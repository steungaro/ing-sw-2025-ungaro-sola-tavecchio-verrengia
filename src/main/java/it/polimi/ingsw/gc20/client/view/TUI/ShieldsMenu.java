package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Scanner;

public class ShieldsMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final String message;

    public ShieldsMenu(String message) {
        this.message = message;
    }

    public void displayMenu(){
        TUI.clearConsole();
        System.out.println("\u001B[1mShields Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate a shield");
        System.out.println("2. Do not activate a shield");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        // Handle user input for the engine menu
        switch (choice) {
            case "1":
                int xs, ys;
                do {
                    System.out.println("Type the coordinates of the shield you want to activate (row col):");
                    System.out.print(" > ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return false;
                    }
                    String[] inputCoord = input.split(" ");
                    try {
                        xs = Integer.parseInt(inputCoord[0]) - 5;
                        ys = Integer.parseInt(inputCoord[1]) - 4;
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                        xs = -1;
                        ys = -1;
                    }
                } while (xs < 0 || ys < 0 || xs > 4 || ys > 6);
                int xb, yb;
                do {
                    System.out.println("Type the coordinates of the battery you want to activate (row col):");
                    System.out.print(" > ");
                    String batteryInput = scanner.nextLine().trim();
                    if (batteryInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return false;
                    }
                    String[] batteryCoord = batteryInput.split(" ");
                    try {
                        xb = Integer.parseInt(batteryCoord[0]) - 5;
                        yb = Integer.parseInt(batteryCoord[1]) - 4;
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                        xb = -1;
                        yb = -1;
                    }
                } while (xb < 0 || yb < 0 || xb > 4 || yb > 6);
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), new Pair<Integer, Integer>(xs, ys), new Pair<Integer, Integer>(xb, yb));
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), null, null);
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

    public String getStateName() {
        return "Shields Menu";
    }
}
