package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.server.model.components.Component;

/**
 * The Tile class represents a single unit or space in a grid or board-like structure.
 * It maintains the ability to hold a single component and manages the state of its availability.
 */
public class Tile {

    private boolean availability;
    private Component component;

    /**
     * Default constructor
     */
    public Tile() {
        availability = true;
        component = null;
    }

    /**
     * Retrieves the component currently associated with this tile.
     *
     * @return the component associated with this tile, or null if no component is present
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Adds a component to the tile if it is available. The component's tile is set
     * to this tile. If the tile is not available, an exception is thrown.
     *
     * @param c the component to be added to the tile
     * @throws InvalidTileException if the tile is not available for a new component
     */
    public void addComponent(Component c) throws InvalidTileException {
        if(availability) {
            this.component = c;
            c.setTile(this);
            this.availability = false;
        } else {
            throw new InvalidTileException("Tile is not available");
        }
    }

    /**
     * Retrieves the availability status of the tile.
     *
     * @return true if the tile is available for a new component, false otherwise
     */
    public boolean getAvailability() {
        return availability;
    }

    /**
     * Sets the availability status of the tile.
     *
     * @param a the new availability status of the tile, where true indicates the tile is available 
     *          for a new component and false indicates it is not
     */
    public void setAvailability(boolean a) {
        availability = a;
    }

    /**
     * Removes the component currently associated with this tile.
     * If a component is present, its associated tile is set to null, the component is removed
     * from the tile, and the tile's availability is set to true.
     * If no component is present, an exception is thrown.
     *
     * @throws InvalidTileException if no component is currently associated with this tile
     */
    public void removeComponent() throws InvalidTileException {
        if(component != null){
            component.setTile(null);
            component = null;
            availability = true;
        }else{
            throw new InvalidTileException("Component does not exist");
        }
    }

}