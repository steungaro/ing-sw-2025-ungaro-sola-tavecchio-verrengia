package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the menu state where the player is required to lose crew members.
 * This class is responsible for displaying the menu, handling user input,
 * and managing the actions related to losing crew.
 * It implements the {@link MenuState} interface to define the behavior of the Lose Crew menu.
 */
public class LoseCrewMenu implements MenuState {
    private final Scanner scanner;
    private final int crewToLose;
    private final String username = ClientGameModel.getInstance().getUsername();

    public LoseCrewMenu(int crewToLose) {
        this.scanner = new Scanner(System.in);
        this.crewToLose = crewToLose;
    }

    /**
     * Displays the lose-crew menu to the user, indicating the number of crew members
     * that need to be lost and presenting the available options for further actions.
     */
    @Override
    public void displayMenu() {
        ClientGameModel.getInstance().printShip(username);
        System.out.println("\u001B[1mLose Crew Menu\u001B[22m");
        System.out.println("You have to lose \u001B[31m" + crewToLose + "\u001B[0m crew members!");
        System.out.println("1. Continue");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the lose-crew menu with an error message.
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
     * Handles user input for the lose-crew menu.
     * This method processes the user's choice, allowing them to continue with losing crew members,
     * view options, or quit the game.
     *
     * @param choice The user's input choice
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        if (choice.equals("1")) {
            ClientGameModel.getInstance().printShip(username);
            boolean inputOk = true;
            List<Pair<Integer, Integer>> cabins = new java.util.ArrayList<>();
            do {
                System.out.println("Type the coordinates of the cabins you want to lose crew from separated by spaces (for example, row1 col1 row2 col2):");
                System.out.print(" > ");
                cabins.clear();
                String cabinInput = scanner.nextLine().trim();
                if (cabinInput.equals("q")) {
                    ClientGameModel.getInstance().shutdown();
                    return;
                }
                String[] cabinCoordinates = cabinInput.split(" ");
                if (cabinCoordinates.length % 2 != 0) {
                    System.out.println("\u001B[31mInvalid input. Please enter an even number of coordinates.\u001B[0m");
                    inputOk = false;
                }
                for (int i = 0; inputOk && i < cabinCoordinates.length; i += 2) {
                    try {
                        Pair<Integer, Integer> coordinates = new Pair<>(Integer.parseInt(cabinCoordinates[i]) - 5, Integer.parseInt(cabinCoordinates[i + 1]) - (ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()).isLearner ? 5 : 4));
                        cabins.add(coordinates);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter valid integers for coordinates.\u001B[0m");
                        inputOk = false;
                    }
                }
            } while (!inputOk);
            ClientGameModel.getInstance().getClient().loseCrew(username, cabins);
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
}
