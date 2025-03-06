package it.polimi.ingsw.gc20.model.components;

import java.util.*;

public abstract class Component {

    public Component() {
    }

    private Map<Direction, ConnectorEnum> connectors;

    private Integer IDComponent;

    private Integer orientation;

    /*
    * This function rotates the component clockwise by 90 degrees
    * */
    public void rotateClockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, conn);
    }

    /*
    * This function rotates the component counterclockwise by 90 degrees
    * */
    public void rotateCounterclockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, conn);
    }

    public Integer getIDComponent() {
        return IDComponent;
    }

    public void setIDComponent(Integer IDComponent) {
        this.IDComponent = IDComponent;
    }

    public Map<Direction, ConnectorEnum> getConnectors() {
        return connectors;
    }

    public void setConnectors(Map<Direction, ConnectorEnum> connectors) {
        for(Direction dir : Direction.values()){
            this.connectors.put(dir, connectors.get(dir));
        }
    }
}