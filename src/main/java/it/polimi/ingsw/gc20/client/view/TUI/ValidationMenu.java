package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The ValidationMenu class represents the menu state where users can validate their game ship,
 * remove a component from the ship, or navigate to other game options.
 * It implements the {@link MenuState} interface and provides methods to display the menu,
 * handle user input, and get the name of the current state.
 */
public class ValidationMenu implements MenuState, GameModelListener {
    public final Scanner scanner = new Scanner(System.in);
    public String username = ClientGameModel.getInstance().getUsername();


    public ValidationMenu(){
    }

    /**
     * Displays the validation menu for the user, providing feedback on the validation status
     * of the user's ship and listing available actions based on the current state.
     * If the ship is already valid, the user is informed and prompted to wait for other players.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().addListener(this);
        if(!ClientGameModel.getInstance().getShip(username).isValid()){
            ClientGameModel.getInstance().printShip(username);
        }
        System.out.println("\u001B[1mValidation Menu\u001B[22m");
        if(ClientGameModel.getInstance().getShip(username).isValid()){
            System.out.println("\u001B[32mShip is already valid! Wait for other players before going to the next phase.\u001B[0m");
        } else {
            System.out.println("Ship is not valid");
            System.out.println("1. Remove a component from the ship");
        }
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the validation menu for the user with an error message highlighted in red.
     * This allows the user to view a specific error before being redirected to the
     * standard validation menu for further actions.
     *
     * @param errorMessage the error message to display to the user
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the validation menu. This method processes the user's choice,
     * allowing them to validate their ship, remove a component, view options, or quit the game.
     * It also manages the state of the game model to ensure that actions are performed correctly.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().removeListener(this);
        ClientGameModel.getInstance().setBusy();
        if (ClientGameModel.getInstance().getShip(username).isValid()){
            if (choice.equalsIgnoreCase("v")) {
                TUI.viewOptionsMenu();
                ClientGameModel.getInstance().setFree();
                return;
            } else if (choice.equalsIgnoreCase("q")) {
                ClientGameModel.getInstance().shutdown();
                return;
            } else {
                System.out.println("\u001B[33mInvalid input. Please try again.\u001B[0m");
                System.out.print(" > ");
                ClientGameModel.getInstance().setFree();
                return;
            }
        }
        // Handle user input from the validation menu
        switch (choice) {
            case "1":
                // Remove a component from the ship
                int row, col;
                do {
                    System.out.println("Type the coordinates of the component you want to remove (row col) or [b] to go back:");
                    System.out.print(" > ");
                    String componentName = scanner.nextLine().trim();
                    if (componentName.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    if (componentName.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    try {
                        row = Integer.parseInt(componentName.split(" ")[0]) - 5;
                        col = Integer.parseInt(componentName.split(" ")[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                        row = -1;
                        col = -1;
                    }
                } while (row < 0 || row > 4 || col < 0 || col > 6);
                Pair<Integer, Integer> coordinates = new Pair<>(row, col);
                ClientGameModel.getInstance().getClient().removeComponentFromShip(ClientGameModel.getInstance().getUsername(), coordinates);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                System.out.print(" > ");
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    @Override
    public String getStateName(){
        return "Validation Menu";
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        ClientGameModel.getInstance().setCurrentMenuState(this);
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
        // ignore
    }

    @Override
    public void onBoardUpdated(ViewBoard board) {

    }
}
