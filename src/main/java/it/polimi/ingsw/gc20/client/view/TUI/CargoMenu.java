package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class CargoMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();
    private final String message;
    private final int cargoToLose;
    private final boolean losing;
    private final Map<CargoColor, Integer> cargoToGain;

    public CargoMenu(String message, int cargoToLose, Map<CargoColor, Integer> cargoToGain, boolean losing) {
        this.message = message;
        this.cargoToLose = cargoToLose;
        this.cargoToGain = cargoToGain;
        this.losing = losing;
    }

    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("\u001B[1mCargo Menu\u001B[0m");
        System.out.println(message);
        if (losing) {
            System.out.println("You have to lose \u001B[31m" + cargoToLose + "\u001B[0m cargo.");
            System.out.println("1. Lose cargo");
            System.out.println("2. Move cargo");
            System.out.println("3. End turn");
        } else {
            System.out.println("You have to gain " +
                    cargoToGain.entrySet().stream().map(e -> e.getKey().toString() + " " + e.getValue().toString()) +
                    " cargo.");
            System.out.println("1. Unload cargo");
            System.out.println("2. Move cargo");
            System.out.println("3. Load cargo");
            System.out.println("4. End turn");
        }
        System.out.println("v. Viewing game options");
    }


    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String choice = scanner.nextLine().trim();
        // Handle user input from the cargo menu
        switch (choice) {
            case "1":
                System.out.println("Type the coordinates of the cargo you want to lose (row col):");
                System.out.print(" > ");
                String cargoInput = scanner.nextLine().trim();
                int x;
                int y;
                try {
                    x = Integer.parseInt(cargoInput.split(" ")[0]) - 5;
                    y = Integer.parseInt(cargoInput.split(" ")[1]) - 4;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                    return false;
                }
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);

                System.out.println("Type the color of the cargo you want to lose (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                System.out.print(" > ");
                String cargoColorInput = scanner.nextLine().trim();
                CargoColor cargoColor;
                try {
                    cargoColor = CargoColor.valueOf(cargoColorInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                    return false;
                }
                ClientGameModel.getInstance().getClient().unloadCargo(username, cargoColor, coordinates);
                break;
            case "2":
                System.out.println("Type the coordinates of the cargo you want to move from (row col):");
                System.out.print(" > ");
                String moveInput = scanner.nextLine().trim();
                int moveX;
                int moveY;
                try {
                    moveX = Integer.parseInt(moveInput.split(" ")[0]) - 5;
                    moveY = Integer.parseInt(moveInput.split(" ")[1]) - 4;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                    return false;
                }
                Pair<Integer, Integer> moveCoordinates = new Pair<>(moveX, moveY);
                System.out.println("Type the coordinates of the cargo you want to move to (row col):");
                System.out.print(" > ");
                String moveToInput = scanner.nextLine().trim();
                int moveToX;
                int moveToY;
                try {
                    moveToX = Integer.parseInt(moveToInput.split(" ")[0]) - 5;
                    moveToY = Integer.parseInt(moveToInput.split(" ")[1]) - 4;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                    return false;
                }
                Pair<Integer, Integer> moveToCoordinates = new Pair<>(moveToX, moveToY);
                System.out.println("Type the color of the cargo you want to move (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                System.out.print(" > ");
                String moveCargoColorInput = scanner.nextLine().trim();
                CargoColor moveCargoColor;
                try {
                    moveCargoColor = CargoColor.valueOf(moveCargoColorInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                    return false;
                }
                ClientGameModel.getInstance().getClient().moveCargo(username, moveCargoColor, moveCoordinates, moveToCoordinates);
                break;
            case "3":
                if (losing) {
                    ClientGameModel.getInstance().getClient().endMove(username);
                } else {
                    System.out.println("Type the coordinates of the cargo hold you want to load to (row col):");
                    System.out.print(" > ");
                    String loadInput = scanner.nextLine().trim();
                    int loadX;
                    int loadY;
                    try {
                        loadX = Integer.parseInt(loadInput.split(" ")[0]) - 5;
                        loadY = Integer.parseInt(loadInput.split(" ")[1]) - 4;
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        return false;
                    }
                    Pair<Integer, Integer> loadCoordinates = new Pair<>(loadX, loadY);
                    System.out.println("Type the color of the cargo you want to load (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                    System.out.print(" > ");
                    String loadCargoColorInput = scanner.nextLine().trim();
                    CargoColor loadCargoColor;
                    try {
                        loadCargoColor = CargoColor.valueOf(loadCargoColorInput.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                        return false;
                    }
                    ClientGameModel.getInstance().getClient().loadCargo(username, loadCargoColor, loadCoordinates);
                }
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                return false;
            case "4":
                if (!losing) {
                    ClientGameModel.getInstance().getClient().endMove(username);
                    break;
                }
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                return false;
        }
        return true;
    }

    @Override
    public String getStateName() {
        return "Cargo Menu";
    }
}
