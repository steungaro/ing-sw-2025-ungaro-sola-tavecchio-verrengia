package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.ship.*;
import it.polimi.ingsw.gc20.model.components.*;

import java.util.*;

/**
 * @author GC20
 */
public class HeavyMeteor extends Projectile {

    /**
     * Default constructor
     */
    public HeavyMeteor() {
    }

    public List<Cannon> getCannons(Ship s, int diceResult) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            if (s instanceof NormalShip) {
                s.getCannon(direction, diceResult - 4);
            } else {
                s.getCannon(direction, diceResult - 5);
            }
        } else {
            s.getCannon(direction, diceResult - 5);
        }
    }
}