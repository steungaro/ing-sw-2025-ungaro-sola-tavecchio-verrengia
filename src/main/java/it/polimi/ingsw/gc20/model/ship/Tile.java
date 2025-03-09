package it.polimi.ingsw.gc20.model.ship;

import Components.Component;

/**
 * @author GC20
 */
public class Tile {

    /**
     * Default constructor
     */
    public Tile() {
    }

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
    public void killComponent() {
    }

    /**
     * @return commponent
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @param Component c 
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
     * @param Boolean a 
     * @return
     */
    public void setAvailability( Boolean a) {
        availability = a;
    }

    public void removeCommpoent(){
        this.component = null;
        this.availability = true;
    }

}