package it.polimi.ingsw.gc20.model.Components;
import it.polimi.ingsw.gc20.model.Gamesets.Cargo;

import java.io.*;
import java.util.*;

public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold(Integer ID, Integer space, Map<Direction, ConnectorEnum> conn) {
        this.setIDComponent(ID);
        this.setSpace(space);
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, conn.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, conn.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, conn.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, conn.get(Direction.RIGHT));
        this.setCargoHeld(new ArrayList<Cargo>());
    }

    public void loadCargo(Cargo g) {
        this.getCargoHeld().add(g);
    }
}