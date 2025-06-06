package it.polimi.ingsw.gc20.server.model.components;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.model.ship.Tile;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Battery.class, name = "Battery"),
        @JsonSubTypes.Type(value = Cannon.class, name = "Cannon"),
        @JsonSubTypes.Type(value = Cabin.class, name = "Cabin"),
        @JsonSubTypes.Type(value = Engine.class, name = "Engine"),
        @JsonSubTypes.Type(value = CargoHold.class, name = "CargoHold"),
        @JsonSubTypes.Type(value = Pipes.class, name = "Pipes"),
        @JsonSubTypes.Type(value = SpecialCargoHold.class, name = "SpecialCargoHold"),
        @JsonSubTypes.Type(value = LifeSupport.class, name = "LifeSupport"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield")
        // Add other component types as needed
})

public abstract class Component {

    protected final Map<Direction, ConnectorEnum> connectors = new HashMap<>();
    protected int ID;
    private Tile tile = null;

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
        return ID;
    }

    /**
     * This function sets the ID of the component
     * @param IDComponent the ID of the component
     */
    public void setIDComponent(int IDComponent) {
        this.ID = IDComponent;
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

        if (this.connectors.get(d) == ConnectorEnum.ZERO && c.connectors.get(opposite) != ConnectorEnum.ZERO) {
            return false;
        }
        if (this.connectors.get(d) != ConnectorEnum.ZERO && c.connectors.get(opposite) == ConnectorEnum.ZERO) {
            return false;
        }
        if(this.connectors.get(d) == c.connectors.get(opposite)){
            return true;
        }
        if(this.connectors.get(d) == ConnectorEnum.U && c.connectors.get(opposite) != ConnectorEnum.ZERO){
            return true;
        }
        return c.connectors.get(opposite) == ConnectorEnum.U && this.connectors.get(d) != ConnectorEnum.ZERO;
    }

    /**
     * Function to update the parameter of the ship
     * @param s ship that is updating his parameter
     * @param sign integer that indicate if the parameter is increasing or decreasing
     */
    public void updateParameter(Ship s, int sign) {}


    /** Function that returns true if the component is a shield and cover the direction d
     *
     * @param d direction of the shield
     * @return true if the component is a shield and cover the direction d, false otherwise
     */
    public boolean shieldIn (Direction d){
        return false; //default implementation
    }

    /** Function that returns true if the component has a valid orientation
     *
     * @param d direction of the component
     * @return true if the component has a valid orientation, false otherwise
     */
    public boolean hasValidOrientation (Direction d){
        return true; //default implementation
    }

    /**
     * Function that returns the number of astronauts that can be initialized in the component
     * @return the number of astronauts that can be initialized in the component
     */
    public int initializeAstronauts (){
        return 0; //default implementation
    }

    /**
     * Function that returns true if the component is a cabin
     * @return true if the component is a cabin, false otherwise
     */
    public boolean isCabin(){
        return false; //default implementation
    }

    /** Function that returns true if the component is a life support
     *
     * @return true if the component is a life support, false otherwise
     */
    public boolean isLifeSupport(){
        return false; //default implementation
    }

    /**
     * Function that returns true if the component is a cannon
     * @return true if the component is a cannon, false otherwise
     *
     */
    public boolean isCannon() {
        return false; //default implementation
    }

    public abstract ViewComponent createViewComponent();

    public void initializeViewComponent(ViewComponent viewComponent) {
        viewComponent.id = ID;
        viewComponent.upConnectors = connectors.get(Direction.UP).getValue();
        viewComponent.downConnectors = connectors.get(Direction.DOWN).getValue();
        viewComponent.leftConnectors = connectors.get(Direction.LEFT).getValue();
        viewComponent.rightConnectors = connectors.get(Direction.RIGHT).getValue();
    }

    public boolean isEngine() { return false;}
}