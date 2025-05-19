package it.polimi.ingsw.gc20.client.view.TUI;

import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ShieldsMenu implements MenuState {
    private final MenuContext menuContext;
    private final String stateName = "Open Space Menu";
    private List<Pair<Integer, Integer>> shields = new ArrayList<>();
    private List<Pair<Integer, Integer>> batteries = new ArrayList<>();

    public ShieldsMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("Activate your engines");
        System.out.println(menuContext.getShip().toString());
        shields = menuContext.getShip().getShields();
        for (Pair<Integer, Integer> shield : shields) {
            System.out.println("1. End Turn, don't activate any shield");
            System.out.println((shields.indexOf(shield) + 2) + ". shields coordinates: <" + shield.getValue0() + "> <" + shield.getValue1() + ">");
        }
        batteries = menuContext.getShip().getBatteries();
        for (Pair<Integer, Integer> battery : batteries) {
            System.out.println("Batteries:");
            System.out.println((shields.indexOf(battery) + shields.size() + 2) + ". Battery coordinates: <" + battery.getValue0() + "> <" + battery.getValue1() + ">");
        }
        System.out.println("Choose the battery you want to use by entering its number");
    }

    public boolean handleInput() throws RemoteException {
        int choice1 = menuContext.getScanner().nextInt();
        int choice2 = menuContext.getScanner().nextInt();
        if(choice1 == 1){
            menuContext.getClient().activateShield(menuContext.getUsername(), null, null);
        }else if(choice1 < shields.size() + 1 && choice2 < shields.size() + batteries.size() + 1) {
            menuContext.getClient().activateShield(menuContext.getUsername(), shields.get(choice1), batteries.get(choice2));
        } else if (choice2 < shields.size() + 1 && choice1 < shields.size() + batteries.size() + 1) {
            menuContext.getClient().activateShield(menuContext.getUsername(), shields.get(choice2), batteries.get(choice1));
        }
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
