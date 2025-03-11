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
     * @return
     */
    /**
     * Default constructor
     */
    public Tile() {
        availability = true;
        component = null;
    }

    public void killComponent() {
    }

    /**
     * @return commponent
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @param c Component
     * @return
     */
    public void addComponent(Component c) {
        if(availability) {
            this.component = c;
        }
    }

    /**
     * @return
     */
    public Boolean getAvailability() {

        return availability;
    }

    /**
     * @param a Boolean
     * @return
     */
    public void setAvailability(Boolean a) {
        availability = a;
    }

    /**
     * @return
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