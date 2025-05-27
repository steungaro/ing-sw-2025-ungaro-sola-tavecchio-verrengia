package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

public class LoseEnergyMenu implements MenuState {
    private final Scanner scanner;
    private final int energyToLose;
    private final String username = ClientGameModel.getInstance().getUsername();

    public LoseEnergyMenu(int energyToLose) {
        this.scanner = new Scanner(System.in);
        this.energyToLose = energyToLose;
    }

    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mLose Energy Menu\u001B[22m");
        System.out.println("You have to lose \u001B[31m" + energyToLose + "\u001B[0m energy because you are short on cargo!");
        System.out.println("1. Continue");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        if (choice.equals("1")) {
            int x;
            int y;
            do {
                System.out.println("Type the coordinates of the battery you want to lose energy from (row col):");
                System.out.print(" > ");
                String batteryInput = scanner.nextLine().trim();
                if (batteryInput.equals("q")) {
                    ClientGameModel.getInstance().shutdown();
                    return;
                }
                try {
                    x = Integer.parseInt(batteryInput.split(" ")[0]) - 5; // Adjusting for 0-indexed array
                    y = Integer.parseInt(batteryInput.split(" ")[1]) - 4; // Adjusting for 0-indexed array
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                    x = -1;
                    y = -1;
                }
            } while (x < 1 || x > 4 || y < 0 || y > 6);
            Pair<Integer, Integer> batteryCoordinates = new Pair<>(x, y);
            ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
        } else if (choice.equals("v")) {
            TUI.viewOptionsMenu();
        } else {
            System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
        }
        ClientGameModel.getInstance().setFree();
    }

    @Override
    public String getStateName() {
        return "LoseEnergyMenu";
    }
}
