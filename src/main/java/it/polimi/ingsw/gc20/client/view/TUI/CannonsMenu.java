package it.polimi.ingsw.gc20.client.view.TUI;


import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;

public class CannonsMenu {
    private final MenuContext menuContext;
    private List<Pair<Integer, Integer>> cannons;
    private List<Pair<Integer, Integer>> batteries;

    public CannonsMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void display(){
        System.out.println("Available cannons:");
        System.out.println("1. End selecting cannons and their batteries");
        for (Pair<Integer, Integer> cannon : cannons) {
            System.out.println((cannons.indexOf(cannon) + 2) + ". Cannon coordinates: <" + cannon.getValue0() + "> <" + cannon.getValue1() + ">");
        }
        for( Pair<Integer, Integer> battery : batteries) {
            System.out.println("Batteries:");
            System.out.println((batteries.indexOf(battery) + cannons.size() + 2) + ". Battery coordinates: <" + battery.getValue0() + ">  <" + battery.getValue1() + ">");
        }
    }

    public void activateCannons() throws RemoteException {
        List<Pair<Integer, Integer>> activatedCannons = menuContext.getShip().getCannons();
        List<Pair<Integer, Integer>> activatedBatteries = menuContext.getShip().getBatteries();
        int choice = menuContext.getScanner().nextInt();
        if(choice == 1){
            menuContext.getClient().activateCannons(menuContext.getUsername(), activatedCannons, activatedBatteries);
        } else if (choice < cannons.size() + 2) {
            activatedCannons.add(cannons.get(choice - 2));
        } else if (choice < cannons.size() + batteries.size() + 2) {
            activatedBatteries.add(batteries.get(choice - cannons.size() - 2));
        }
    }


}
