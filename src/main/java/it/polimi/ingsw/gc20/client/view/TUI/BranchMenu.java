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
        TUI.clearConsole();
        System.out.println("\u001B[1mBranch Menu\u001B[22m");
        System.out.println("Your ship has split into two branches.");
    }

    public boolean handleInput() throws RemoteException {
            int row;
            int col;
        do {
            System.out.println("Type the coordinates of the branch you want to keep (row col):");
            System.out.print(" > ");
            String branchInput = scanner.nextLine().trim();
            if (branchInput.equals("q")) {
                ClientGameModel.getInstance().shutdown();
                return false;
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
        ClientGameModel.getInstance().getClient().chooseBranch(username, new Pair<>(row, col) );
        return true;
    }

    public String getStateName() {
        return "BranchMenu";
    }
}
