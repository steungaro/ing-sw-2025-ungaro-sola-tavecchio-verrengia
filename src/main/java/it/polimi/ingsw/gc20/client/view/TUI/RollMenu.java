package it.polimi.ingsw.gc20.client.view.TUI;

import java.rmi.RemoteException;

public class RollMenu implements MenuState{
    private final MenuContext menuContext;
    private final String stateName = "Roll Menu";

    public RollMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("1. Roll dice");
    }

    @Override
    public boolean handleInput() throws RemoteException {
        int choice = menuContext.getScanner().nextInt();
        if(choice != 1){
            System.out.println("Invalid choice. Please try again.");
            return false;
        }
        menuContext.getClient().rollDice(menuContext.getUsername());
        return true;
    }
}
