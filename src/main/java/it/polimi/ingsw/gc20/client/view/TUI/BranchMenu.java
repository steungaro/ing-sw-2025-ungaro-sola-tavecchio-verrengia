package it.polimi.ingsw.gc20.client.view.TUI;

import org.javatuples.Pair;

import java.rmi.RemoteException;

public class BranchMenu implements MenuState{
    private final MenuContext menuContext;
    private final String stateName = "Branch Menu";

    public BranchMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("Your ship is split, you can only keep one branch, choose wisely:");
        System.out.println(menuContext.getShip().toString());
    }

    public boolean handleInput() throws RemoteException {
        int x = menuContext.getScanner().nextInt();
        int y = menuContext.getScanner().nextInt();
        menuContext.getClient().chooseBranch(menuContext.getUsername(), new Pair<>(x, y));
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
