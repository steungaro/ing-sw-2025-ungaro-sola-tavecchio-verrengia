package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.ship.*;
import it.polimi.ingsw.gc20.model.components.Direction;

/**
 * @author GC20
 */
public abstract class Projectile {

    /**
     * Default constructor
     */
    public Projectile() {
    }

    /**
     * 
     */
    protected Direction direction;

    /**
     * @param s is the ship fired at
     * @param diceResult is the result of the dice
     * @implNote ships cannot resist a heavy fire
     * @throws Exception if the ship is invalid
     * @see Ship
     * @apiNote Controller must ask getCannon to know whether a cannon is active or not, then it must invoke Fire if and only if the given cannons(s) cannot protect the ship catch the exception and ask the player to validate the ship
     */
    public void Fire(Ship s, int diceResult) throws Exception {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            if (s instanceof NormalShip) {
                if(!s.killComponent(s.getFirstComponent(direction, diceResult - 4))){
                    throw new Exception("Invalid ship");
                }
            } else {
                if(!s.killComponent(s.getFirstComponent(direction, diceResult - 5))){
                    throw new Exception("Invalid ship");
                }
            }
        } else {
            if(!s.killComponent(s.getFirstComponent(direction, diceResult - 5))){
                throw new Exception("Invalid ship");
            }
        }
    }

}