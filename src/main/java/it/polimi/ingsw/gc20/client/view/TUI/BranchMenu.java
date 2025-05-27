package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.Scanner;

public class BranchMenu implements MenuState {
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public BranchMenu() {
    }

    public void displayMenu() {
        System.out.println("\u001B[1mBranch Menu\u001B[22m");
        System.out.println("Your ship has split into two branches.");
        System.out.println("Press any key to continue.");
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

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
                col = Integer.parseInt(branchInput.split(" ")[1]) - 4;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mInvalid input. Please enter two integers separated by a space.\u001B[0m");
                row = -1;
                col = -1;
            }
        } while (row < 0 || row > 4 || col < 0 || col > 6);
        ClientGameModel.getInstance().getClient().chooseBranch(username, new Pair<>(row, col));
        ClientGameModel.getInstance().setFree();
    }

    public String getStateName() {
        return "BranchMenu";
    }
}
