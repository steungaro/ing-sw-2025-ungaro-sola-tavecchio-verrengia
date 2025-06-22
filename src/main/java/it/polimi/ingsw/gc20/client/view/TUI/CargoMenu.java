package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
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
public class CargoMenu implements MenuState, GameModelListener {
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
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1mCargo Menu\u001B[0m");
        System.out.println(message);
        if (losing) {
            System.out.println("You have to lose \u001B[31m" + cargoToLose + "\u001B[0m cargo.");
            System.out.println("1. Lose cargo");
            System.out.println("2. End turn");
        } else {
            System.out.println("You have to gain " +
                    cargoToGain.entrySet().stream().map(e -> e.getValue().toString() + " " + e.getKey().toString()).reduce((a, b) -> a + ", " + b).orElse("") +
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
        ClientGameModel.getInstance().addListener(this);
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }


    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().removeListener(this);
        ClientGameModel.getInstance().setBusy();
        // Handle user input from the cargo menu
        switch (choice) {
            case "1":
                int x;
                int y;
                ClientGameModel.getInstance().printShip(username);
                do {
                    System.out.println("Type the coordinates of the cargo you want to lose (row col) or [b] to go back:");
                    System.out.print(" > ");
                    String cargoInput = scanner.nextLine().trim();
                    if (cargoInput.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (cargoInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    try {
                        x = Integer.parseInt(cargoInput.split(" ")[0]) - 5;
                        y = Integer.parseInt(cargoInput.split(" ")[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        x = -1;
                        y = -1;
                    }
                } while (x < 0 || x > 4 || y < 0 || y > 6);
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);

                CargoColor cargoColor;
                do {
                    System.out.println("Type the color of the cargo you want to lose (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m) or [b] to go back:");
                    System.out.print(" > ");
                    String cargoColorInput = scanner.nextLine().trim();
                    if (cargoColorInput.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (cargoColorInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
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
                if (losing) {
                    ClientGameModel.getInstance().getClient().endMove(username);
                    break;
                } else {
                    int moveX;
                    int moveY;
                    ClientGameModel.getInstance().printShip(username);
                    do {
                        System.out.println("Type the coordinates of the cargo you want to move from (row col) or [b] to go back:");
                        System.out.print(" > ");
                        String moveInput = scanner.nextLine().trim();
                        if (moveInput.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (moveInput.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        try {
                            moveX = Integer.parseInt(moveInput.split(" ")[0]) - 5;
                            moveY = Integer.parseInt(moveInput.split(" ")[1]) - 4;
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                            moveX = -1;
                            moveY = -1;
                        }
                    } while (moveX < 0 || moveX > 4 || moveY < 0 || moveY > 6);
                    Pair<Integer, Integer> moveCoordinates = new Pair<>(moveX, moveY);
                    int moveToX;
                    int moveToY;
                    do {
                        System.out.println("Type the coordinates of the cargo you want to move to (row col) or [b] to go back:");
                        System.out.print(" > ");
                        String moveToInput = scanner.nextLine().trim();
                        if (moveToInput.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (moveToInput.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        try {
                            moveToX = Integer.parseInt(moveToInput.split(" ")[0]) - 5;
                            moveToY = Integer.parseInt(moveToInput.split(" ")[1]) - 4;
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                            moveToX = -1;
                            moveToY = -1;
                        }
                    } while (moveToX < 0 || moveToX > 4 || moveToY < 0 || moveToY > 6);
                    Pair<Integer, Integer> moveToCoordinates = new Pair<>(moveToX, moveToY);
                    CargoColor moveCargoColor;
                    do {
                        System.out.println("Type the color of the cargo you want to move (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m) or [b] to go back:");
                        System.out.print(" > ");
                        String moveCargoColorInput = scanner.nextLine().trim();
                        if (moveCargoColorInput.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (moveCargoColorInput.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        try {
                            moveCargoColor = CargoColor.valueOf(moveCargoColorInput.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                            moveCargoColor = null;
                        }
                    } while (moveCargoColor == null);
                    ClientGameModel.getInstance().getClient().moveCargo(username, moveCargoColor, moveCoordinates, moveToCoordinates);
                }
                break;
            case "3":
                if (!losing) {
                    int loadX;
                    int loadY;
                    ClientGameModel.getInstance().printShip(username);
                    do {
                        System.out.println("Type the coordinates of the cargo hold you want to load to (row col) or [b] to go back:");
                        System.out.print(" > ");
                        String loadInput = scanner.nextLine().trim();
                        if (loadInput.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (loadInput.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        try {
                            loadX = Integer.parseInt(loadInput.split(" ")[0]) - 5;
                            loadY = Integer.parseInt(loadInput.split(" ")[1]) - 4;
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                            loadX = -1;
                            loadY = -1;
                        }
                    } while (loadX < 0 || loadX > 4 || loadY < 0 || loadY > 6);
                    Pair<Integer, Integer> loadCoordinates = new Pair<>(loadX, loadY);
                    CargoColor loadCargoColor;
                    do {
                        System.out.println("Type the color of the cargo you want to load (\u001B[31mRED\u001B[0m/\u001B[33mYELLOW\u001B[0m/\u001B[32mGREEN\u001B[0m/\u001B[34mBLUE\u001B[0m) or [b] to go back:");
                        System.out.print(" > ");
                        String loadCargoColorInput = scanner.nextLine().trim();
                        if (loadCargoColorInput.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (loadCargoColorInput.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        try {
                            loadCargoColor = CargoColor.valueOf(loadCargoColorInput.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter a valid cargo color.\u001B[0m");
                            loadCargoColor = null;
                        }
                    } while (loadCargoColor == null);
                    ClientGameModel.getInstance().getClient().loadCargo(username, loadCargoColor, loadCoordinates);
                } else {
                    System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                    System.out.print(" > ");
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
                System.out.print(" > ");
                break;
        }
        ClientGameModel.getInstance().setFree();
    }

    @Override
    public String getStateName() {
        return "Cargo Menu";
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        ClientGameModel.getInstance().setCurrentMenuStateNoClear(this);
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    @Override
    public void onErrorMessageReceived(String message) {

    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    @Override
    public void onBoardUpdated(ViewBoard board) {

    }

    @Override
    public void onCardUpdated(ViewAdventureCard card) {

    }
}
