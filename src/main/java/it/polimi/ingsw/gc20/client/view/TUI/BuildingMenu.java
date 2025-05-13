package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.javatuples.Pair;

import java.rmi.RemoteException;

public class BuildingMenu implements MenuState{
    private final MenuContext menuContext;
    private final ViewShip ship;
    private ViewComponent componentInHand;
    public BuildingMenu(MenuContext menuContext, ViewShip ship) {
        this.ship = ship;
        this.menuContext = menuContext;
    }

    public void displayMenu(){
        if(componentInHand == null) {
            System.out.println("Building Ship Menu");
            System.out.println("1. Take component from covered components with the argument: <componentIndex>");
            System.out.println("2. Take component from uncovered components with the argument: <componentIndex>");
            if(!ship.isLearner) {
                System.out.println("3. Take component from the stored components with the argument: <componentIndex>");
                System.out.println("4. Turn hourglass");
                System.out.println("5. Check a deck of cards with argument: <deckIndex>");
            }
        }else {
            System.out.println("Building Ship Menu");
            System.out.println("1. Put back the component in hand");
            System.out.println("2. Add the component in hand to your ship with the arguments: <x> <y>");
            System.out.println("3. Rotate the component in hand Clockwise with argument: <numberOfRotations>");
            System.out.println("4. Rotate the component in hand Counter-Clockwise with argument: <numberOfRotations>");
            if(!ship.isLearner) {
                System.out.println("5. Add the component in hand to stored components");
                System.out.println("6. Turn hourglass");
                System.out.println("7. Check a deck of cards with argument: <deckIndex>");
            }
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws RemoteException{
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the building menu
        if(componentInHand == null) {
            switch (choice) {
                case 1:
                    int num = menuContext.getScanner().nextInt();
                    menuContext.getClient().takeComponentFromUnviewed(menuContext.getUsername(), num);
                    break;
                case 2:
                    int num1 = menuContext.getScanner().nextInt();
                    menuContext.getClient().takeComponentFromViewed(menuContext.getUsername(), num1);
                    break;
                case 3:
                    componentInHand = ship.getBooked(menuContext.getScanner().nextInt());
                    break;
                case 4:
                    menuContext.getClient().turnHourglass(menuContext.getUsername());
                    break;
                case 5:
                    int deckIndex = menuContext.getScanner().nextInt();
                    menuContext.getClient().peekDeck(menuContext.getUsername(), deckIndex);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        }else{
            switch (choice){
                case 5:
                    menuContext.getClient().addComponentToBooked(menuContext.getUsername());
                    break;
                case 1:
                    menuContext.getClient().addComponentToViewed(menuContext.getUsername());
                    break;
                case 2:
                    int x = menuContext.getScanner().nextInt();
                    int y = menuContext.getScanner().nextInt();
                    menuContext.getClient().placeComponent(menuContext.getUsername(), new Pair<>(x, y));
                    break;
                case 3:
                    int numRotations = menuContext.getScanner().nextInt();
                    for(int i = 0; i < numRotations; i++){
                        menuContext.getClient().rotateComponentClockwise(menuContext.getUsername());
                    }
                    break;
                case 4:
                    int numRotations1 = menuContext.getScanner().nextInt();
                    for(int i = 0; i < numRotations1; i++){
                        menuContext.getClient().rotateComponentCounterclockwise(menuContext.getUsername());
                    }
                    break;
                case 6:
                    menuContext.getClient().turnHourglass(menuContext.getUsername());
                    break;
                case 7:
                    menuContext.getClient().peekDeck(menuContext.getUsername(), menuContext.getScanner().nextInt());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Building Ship Menu";
    }
}
