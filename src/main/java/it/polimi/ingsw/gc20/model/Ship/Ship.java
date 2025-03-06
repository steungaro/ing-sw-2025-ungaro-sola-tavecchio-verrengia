package it.polimi.ingsw.gc20.model.Ship;

import Components.Component;
import it.polimi.ingsw.gc20.model.Components.Component;

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

    private Integer singleEngines;
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

    private Integer batteries;

    /**
     * Function that determines the total fire power of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * @param cannoni
     * @return
     */
    public float firePower(Set<Component> cannoni) {
        float power  = singleCannonsPower;
        for(Component cannon : cannoni){
            power += cannon.getPower();
        }
        return power;
    }

    /**Function that determines the total engine power of the ship that the user wants to activate
     * @param doubleEnginesActivated: the number of double engines the user wants to activate => the number of battery cells consumed
     * @return
     */
    public Integer enginePower(Integer doubleEnginesActivated) {
        return doubleEnginesActivated * 2 + singleEngines;
    }

    /**
     * @return
     */
    public Integer getBatteriesAvailable() {
        return batteries;
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