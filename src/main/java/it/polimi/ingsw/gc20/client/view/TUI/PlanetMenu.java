package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;


public class PlanetMenu implements MenuState{
    private final MenuContext menuContext;
    private List<Integer> cargo;

    public PlanetMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        if(cargo == null) {
            System.out.println("Available planets:");
            System.out.println("1. To land on a planet argument is: <planetIndex>");
            System.out.println("2. To skip the turn");
        }else{
            System.out.println("1. Load cargo from planet with the arguments: <CargoColor> <DestinationX> <DestinationY>");
            System.out.println("2. Unload cargo from ship with the arguments: <CargoColor> <FromX> <FromY>");
            System.out.println("3. Move cargo from a CargoHold to another with the arguments: <CargoColor> <FromX> <FromY> <ToX> <ToY>");
            System.out.println("4. To end the turn");
        }
    }

    public boolean handleInput() throws RemoteException {
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the planet menu
        if(cargo == null) {
            switch (choice) {
                case 1:
                    int planetIndex = menuContext.getScanner().nextInt();
                    menuContext.getClient().landOnPlanet(menuContext.getUsername(), planetIndex);
                    break;
                case 2:
                    menuContext.getClient().endMove(menuContext.getUsername());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }else{
            switch (choice) {
                case 1:
                    String cargoColor = menuContext.getScanner().next();
                    int x = menuContext.getScanner().nextInt();
                    int y = menuContext.getScanner().nextInt();
                    //menuContext.getClient().loadCargo(menuContext.getUsername(), CargoColor, new Pair<>(x, y)); //TODO cargo color argument?
                    break;
                case 2:
                    String cargoColor1 = menuContext.getScanner().next();
                    int fromX = menuContext.getScanner().nextInt();
                    int fromY = menuContext.getScanner().nextInt();
                    //menuContext.getClient().unloadCargo(menuContext.getUsername(), CargoColor, new Pair<>(fromX, fromY)); //TODO cargo color argument?
                    break;
                case 3:
                    String cargoColor2 = menuContext.getScanner().next();
                    int fromX1 = menuContext.getScanner().nextInt();
                    int fromY1 = menuContext.getScanner().nextInt();
                    int toX = menuContext.getScanner().nextInt();
                    int toY = menuContext.getScanner().nextInt();
                    //menuContext.getClient().moveCargo(menuContext.getUsername(), CargoColor, new Pair<>(fromX1, fromY1), new Pair<>(toX, toY)); //TODO cargo color argument?
                    break;
                case 4:
                    menuContext.getClient().endMove(menuContext.getUsername());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        return false;
    }

    public String getStateName() {
        return "Planet Menu";
    }
}
