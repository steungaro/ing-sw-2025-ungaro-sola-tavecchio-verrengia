package it.polimi.ingsw.gc20.model.Ship;

import Components.Component;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public abstract class Ship {

    /**
     * Default constructor
     */
    public Ship() {
    }

    /**
     * 
     */
    private Set<Component> waste;

    /**
     * 
     */
    private Integer doubleCannons;

    /**
     * 
     */
    private Integer doubleEngines;

    /**
     * 
     */
    private Integer doubleCannonsPower;

    /**
     * 
     */
    private Float singleCannonsPower;

    /**
     * @return
     */
    public Integer firePower() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Integer enginePower() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Integer batteriesAvailable() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Integer astronauts() {
        // TODO implement here
        return null;
    }

    /**
     * @param Boolean 
     * @param Integer 
     * @return
     */
    public void fire(void Boolean, void Integer) {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    public void Operation1() {
        // TODO implement here
    }

    /**
     * @param Direction d 
     * @param Integer n 
     * @return
     */
    public Component getFirstComponent(void Direction d, void Integer n) {
        // TODO implement here
        return null;
    }

    /**
     * @param Direction d 
     * @return
     */
    public Boolean getShield(void Direction d) {
        // TODO implement here
        return null;
    }

    /**
     * @param Direction d 
     * @param Integer n 
     * @return
     */
    public Boolean getCannon(void Direction d, void Integer n) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Integer getAllExposed() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void validateShip() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Set<Component> getWaste() {
        // TODO implement here
        return null;
    }

    /**
     * @param Component c 
     * @return
     */
    public void addToWaste(void Component c) {
        // TODO implement here
        return null;
    }

}