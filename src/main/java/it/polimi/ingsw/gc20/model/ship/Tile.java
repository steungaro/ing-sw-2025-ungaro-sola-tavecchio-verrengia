package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import java.lang.*;
import java.security.*;

/**
 * @author GC20
 */
public class Tile {

    /**
     * 
     */
    private Boolean availability;

    /**
     * 
     */
    private Component component;

    /**
     * Default constructor
     */
    public Tile() {
        availability = true;
        component = null;
    }

    /**
     * @return component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @param c Component
     */
    public void addComponent(Component c) throws IllegalArgumentException {
        if(availability) {
            this.component = c;
            c.setTile(this);
            this.availability = false;
        } else {
            throw new IllegalArgumentException("Tile is not available");
        }
    }

    /**
     * @return availability
     */
    public Boolean getAvailability() {

        return availability;
    }

    /**
     * @param a Boolean
     */
    public void setAvailability(Boolean a) {
        availability = a;
    }

    /**
     * @throws RuntimeException Component does not exist
     */
    public void removeComponent(){
        if(component != null){
            component = null;
            availability = true;
        }else{
            throw new InvalidParameterException("Component does not exist");
        }
    }

}