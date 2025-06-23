package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Represents the Shields Menu state in the game, allowing users to interact with
 * options related to activating shields or other related actions.
 * Implements the {@link MenuState} interface to provide behavior for menu rendering, input handling,
 * and menu state retrieval.
 */
public class ShieldsMenu implements MenuState, GameModelListener {
    private final Scanner scanner = new Scanner(System.in);
    private final String message;
    private final String username = ClientGameModel.getInstance().getUsername();

    public ShieldsMenu(String message) {
        this.message = message;
    }

    /**
     * Displays the shields' menu to the user.
     * The menu contains options for activating a shield, declining to activate a shield,
     * and viewing game options. The method also displays a message that provides
     * additional context or instructions to the user.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().addListener(this);
        ClientGameModel.getInstance().printBoard();
        System.out.println("\u001B[1mShields Menu\u001B[22m");
        System.out.println(message);
        System.out.println("1. Activate a shield");
        System.out.println("2. Do not activate a shield");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the shields' menu with an error message.
     * This method is called when there is an error in user input or processing.
     * It prints the error message in red and then displays the menu again.
     *
     * @param errorMessage The error message to display
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the Shields menu.
     * This method processes the user's choice, allowing them to activate a shield,
     * decline to activate a shield, view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().removeListener(this);
        ClientGameModel.getInstance().setBusy();
        // Handle user input from the engine menu
        switch (choice) {
            case "1":
                int xs, ys;
                ClientGameModel.getInstance().printShip(username);
                do {
                    System.out.println("Type the coordinates of the shield you want to activate (row col) or [b] to go back:");
                    System.out.print(" > ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    if (input.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    String[] inputCoordinates = input.split(" ");
                    try {
                        xs = Integer.parseInt(inputCoordinates[0]) - 5;
                        ys = Integer.parseInt(inputCoordinates[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
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
                        return;
                    }
                    String[] batteryCoordinates = batteryInput.split(" ");
                    try {
                        xb = Integer.parseInt(batteryCoordinates[0]) - 5;
                        yb = Integer.parseInt(batteryCoordinates[1]) - 4;
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                        xb = -1;
                        yb = -1;
                    }
                } while (xb < 0 || yb < 0 || xb > 4 || yb > 6);
                ClientGameModel.getInstance().getClient().activateShield(username, new Pair<>(xs, ys), new Pair<>(xb, yb));
                break;
            case "2":
                ClientGameModel.getInstance().getClient().activateShield(username, null, null);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            default:
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                System.out.print(" > ");
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Returns the name of the current menu state.
     * This method is used to identify the current menu state in the game.
     *
     * @return The name of the current menu state
     */
    @Override
    public String getStateName() {
        return "Shields Menu";
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
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {

    }
}
