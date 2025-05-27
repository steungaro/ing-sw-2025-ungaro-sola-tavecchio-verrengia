package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LoseCrewMenu implements MenuState {
    private final Scanner scanner;
    private final int crewToLose;
    private final String username = ClientGameModel.getInstance().getUsername();

    public LoseCrewMenu(int crewToLose) {
        this.scanner = new Scanner(System.in);
        this.crewToLose = crewToLose;
    }

    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mLose Crew Menu\u001B[22m");
        System.out.println("You have to lose \u001B[31m" + crewToLose + "\u001B[0m crew members!");
        System.out.println("1. Continue");
        System.out.println("v. Viewing game options");
    }

    @Override
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String input = scanner.nextLine().trim();
        if (input.equals("1")) {
            int x;
            int y;
            boolean inputOk = true;
            List<Pair<Integer, Integer>> cabins = new java.util.ArrayList<>();
            do {
                System.out.println("Type the coordinates of the cabins you want to lose crew from separated by spaces (for example, row1 col1 row2 col2):");
                System.out.print(" > ");
                cabins.clear();
                String cabinInput = scanner.nextLine().trim();
                if (cabinInput.equals("q")) {
                    ClientGameModel.getInstance().shutdown();
                    return false;
                }
                String[] cabinCoord = cabinInput.split(" ");
                if (cabinCoord.length % 2 != 0) {
                    System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                    inputOk = false;
                }
                for (int i = 0; inputOk && i < cabinCoord.length; i += 2) {
                    try {
                        Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(cabinCoord[i]) - 5, Integer.parseInt(cabinCoord[i + 1]) - 4);
                        cabins.add(coordinates);
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                        inputOk = false;
                    }
                }
            } while (!inputOk);
            ClientGameModel.getInstance().getClient().loseCrew(ClientGameModel.getInstance().getUsername(), cabins);
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
