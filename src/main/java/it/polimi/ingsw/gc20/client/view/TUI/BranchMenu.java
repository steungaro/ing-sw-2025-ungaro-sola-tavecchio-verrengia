package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Represents the menu state where the player interacts with the branch menu of the game.
 * This menu is shown when the player's ship splits into two branches, requiring the player
 * to choose one branch to keep.
 * It implements the {@link MenuState} interface to define the behavior of the branch menu.
 */
public class BranchMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public BranchMenu() {
    }

    /**
     * Displays the branch menu to the player. This method prints the menu header and
     * provides a prompt for the player's input. The menu informs the player that the
     * ship has split into two branches and instructs them to press any key to proceed.
     */
    @Override
    public void displayMenu() {
        System.out.println("\u001B[1mBranch Menu\u001B[22m");
        System.out.println("Your ship has split into two branches.");
        System.out.println("Press any key to continue.");
        System.out.print(" > ");
    }

    /**
     * Displays the branch menu with an error message. This method prints the provided error
     * message in red text and then displays the menu to the player.
     *
     * @param errorMessage the error message to be displayed before the menu
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles input from the player for selecting a specific branch in the game.
     * Prompts the user to type coordinates of the branch they wish to keep, processes
     * the input, and communicates the selection to the game client.
     * If the input is invalid, the user is prompted again until valid input is entered.
     *
     * @param choice the player's initial input choice, though this parameter is unused in the current implementation
     * @throws RemoteException if a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        int row;
        int col;
        ClientGameModel.getInstance().setBusy();
        do {
            System.out.println("Type the coordinates of the branch you want to keep (row col):");
            System.out.print(" > ");
            String branchInput = scanner.nextLine().trim();
            if (branchInput.equals("q")) {
                ClientGameModel.getInstance().shutdown();
                return;
            }
            try {
                row = Integer.parseInt(branchInput.split(" ")[0]) - 5;
                col = Integer.parseInt(branchInput.split(" ")[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                row = -1;
                col = -1;
            }
        } while (row < 0 || row > 4 || col < 0 || col > 6);
        ClientGameModel.getInstance().getClient().chooseBranch(username, new Pair<>(row, col));
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Returns the name of the current state, which is used for identification purposes.
     *
     * @return the name of the current state as a string
     */
    @Override
    public String getStateName() {
        return "BranchMenu";
    }
}
