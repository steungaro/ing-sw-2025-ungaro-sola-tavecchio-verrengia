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
        System.out.println("Type the coordinates of the branch you want to keep (x y):");
        System.out.print(" > ");
        String branchInput = scanner.nextLine().trim();
        int x = Integer.parseInt(branchInput.split(" ")[0]) - 5;
        int y = Integer.parseInt(branchInput.split(" ")[1]) - 4;
        ClientGameModel.getInstance().getClient().chooseBranch(username, new Pair<>(x, y) );
        return true;
    }

    public String getStateName() {
        return "BranchMenu";
    }
}
