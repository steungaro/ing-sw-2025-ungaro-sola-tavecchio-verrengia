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
        System.out.println("Shields Menu");
        System.out.println(message);
        System.out.println("1. Activate a shield");
        System.out.println("2. Do not activate a shield");
        System.out.print(" > ");
    }

    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        // Handle user input for the engine menu
        switch (choice) {
            case "1":
                System.out.println("Type the coordinates of the shield you want to activate (x y):");
                System.out.print(" > ");
                String input = scanner.nextLine().trim();
                String[] inputCoord = input.split(" ");
                int xs = Integer.parseInt(inputCoord[0]) - 5;
                int ys = Integer.parseInt(inputCoord[1]) - 4;
                System.out.println("Type the coordinates of the battery you want to activate (x y):");
                System.out.print(" > ");
                String batteryInput = scanner.nextLine().trim();
                String[] batteryCoord = batteryInput.split(" ");
                int xb = Integer.parseInt(batteryCoord[0]) - 5;
                int yb = Integer.parseInt(batteryCoord[1]) - 4;
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), new Pair<Integer, Integer>(xs, ys), new Pair<Integer, Integer>(xb, yb));
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateShield(ClientGameModel.getInstance().getUsername(), null, null);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
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
