package it.polimi.ingsw.gc20.model.components;

import java.util.*;
import it.polimi.ingsw.gc20.model.ship.Tile;

public abstract class Component {

    protected Map<Direction, ConnectorEnum> connectors = new HashMap<>();
    protected int IDComponent;
    private Tile tile;

    public Component() {}

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
    public int getIDComponent() {
        return IDComponent;
    }

    /**
     * This function sets the ID of the component
     * @param IDComponent the ID of the component
     */
    public void setIDComponent(int IDComponent) {
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


    /**
     * This function checks if the component is linked correctly to the other component
     * @param c the component to check
     * @param d the direction of the component
     * @return true if the link is correct, false otherwise
     */
    public Boolean isValid(Component c, Direction d){
        Direction opposite;
        if(d==Direction.UP){
            opposite = Direction.DOWN;
        } else if(d==Direction.DOWN){
            opposite = Direction.UP;
        } else if(d==Direction.LEFT){
            opposite = Direction.RIGHT;
        } else {
            opposite = Direction.LEFT;
        }

        if(this.connectors.get(d) == c.connectors.get(opposite) && this.connectors.get(d) != ConnectorEnum.ZERO){
            return true;
        }
        if(this.connectors.get(d) == ConnectorEnum.U && c.connectors.get(opposite) != ConnectorEnum.ZERO){
            return true;
        }
        return c.connectors.get(opposite) == ConnectorEnum.U && this.connectors.get(opposite) != ConnectorEnum.ZERO;
    }
}