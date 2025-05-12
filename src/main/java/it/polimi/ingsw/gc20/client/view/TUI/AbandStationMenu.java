package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;

public class AbandStationMenu implements MenuState {
    private final MenuContext menuContext;
    private final String stateName = "Abandon Station Menu";
    private List<CargoColor> cargos = null;

    public AbandStationMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    @Override
    public void displayMenu() {
        if(cargos == null) {
            System.out.println("\n=== Abandoned Station Menu ===");
            System.out.println("1. Confirm boarding station");
            System.out.println("2. Skip turn");
        }else{
            System.out.println("\n=== Abandoned Station Menu ===");
            System.out.println("Cargo holds in the ship:");
            System.out.println(menuContext.getShip().getCargo().toString());
            System.out.println("1. Load cargo from planet with the arguments: <CargoColor> <DestinationX> <DestinationY>");
            System.out.println("2. Unload cargo from ship with the arguments: <CargoColor> <FromX> <FromY>");
            System.out.println("3. Move cargo from a CargoHold to another with the arguments: <CargoColor> <FromX> <FromY> <ToX> <ToY>");
            System.out.println("4. End turn");
            }
        }

    @Override
    public boolean handleInput() throws RemoteException {
        int choice = menuContext.getIntInput(1, 2);
        if(cargos==null) {
            switch (choice) {
                case 1:
                    System.out.println("Abandoning station...");
                    menuContext.getClient().acceptCard(menuContext.getUsername());
                    return true;
                case 2:
                    menuContext.getClient().endMove(menuContext.getUsername());
                    return true;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        }else{
            switch(choice){
                case 1:
                    String cargoColor = menuContext.getScanner().next();
                    int x = menuContext.getScanner().nextInt();
                    int y = menuContext.getScanner().nextInt();
                    menuContext.getClient().loadCargo(menuContext.getUsername(), CargoColor.valueOf(cargoColor), new Pair<>(x, y));
                    break;
                case 2:
                    cargoColor = menuContext.getScanner().next();
                    x = menuContext.getScanner().nextInt();
                    y = menuContext.getScanner().nextInt();
                    menuContext.getClient().unloadCargo(menuContext.getUsername(), CargoColor.valueOf(cargoColor), new Pair<>(x, y));
                    break;
                case 3:
                    cargoColor = menuContext.getScanner().next();
                    int fromX = menuContext.getScanner().nextInt();
                    int fromY = menuContext.getScanner().nextInt();
                    int toX = menuContext.getScanner().nextInt();
                    int toY = menuContext.getScanner().nextInt();
                    menuContext.getClient().moveCargo(menuContext.getUsername(), CargoColor.valueOf(cargoColor),
                            new Pair<>(fromX, fromY), new Pair<>(toX, toY));
                    break;
                case 4:
                    menuContext.getClient().endMove(menuContext.getUsername());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        }
        return true;
    }

    @Override
    public String getStateName() {
        return stateName;
    }
}
