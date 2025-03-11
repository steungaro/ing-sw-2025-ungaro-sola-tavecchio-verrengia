package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.ship.Ship;

import java.io.*;
import java.util.*;


/**
 * @author GC20
 */
public class LightFire extends Projectile {

    /**
     * Default constructor
     */
    public LightFire() {
        super();
    }

    /**
     * @param s is the ship that is being attacked
     * @param diceResult is the result of the dice
     * @return the list of shields that can defeat the LightFire
     * @see Ship
     * @see Cannon
     * @apiNote The controller needs to verify whether the list contains shields and ask the user to activate them
     */
    public List<Shield> getShields(Ship s, int diceResult) {
        return s.getShield(direction);
    }
}