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
 * Represents the menu state where the user is required to lose energy
 * due to a shortage of cargo in the game. This menu provides options for
 * handling energy loss and interacting with the relevant game elements.
 * Implements the {@link MenuState} interface to define the behavior of the Lose-Energy menu.
 */
public class LoseEnergyMenu implements MenuState, GameModelListener {
    private final Scanner scanner;
    private final int energyToLose;
    private final String username = ClientGameModel.getInstance().getUsername();

    public LoseEnergyMenu(int energyToLose) {
        this.scanner = new Scanner(System.in);
        this.energyToLose = energyToLose;
    }

    /**
     * Displays the LoseEnergy menu to the user. This menu informs the user about
     * the amount of energy they need to lose as a consequence of being short on cargo
     * and provides options to either proceed with the energy loss or view additional game options.
     */
    @Override
    public void displayMenu() {
        ClientGameModel.getInstance().addListener(this);
        ClientGameModel.getInstance().printShip(username);
        System.out.println("\u001B[1mLose Energy Menu\u001B[22m");
        System.out.println("You have to lose \u001B[31m" + energyToLose + "\u001B[0m energy because you are short on cargo!");
        System.out.println("1. Continue");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the LoseEnergy menu with an error message.
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
     * Handles user input for the LoseEnergy menu.
     * This method processes the user's choice, allowing them to continue with losing energy,
     * view options, or quit the game. It prompts the user for the coordinates of the battery
     * from which they want to lose energy.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().removeListener(this);
        ClientGameModel.getInstance().setBusy();
        if (choice.equals("1")) {
            int x;
            int y;
            ClientGameModel.getInstance().printShip(username);
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
                    y = Integer.parseInt(batteryInput.split(" ")[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4); // Adjusting for 0-indexed array
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
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
            System.out.print(" > ");
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Returns the name of the current state, which is used for identification.
     *
     * @return the name of the current state as a string
     */
    @Override
    public String getStateName() {
        return "LoseEnergyMenu";
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
        // ignore
    }
}
