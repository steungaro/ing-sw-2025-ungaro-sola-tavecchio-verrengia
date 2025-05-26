package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
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
    }

    @Override
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String input = scanner.nextLine().trim();
        if (input.equals("1")) {
            System.out.println("Type the coordinates of the battery you want to lose energy from (x y):");
            System.out.print(" > ");
            String batteryInput = scanner.nextLine().trim();
            Pair<Integer, Integer> batteryCoordinates = new Pair<>(Integer.parseInt(batteryInput.split(" ")[0]) - 5, Integer.parseInt(batteryInput.split(" ")[1]) - 4);
            ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
            return true;
        } else if (input.equals("v")) {
            TUI.viewOptionsMenu();
            return false;
        } else {
            System.out.println("Invalid input. Please try again.");
            return false;
        }
    }

    @Override
    public String getStateName() {
        return "LoseEnergyMenu";
    }
}
