package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents the Cargo Menu in the game where players can manage their cargo.
 * The menu allows players to choose various actions based on their current
 * game state, such as losing, gaining, moving, or loading cargo.
 * Implements the {@link MenuState} interface to represent a specific menu state.
 */
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
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }


    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        // Handle user input from the cargo menu
        switch (choice) {
            case "1":
                int x;
                int y;
                do {
                    System.out.println("Type the coordinates of the cargo you want to lose (row col):");
                    System.out.print(" > ");
                    String cargoInput = scanner.nextLine().trim();
                    try {
                        x = Integer.parseInt(cargoInput.split(" ")[0]) - 5;
                        y = Integer.parseInt(cargoInput.split(" ")[1]) - 4;
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        x = -1;
                        y = -1;
                    }
                } while (x < 0 || x > 4 || y < 0 || y > 6);
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);

                CargoColor cargoColor;
                do {
                    System.out.println("Type the color of the cargo you want to lose (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                    System.out.print(" > ");
                    String cargoColorInput = scanner.nextLine().trim();
                    try {
                        cargoColor = CargoColor.valueOf(cargoColorInput.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                        cargoColor = null;
                    }
                } while (cargoColor == null);
                ClientGameModel.getInstance().getClient().unloadCargo(username, cargoColor, coordinates);
                break;
            case "2":
                int moveX;
                int moveY;
                do {
                    System.out.println("Type the coordinates of the cargo you want to move from (row col):");
                    System.out.print(" > ");
                    String moveInput = scanner.nextLine().trim();
                    try {
                        moveX = Integer.parseInt(moveInput.split(" ")[0]) - 5;
                        moveY = Integer.parseInt(moveInput.split(" ")[1]) - 4;
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        moveX = -1;
                        moveY = -1;
                    }
                } while (moveX < 0 || moveX > 4 || moveY < 0 || moveY > 6);
                Pair<Integer, Integer> moveCoordinates = new Pair<>(moveX, moveY);
                int moveToX;
                int moveToY;
                do {
                    System.out.println("Type the coordinates of the cargo you want to move to (row col):");
                    System.out.print(" > ");
                    String moveToInput = scanner.nextLine().trim();
                    try {
                        moveToX = Integer.parseInt(moveToInput.split(" ")[0]) - 5;
                        moveToY = Integer.parseInt(moveToInput.split(" ")[1]) - 4;
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        moveToX = -1;
                        moveToY = -1;
                    }
                } while (moveToX < 0 || moveToX > 4 || moveToY < 0 || moveToY > 6);
                Pair<Integer, Integer> moveToCoordinates = new Pair<>(moveToX, moveToY);
                CargoColor moveCargoColor;
                do {
                    System.out.println("Type the color of the cargo you want to move (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                    System.out.print(" > ");
                    String moveCargoColorInput = scanner.nextLine().trim();
                    try {
                        moveCargoColor = CargoColor.valueOf(moveCargoColorInput.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                        moveCargoColor = null;
                    }
                } while (moveCargoColor == null);
                ClientGameModel.getInstance().getClient().moveCargo(username, moveCargoColor, moveCoordinates, moveToCoordinates);
                break;
            case "3":
                if (losing) {
                    ClientGameModel.getInstance().getClient().endMove(username);
                } else {
                    int loadX;
                    int loadY;
                    do {
                        System.out.println("Type the coordinates of the cargo hold you want to load to (row col):");
                        System.out.print(" > ");
                        String loadInput = scanner.nextLine().trim();
                        try {
                            loadX = Integer.parseInt(loadInput.split(" ")[0]) - 5;
                            loadY = Integer.parseInt(loadInput.split(" ")[1]) - 4;
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                            loadX = -1;
                            loadY = -1;
                        }
                    } while (loadX < 0 || loadX > 4 || loadY < 0 || loadY > 6);
                    Pair<Integer, Integer> loadCoordinates = new Pair<>(loadX, loadY);
                    CargoColor loadCargoColor;
                    do {
                        System.out.println("Type the color of the cargo you want to load (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m):");
                        System.out.print(" > ");
                        String loadCargoColorInput = scanner.nextLine().trim();
                        try {
                            loadCargoColor = CargoColor.valueOf(loadCargoColorInput.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                            loadCargoColor = null;
                        }
                    } while (loadCargoColor == null);
                    ClientGameModel.getInstance().getClient().loadCargo(username, loadCargoColor, loadCoordinates);
                }
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            case "4":
                if (!losing) {
                    ClientGameModel.getInstance().getClient().endMove(username);
                    break;
                }
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                break;
        }
        ClientGameModel.getInstance().setFree();
    }

    @Override
    public String getStateName() {
        return "Cargo Menu";
    }
}
