package it.polimi.ingsw.gc20.client.view.TUI;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.List;

public class AbandShipMenu implements MenuState{
    private final String stateName = "Abandon Ship Menu";

    private final MenuContext menuContext;
    private boolean shipBoarded = false;
    private List<Pair<Integer, Integer>> listAstronauts = new ArrayList<>();

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
            List<Triplet<Integer, Integer, Integer>> crew = menuContext.getShip().getCrew();
            int index = 0;
            for (Triplet<Integer, Integer, Integer> astronaut : crew) {
                System.out.println("1. To end the turn");
                System.out.println((index + 2) + ". Cabin in coordinates: <" + astronaut.getValue0() + "> <"
                        + astronaut.getValue1() +
                        "> Number of people inside: " + astronaut.getValue2());
                index++;
            }
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
                    menuContext.getClient().acceptCard(menuContext.getUsername());
                    break;
                case 2:
                    menuContext.getClient().endMove(menuContext.getUsername());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
            return true;
        }else{
            int choice = menuContext.getScanner().nextInt();
            List<Triplet<Integer, Integer, Integer>> crew = menuContext.getShip().getCrew();
            if (choice == 1) {
                menuContext.getClient().loseCrew(menuContext.getUsername(), listAstronauts);
                menuContext.getClient().endMove(menuContext.getUsername());
            } else {
                if (choice >= 2 && choice <= crew.size() + 1) {
                    Triplet<Integer, Integer, Integer> selected = crew.get(choice - 2);
                    listAstronauts.add(new Pair<>(selected.getValue0(), selected.getValue1()));
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName() {
        return stateName;
    }
}
