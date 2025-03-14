package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.ship.Ship;
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
     * @return the list of shields that can defeat the LightFire
     * @see Ship
     * @see Cannon
     * @apiNote The controller needs to verify whether the list contains shields and verify the shield received by the player, then manage the attack through the Fire method
     */
    public List<Shield> getShields(Ship s) {
        return s.getShield(direction);
    }
}