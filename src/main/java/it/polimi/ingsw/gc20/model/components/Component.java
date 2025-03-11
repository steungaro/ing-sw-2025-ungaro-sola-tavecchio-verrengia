package it.polimi.ingsw.gc20.model.components;

import java.util.*;
import it.polimi.ingsw.gc20.model.ship.Tile;

public abstract class Component {

    protected Map<Direction, ConnectorEnum> connectors;
    protected Integer IDComponent;
    private Tile tile;

    public Component() {
        connectors = new HashMap<Direction, ConnectorEnum>();
        IDComponent = 0;
    }

    /**
    * This function rotates the component clockwise by 90 degrees
    * */
    public void rotateClockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, conn);
    }

    /**
    * This function rotates the component counterclockwise by 90 degrees
    * */
    public void rotateCounterclockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, conn);
    }

    /**
     * This function returns the ID of the component
     * @return the ID of the component
     */
    public Integer getIDComponent() {
        return IDComponent;
    }

    /**
     * This function sets the ID of the component
     * @param IDComponent the ID of the component
     */
    public void setIDComponent(Integer IDComponent) {
        this.IDComponent = IDComponent;
    }

    /**
     * This function returns the connectors of the component
     * @return the connectors of the component
     */
    public Map<Direction, ConnectorEnum> getConnectors() {
        return connectors;
    }

    /**
     * This function sets the connectors of the component
     * @param connectors the connectors of the component
     */
    public void setConnectors(Map<Direction, ConnectorEnum> connectors) {
        for(Direction dir : Direction.values()){
            this.connectors.put(dir, connectors.get(dir));
        }
    }

    /**
     * This function returns the tile of the component
     * @return the tile of the component
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * This function sets the tile of the component
     * @param tile the tile of the component
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Boolean isValid(Component c, Direction d){
        //TODO implement
        return true;
    }
}