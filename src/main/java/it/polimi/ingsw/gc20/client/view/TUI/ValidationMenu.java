package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.javatuples.Pair;

import java.rmi.RemoteException;

public class ValidationMenu implements MenuState{
    private final MenuContext menuContext;


    public ValidationMenu(MenuContext menuContext) throws RemoteException {
        this.menuContext = menuContext;
        menuContext.getShip().setValid(false);
        menuContext.getClient().validateShip(menuContext.getUsername());

    }

    public void displayMenu(){
        if(menuContext.getShip().isValid()){
            System.out.println("Ship is already valid wait for other players before going to the next phase");
        } else {
            System.out.println("Ship is not valid");
            System.out.println("1. Validate ship");
            System.out.println("2. Remove a component from the ship with the arguments: <x> <y>");
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws RemoteException{
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the validation menu
        if(menuContext.getShip().isValid()){
            switch (choice) {
                case 1:
                    menuContext.getClient().validateShip(menuContext.getUsername());
                    break;
                case 2:
                    int x = menuContext.getScanner().nextInt();
                    int y = menuContext.getScanner().nextInt();
                    menuContext.getClient().removeComponentFromShip(menuContext.getUsername(), new Pair<>(x, y));
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        } else {
            System.out.println("Ship is already valid wait for other players before going to the next phase");
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Validation Menu";
    }
}
