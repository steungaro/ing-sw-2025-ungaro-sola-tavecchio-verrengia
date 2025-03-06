package it.polimi.ingsw.gc20.model.Components;
import it.polimi.ingsw.gc20.model.Gamesets.Cargo;


import java.io.*;
import java.util.*;

public class CargoHold extends Component {

    private List<Cargo> cargoHeld;

    private Integer space;

    public CargoHold() {
    }

    public CargoHold(Integer ID, Integer space, Map<Direction, ConnectorEnum> conn) {
        this.setIDComponent(ID);
        this.space = space;
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, conn.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, conn.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, conn.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, conn.get(Direction.RIGHT));
        this.cargoHeld = new ArrayList<Cargo>();
    }

    public List<Cargo> getCargoHeld() {
        return cargoHeld;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public void setCargoHeld(List<Cargo> cargoHeld) {
        this.cargoHeld = cargoHeld;
    }

    public void loadCargo(Cargo g) {
        if(g!=Cargo.RED) //Note: RED is a special cargo that can't be added
            cargoHeld.add(g);
    }

    public void unloadCargo(Integer i) {
        cargoHeld.remove(i);
        //Scroll the list to the left
        for (int j = i; j < cargoHeld.size(); j++) {
            cargoHeld.set(j, cargoHeld.get(j + 1));
        }
    }
}