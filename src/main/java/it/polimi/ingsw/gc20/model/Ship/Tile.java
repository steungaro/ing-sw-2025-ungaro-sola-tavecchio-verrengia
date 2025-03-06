package it.polimi.ingsw.gc20.model.Ship;

import Components.Component;

import java.io.*;
import java.util.*;

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
     * @return
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

}