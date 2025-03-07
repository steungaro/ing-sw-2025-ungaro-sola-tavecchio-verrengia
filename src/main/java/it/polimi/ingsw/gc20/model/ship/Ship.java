package it.polimi.ingsw.gc20.model.ship;

import Components.Component;
import it.polimi.ingsw.gc20.model.components.Direction;

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

    private Integer totalEnergy;

    private List<Integer> cargo;


    /**
     * Function that determines the total firepower of the ship
     * it is the sum of single cannons power and double cannons power based on their orientation (cannons facing north have full power, others have half power)
     * the user will select one by one the cannons he wants to use (single cannons automatically selected) every time he selects a cannon the power of the ship will be recalculated
     * it also checks if the ship has the necessary amount of batteries
     * @param cannoni
     * @return power
     */
    public float firePower(Set<Component> cannoni) {
        if(cannoni.size()>totalEnergy)
            throw new IllegalArgumentException("cannon size too large");
        float power  = singleCannonsPower;
        for(Component cannon : cannoni){
            power += cannon.getPower();
        }
        return power;
    }

    /**Function that determines the total engine power of the ship that the user wants to activate
     * @param doubleEnginesActivated: the number of double engines the user wants to activate => the number of battery cells consumed
     * @return power_of_the_ship
     */
    public Integer enginePower(Integer doubleEnginesActivated) {
        return doubleEnginesActivated * 2 + singleEngines;
    }

    /**
     * @return
     */
    public Integer getTotalEnergy() {
        return totalEnergy;
    }

    /**
     * @return
     */
    public Integer crew() {

        return null;
    }

    /**
     * Function that gets the first component of the ship from a certain direction to determine what component will be hit
     *
     * @param Direction d: the direction from which the component will be hit
     * @param Integer n: row or column of the component ATTENTION it is the row or colum get from the dice NOT the row or column of the ship
     * @return component_hit
     */
    public Component getFirstComponent(Direction d, Integer n) {
        int rows, cols;
        if (this instanceof LearnerShip) {
            rows = 5;
            cols = 5;
        }else{
            rows = 7;
            cols = 5;
        }
        if ((d == Direction.UP || d == Direction.DOWN) && (n < 0 || n >= cols)) {
            return null;
        }
        if ((d == Direction.LEFT || d == Direction.RIGHT) && (n < 0 || n >= rows)) {
            return null;
        }

        switch (d)
        {
            case UP:
                for(int i = 0; i < rows; i++){
                    if(table[i][n].getComponent()){
                        return table[i][n].getComponent();
                    }
                }
            case DOWN:
                for(int i = rows-1; i >= 0; i--){
                    if(table[i][n].getComponent()){
                        return table[i][n].getComponent();
                    }
                }
            case LEFT:
                for(int i = 0; i < cols; i++){
                    if(table[n][i].getComponent()){
                        return table[n][i].getComponent();
                    }
                }
            case RIGHT:
                for(int i = cols-1; i >= 0; i--){
                    if(table[n][i].getComponent()){
                        return table[n][i].getComponent();
                    }
                }
        }
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