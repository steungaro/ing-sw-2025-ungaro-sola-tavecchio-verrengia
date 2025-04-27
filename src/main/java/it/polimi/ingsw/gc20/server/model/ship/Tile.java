package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.server.model.components.Component;

import java.security.*;

/**
 * @author GC20
 */
public class Tile {

    private Boolean availability;
    private Component component;

    /**
     * Default constructor
     */
    public Tile() {
        availability = true;
        component = null;
    }

    /**
     * Getter for the component
     * @return component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Add a component to the tile
     * @param c Component
     * @throws InvalidTileException if the tile is not available
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
     * Getter for the availability
     * @return availability
     */
    public Boolean getAvailability() {

        return availability;
    }

    /**
     * Setter for the availability
     * @param a Boolean
     */
    public void setAvailability(Boolean a) {
        availability = a;
    }

    /**
     * Removes the component from the tile
     * @throws InvalidParameterException Component does not exist
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