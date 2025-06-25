package it.polimi.ingsw.gc20.server.model.components;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.model.ship.Tile;

/**
 * Abstract class representing a component in the system. This class serves as the base
 * for various types of components and provides common functionality for managing
 * connectors, rotation, and validation of connections.
 * <p>
 * The components can be of different types such as Battery, Cannon, Cabin, Engine,
 * CargoHold, Pipes, SpecialCargoHold, LifeSupport, and Shield. Each subclass
 * provides its specific implementation and behavior.
 */

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
})

public abstract class Component {

    protected final Map<Direction, ConnectorEnum> connectors = new HashMap<>();
    protected int ID;
    private Tile tile = null;
    protected Direction rotation; // Default rotation direction

    /**
     * Default constructor for the Component class.
     * Initializes the component with a default rotation set to {@code Direction.UP}.
     */
    public Component() {
        rotation = Direction.UP;
    }

    /**
     * Rotates the component 90 degrees clockwise.
     * <p>
     * This method updates the connector mappings for each direction by shifting the connectors
     * in the order of UP -> RIGHT -> DOWN -> LEFT -> UP. It also updates the current rotation
     * of the component to reflect the new orientation after the clockwise rotation.
     * <p>
     * The rotation transitions are as follows:
     * - UP becomes RIGHT
     * - RIGHT becomes DOWN
     * - DOWN becomes LEFT
     * - LEFT becomes UP
     */
    public void rotateClockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, conn);
        switch (rotation) {
            case UP:
                rotation = Direction.RIGHT;
                break;
            case RIGHT:
                rotation = Direction.DOWN;
                break;
            case DOWN:
                rotation = Direction.LEFT;
                break;
            case LEFT:
                rotation = Direction.UP;
                break;
        }
    }

    /**
     * Rotates the component 90 degrees counterclockwise.
     * <p>
     * This method updates the connector mappings for each direction by rotating the connectors
     * in the order of UP -> LEFT -> DOWN -> RIGHT -> UP. It also updates the current rotation
     * of the component to reflect the new orientation after the counterclockwise rotation.
     * <p>
     * The rotation transitions are as follows:
     * - UP becomes LEFT
     * - LEFT becomes DOWN
     * - DOWN becomes RIGHT
     * - RIGHT becomes UP
     */
    public void rotateCounterclockwise() {
        ConnectorEnum conn = connectors.get(Direction.UP);
        connectors.put(Direction.UP, connectors.get(Direction.RIGHT));
        connectors.put(Direction.RIGHT, connectors.get(Direction.DOWN));
        connectors.put(Direction.DOWN, connectors.get(Direction.LEFT));
        connectors.put(Direction.LEFT, conn);
        switch (rotation) {
            case UP:
                rotation = Direction.LEFT;
                break;
            case LEFT:
                rotation = Direction.DOWN;
                break;
            case DOWN:
                rotation = Direction.RIGHT;
                break;
            case RIGHT:
                rotation = Direction.UP;
                break;
        }
    }

    /**
     * Retrieves the ID of the component.
     *
     * @return the ID of the component
     */
    public int getIDComponent() {
        return ID;
    }

    /**
     * Sets the ID of the component.
     *
     * @param IDComponent the ID to set for the component
     */
    public void setIDComponent(int IDComponent) {
        this.ID = IDComponent;
    }

    /**
     * Retrieves the mapping of connectors associated with each direction for the component.
     * The connectors indicate the type of connection available in the specified direction.
     *
     * @return a map where the keys are directions (of type Direction) and the values are
     *         the corresponding connector types (of type ConnectorEnum) for those directions
     */
    public Map<Direction, ConnectorEnum> getConnectors() {
        return connectors;
    }

    /**
     * Sets the mapping of connectors for each direction of the component.
     * This method updates the existing connectors map with the provided mappings.
     *
     * @param connectors a map where the keys are directions (of type Direction)
     *                   and the values are the corresponding connector types
     *                   (of type ConnectorEnum) to be set for those directions
     */
    public void setConnectors(Map<Direction, ConnectorEnum> connectors) {
        for(Direction dir : Direction.values()){
            this.connectors.put(dir, connectors.get(dir));
        }
    }

    /**
     * Retrieves the tile associated with this component.
     *
     * @return the {@code Tile} object linked to this component
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile associated with this component.
     *
     * @param tile the {@code Tile} to be associated with this component
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }


    /**
     * Checks if the connection between the current component and the provided component is valid
     * in the specified direction.
     * <p>
     * A connection is considered valid if the connector type in the specified direction (d)
     * of the current component matches the connector type in the opposite direction on the
     * provided component. Additionally, the method also considers cases where the connector
     * type is {@code ConnectorEnum.U} or {@code ConnectorEnum.ZERO} based on specific conditions.
     *
     * @param c the component to check the connection with
     * @param d the direction to evaluate the connection in
     * @return {@code true} if the connection between the two components is valid,
     *         {@code false} otherwise
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

        if(this.connectors.get(d) == c.getConnectors().get(opposite)){
            return true;
        }
        if (this.connectors.get(d) == ConnectorEnum.U && c.getConnectors().get(opposite) != ConnectorEnum.ZERO) {
            return true;
        }
        return this.connectors.get(d) != ConnectorEnum.ZERO && c.getConnectors().get(opposite) == ConnectorEnum.U;
    }

    /**
     * Updates the parameters of the given ship based on the sign parameter.
     * This method is intended to be overridden by subclasses to implement, otherwise it does nothing.
     *
     * @param s the ship whose parameters are to be updated
     * @param sign an integer indicating the update operation to be performed
     */
    public void updateParameter(Ship s, int sign) {}

    /**
     * Retrieves the current rotation of the component.
     *
     * @return the current rotation of the component as a {@code Direction}
     */
    public Direction getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the component to the specified direction.
     *
     * @param rotation the new rotation to set for the component, represented as a {@code Direction}
     */
    public void setRotation(Direction rotation) {
        this.rotation = rotation;
    }

    /**
     * Checks if the component has a shield facing the specified direction.
     * This method is intended to be overridden by {@code Shield} class, otherwise it returns false.
     *
     * @param d the direction to check for a shield
     * @return {@code true} if the component has a shield in the specified direction, {@code false} otherwise
     */
    public boolean shieldIn (Direction d){
        return false; //default implementation
    }

    /**
     * Checks if the component has a valid orientation for the specified direction.
     * This method is intended to be overridden by {@code Engine} class to provide specific orientation validation,
     * otherwise it returns true.
     *
     * @param d the {@code Direction} to check for a valid orientation
     * @return {@code true} if the component has a valid orientation in the specified direction,
     *         {@code false} otherwise
     */
    public boolean hasValidOrientation (Direction d){
        return true; //default implementation
    }

    /**
     * Initializes the number of astronauts associated with the component.
     * This method is intended to be overridden by {@code Cabin} class to set the initial number of astronauts.
     *
     * @return the initial number of astronauts, defaulting to 0 if not overridden
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

    /**
     * Creates and returns a new instance of a {@code ViewComponent} associated with the component.
     * This method is intended to be implemented by subclasses to define the
     * specific view representation of the component.
     *
     * @return a {@code ViewComponent} object representing the visual depiction of the component
     */
    public abstract ViewComponent createViewComponent();

    /**
     * Initializes the provided {@code ViewComponent} with the attributes and connector values
     * from this component. This method updates the {@code id}, {@code rotComp}, and the connector
     * values for all four directions (up, down, left, right) in the {@code ViewComponent}.
     *
     * @param viewComponent the {@code ViewComponent} to initialize with the current component's attributes
     */
    public void initializeViewComponent(ViewComponent viewComponent) {
        viewComponent.id = ID;
        viewComponent.rotComp = rotation.getValue();
        viewComponent.upConnectors = connectors.get(Direction.UP).getValue();
        viewComponent.downConnectors = connectors.get(Direction.DOWN).getValue();
        viewComponent.leftConnectors = connectors.get(Direction.LEFT).getValue();
        viewComponent.rightConnectors = connectors.get(Direction.RIGHT).getValue();
    }

    /**
     * Determines whether the component is an engine.
     *
     * @return {@code true} if the component is an engine, {@code false} otherwise
     */
    public boolean isEngine() { return false;}
}