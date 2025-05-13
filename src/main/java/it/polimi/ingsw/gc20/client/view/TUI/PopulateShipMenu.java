package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import org.javatuples.Pair;
import java.rmi.RemoteException;

public class PopulateShipMenu {
    MenuContext menuContext;
    /**
     * Displays the current menu to the player
     */
    public void displayMenu(){
        System.out.println("Populate Ship Menu");
        System.out.println("1. End population phase, all other cabins will be filled with astronauts");
        if(menuContext.getLobby().getLevel() == 1) {
            System.out.println("2. Add alien to a Cabin with the arguments: <color of the alien> <x> <y>");
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws RemoteException{
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the populate ship menu
        switch (choice) {
            case 1:
                menuContext.getClient().readyToFly(menuContext.getUsername());
                break;
            case 2:
                if(menuContext.getLobby().getLevel() == 1) {
                    AlienColor color = AlienColor.valueOf(menuContext.getScanner().next());
                    int x = menuContext.getScanner().nextInt();
                    int y = menuContext.getScanner().nextInt();
                    menuContext.getClient().addAlien(menuContext.getUsername(), color, new Pair<>(x, y)); //TODO remove the alien color argument
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Populate Ship Menu";
    }
}
