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
    private final Map<CargoColor, Integer> cargoToGain;

    public CargoMenu(String message, int cargoToLose, Map<CargoColor, Integer> cargoToGain) {
        this.message = message;
        this.cargoToLose = cargoToLose;
        this.cargoToGain = cargoToGain;
    }

    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("Cargo Menu");
        System.out.println(message);
        if (cargoToLose > 0) {
            System.out.println("You have to lose " + cargoToLose + " cargo.");
            System.out.println("1. Lose cargo");
            System.out.println("2. Move cargo");
            System.out.println("3. Lose energy (only if you don't have enough cargo to lose)");
            System.out.println("4. End turn");
        } else {
            System.out.println("You have to gain " +
                    cargoToGain.entrySet().stream().map(e -> e.getKey().toString() + " " + e.getValue()) +
                    " cargo.");
            System.out.println("1. Unload cargo");
            System.out.println("2. Move cargo");
            System.out.println("3. Load cargo");
            System.out.println("4. End turn");
        }
        System.out.print(" > ");
    }


    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        // Handle user input for the cargo menu
        switch (choice) {
            case "1":
                System.out.println("Type the coordinates of the cargo you want to lose (x y):");
                System.out.print(" > ");
                String cargoInput = scanner.nextLine().trim();
                int x = Integer.parseInt(cargoInput.split(" ")[0]) - 5;
                int y = Integer.parseInt(cargoInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);

                System.out.println("Type the color of the cargo you want to lose (RED/YELLOW/GREEN/BLUE):");
                System.out.print(" > ");
                String cargoColorInput = scanner.nextLine().trim();
                CargoColor cargoColor = CargoColor.valueOf(cargoColorInput.toUpperCase());
                ClientGameModel.getInstance().getClient().unloadCargo(username, cargoColor, coordinates);
                break;
            case "2":
                System.out.println("Type the coordinates of the cargo you want to move from (x y):");
                System.out.print(" > ");
                String moveInput = scanner.nextLine().trim();
                int moveX = Integer.parseInt(moveInput.split(" ")[0]) - 5;
                int moveY = Integer.parseInt(moveInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> moveCoordinates = new Pair<>(moveX, moveY);
                System.out.println("Type the coordinates of the cargo you want to move to (x y):");
                System.out.print(" > ");
                String moveToInput = scanner.nextLine().trim();
                int moveToX = Integer.parseInt(moveToInput.split(" ")[0]) - 5;
                int moveToY = Integer.parseInt(moveToInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> moveToCoordinates = new Pair<>(moveToX, moveToY);
                System.out.println("Type the color of the cargo you want to move (RED/YELLOW/GREEN/BLUE):");
                System.out.print(" > ");
                String moveCargoColorInput = scanner.nextLine().trim();
                CargoColor moveCargoColor = CargoColor.valueOf(moveCargoColorInput.toUpperCase());
                ClientGameModel.getInstance().getClient().moveCargo(username, moveCargoColor, moveCoordinates, moveToCoordinates);
                break;
            case "3":
                if (cargoToLose > 0) {
                    System.out.println("Type the coordinates of the battery you want to lose energy from (x y):");
                    System.out.print(" > ");
                    String batteryInput = scanner.nextLine().trim();
                    Pair<Integer, Integer> batteryCoordinates = new Pair<>(Integer.parseInt(batteryInput.split(" ")[0]) - 5, Integer.parseInt(batteryInput.split(" ")[1]) - 4);
                    ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
                } else {
                    System.out.println("Type the coordinates of the cargo hold you want to load to (x y):");
                    System.out.print(" > ");
                    String loadInput = scanner.nextLine().trim();
                    int loadX = Integer.parseInt(loadInput.split(" ")[0]) - 5;
                    int loadY = Integer.parseInt(loadInput.split(" ")[1]) - 4;
                    Pair<Integer, Integer> loadCoordinates = new Pair<>(loadX, loadY);
                    System.out.println("Type the color of the cargo you want to load (RED/YELLOW/GREEN/BLUE):");
                    System.out.print(" > ");
                    String loadCargoColorInput = scanner.nextLine().trim();
                    CargoColor loadCargoColor = CargoColor.valueOf(loadCargoColorInput.toUpperCase());
                    ClientGameModel.getInstance().getClient().loadCargo(username, loadCargoColor, loadCoordinates);
                }
                break;
            case "4":
                ClientGameModel.getInstance().getClient().endMove(username);
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

    @Override
    public String getStateName() {
        return "Cargo Menu";
    }
}
