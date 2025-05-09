package it.polimi.ingsw.gc20.client.view.TUI;

import org.javatuples.Triplet;

import java.rmi.RemoteException;

public class AbandShipMenu implements MenuState{

    private final MenuContext menuContext;
    private boolean shipBoarded = false;

    public AbandShipMenu(MenuContext menuContext){
        this.menuContext = menuContext;
    }
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        if(!shipBoarded) {
            System.out.println("Abandoned Ship Menu");
            System.out.println("1. Board Ship");
            System.out.println("2. Don't Board Ship");
        }else{
            System.out.println("Choose the astronauts that will board the ship");
            new List <Triplet<Integer, Integer, Integer>> crew = menuContext.getShip().getCrew();
            for(astr in crew)
            //TODO handle astronauts
        }
    }

    /**
     * Handles user input for the current menu
     *
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws RemoteException {
        if (!shipBoarded) {
            int choice = menuContext.getScanner().nextInt();
            // Handle user input for the abandon ship menu
            switch (choice) {
                case 1:

                    break;
                case 2:
                    menuContext.getClient().abandonShip(menuContext.getUsername());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
            return false;
        }
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    String getStateName();
}
