package it.polimi.ingsw.gc20.client.view.TUI;

import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EngineMenu implements MenuState {
    private final MenuContext menuContext;
    private final String stateName = "Open Space Menu";
    private List<Pair<Integer, Integer>> shields = new ArrayList<>();
    private List<Pair<Integer, Integer>> batteries = new ArrayList<>();

    public EngineMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("Activate your engines");
        System.out.println(menuContext.getShip().toString());
        shields = menuContext.getShip().getEngines();
        for (Pair<Integer, Integer> shield : shields) {
            System.out.println("1. End Turn, stop activating shields");
            System.out.println((shields.indexOf(shield) + 2) + ". shields coordinates: <" + shield.getValue0() + "> <" + shield.getValue1() + ">");
        }
        batteries = menuContext.getShip().getShields();
        for (Pair<Integer, Integer> battery : batteries) {
            System.out.println("Batteries:");
            System.out.println((shields.indexOf(battery) + shields.size() + 2) + ". Battery coordinates: <" + battery.getValue0() + "> <" + battery.getValue1() + ">");
        }
        System.out.println("Choose the battery you want to use by entering its number");
    }

    public boolean handleInput() throws RemoteException {
        ArrayList<Pair<Integer, Integer>> activatedShields = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> activatedBatteries = new ArrayList<>();
        int choice = menuContext.getScanner().nextInt();
        if(choice == 1){
            menuContext.getClient().activateShield(menuContext.getUsername(), activatedShields, activatedBatteries);
        }
        if(choice < shields.size() + 1) {
            activatedShields.add(shields.get(choice - 2));
        } else if (choice < shields.size() + batteries.size() + 1) {
            activatedBatteries.add(batteries.get(choice - shields.size() - 2));
        } else {
            System.out.println("Invalid choice. Please try again.");
            return false;

        }
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
