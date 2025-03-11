package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.ship.*;
import it.polimi.ingsw.gc20.model.components.*;

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
     * @param direction is the direction of the projectile
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @param s is the ship fired at
     * @param diceResult is the result of the dice
     * @implNote ships cannot resist a heavy fire
     * @throws Exception if the ship is invalid
     * @see Ship
     * @apiNote Controller must ask getCannons (for HevayMeteor) to know whether a cannon is active or not, then it must invoke Fire if and only if the given cannons(s) cannot protect the ship catch the exception and ask the player to validate the ship
     * @apiNote Controller must ask getShields and getFirstComponent (for LightMeteor) to know whether a shield is active or not or if the component has connectors exposed, then it must invoke Fire if and only if the given shields(s) cannot protect the ship catch the exception and ask the player to validate the ship
     * @apiNote Controller must activate Fire without any checks (for HeavyFire)
     * @apiNote Controller must ask getShields (for LightFire) to know whether a shield is active or not, then it must invoke Fire if and only if the given shield(s) cannot protect the ship catch the exception and ask the player to validate the ship
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