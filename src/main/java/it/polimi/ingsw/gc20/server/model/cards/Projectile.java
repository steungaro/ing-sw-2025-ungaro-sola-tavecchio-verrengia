package it.polimi.ingsw.gc20.server.model.cards;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.server.model.components.Direction;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.io.Serializable;

/**
 * @author GC20
 */
public class Projectile implements Serializable {
    private Direction direction;
    private FireType fireType;

    /**
     * Default constructor
     */
    public Projectile() {
    }


    /**
     * @param direction is the direction of the projectile
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @param fireType type of the projectile
     */
    public void setFireType(FireType fireType) {
        this.fireType = fireType;
    }

    /**
     * @return the direction of the projectile
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return the type of the projectile
     */
    public FireType getFireType() {
        return fireType;
    }

    /**
     * @param s ship fired at
     * @param diceResult result of the dice
     * @implNote ships cannot resist a heavy fire
     * @throws InvalidShipException if the ship is invalid
     * @see Ship
     * @apiNote Controller must ask getCannons (for HeavyMeteor) to know whether a cannon is active or not, then it must invoke Fire if and only if the given cannons(s) cannot protect the ship catch the exception and ask the player to validate the ship
     * @apiNote Controller must ask getShields and getFirstComponent (for LightMeteor) to know whether a shield is active or not or if the component has connectors exposed, then it must invoke Fire if and only if the given shields(s) cannot protect the ship catch the exception and ask the player to validate the ship
     * @apiNote Controller must activate Fire without any checks (for HeavyFire)
     * @apiNote Controller must ask getShields (for LightFire) to know whether a shield is active or not, then it must invoke Fire if and only if the given shield(s) cannot protect the ship, catch the exception and ask the player to validate the ship
     */
    public void Fire(Ship s, int diceResult) throws InvalidShipException {
        try {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (s instanceof NormalShip) {
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
            //this should never happen
        }
    }

}