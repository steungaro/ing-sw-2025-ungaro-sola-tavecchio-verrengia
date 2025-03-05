package it.polimi.ingsw.gc20.model.Components;

import java.io.*;
import java.util.*;

public class Engine extends Component {

    public Engine(Integer ID, Integer power, Map<Direction, ConnectorEnum> conn) {
        this.setIDComponent(ID);
        this.power = power;
        this.setConnectors(new HashMap<Direction, ConnectorEnum>());
        this.getConnectors().put(Direction.UP, conn.get(Direction.UP));
        this.getConnectors().put(Direction.LEFT, conn.get(Direction.LEFT));
        this.getConnectors().put(Direction.DOWN, conn.get(Direction.DOWN));
        this.getConnectors().put(Direction.RIGHT, conn.get(Direction.RIGHT));
    }

    private Integer power;

    public Integer getPower() {
        return power;
    }
}