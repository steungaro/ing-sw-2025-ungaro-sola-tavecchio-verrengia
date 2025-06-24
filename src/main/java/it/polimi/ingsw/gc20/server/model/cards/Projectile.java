package it.polimi.ingsw.gc20.server.model.cards;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.server.model.components.Direction;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.io.Serializable;

/**
 * The Projectile class represents a projectile in the game that can be fired at a target ship.
 * It includes attributes for the direction of the projectile and the type of fire it corresponds to.
 * The class provides methods for setting and retrieving these attributes, as well as a method to handle firing at a ship.
 */
public class Projectile implements Serializable {
    private Direction direction;
    private FireType fireType;

    /**
     * Default constructor for the Projectile class.
     * Initializes a new instance of the Projectile with default settings for its attributes.
     */
    public Projectile() {
    }


    /**
     * Sets the direction of the projectile.
     *
     * @param direction the direction to be set for the projectile, represented as an instance of the Direction enum
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Sets the fire type for the projectile.
     *
     * @param fireType the type of fire to be set for the projectile,
     *                 represented as an instance of the FireType enum
     */
    public void setFireType(FireType fireType) {
        this.fireType = fireType;
    }

    /**
     * Retrieves the direction of the projectile.
     *
     * @return the direction of the projectile, represented as an instance of the Direction enum
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Retrieves the fire type associated with the projectile.
     *
     * @return the FireType of the projectile, represented as an instance of the FireType enum
     */
    public FireType getFireType() {
        return fireType;
    }

    /**
     * Fires the projectile at a specified ship, attempting to target and destroy a specific component
     * of the ship, based on its type, direction, and dice result. Throws an exception
     * if the target component cannot be successfully destroyed or if the ship is invalid for the operation.
     *
     * @param s the target ship at which the projectile is fired, represented as an instance of the Ship class
     * @param diceResult the result of the dice roll used to calculate the target component
     * @throws InvalidShipException if the ship is invalid after the firing operation
     */
    public void Fire(Ship s, int diceResult) throws InvalidShipException {
        try {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (s.isNormal()) {
                    if (!s.killComponent(s.getFirstComponent(direction, diceResult - 4))) {
                        throw new InvalidShipException("Invalid ship");
                    }
                } else {
                    if (!s.killComponent(s.getFirstComponent(direction, diceResult - 5))) {
                        throw new InvalidShipException("Invalid ship");
                    }
                }
            } else {
                if (!s.killComponent(s.getFirstComponent(direction, diceResult - 5))) {
                    throw new InvalidShipException("Invalid ship");
                }
            }
        } catch (ComponentNotFoundException _) {
            //this should never happen because the controller checks this before firing
        }
    }

}