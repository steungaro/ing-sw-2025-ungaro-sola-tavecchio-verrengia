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
        super();
    }

    /**
     * @param s is the ship that is being attacked
     * @param diceResult is the result of the dice
     * @return the list of cannons that can defeat the HeavyMeteor
     * @see Ship
     * @see Cannon
     * @apiNote The controller needs to verify whether the list contains single cannons or double cannons and ask the user to activate them
     * @implNote HeavyMeteor from above can be defeated by a cannon in the same coloumn, HeavyMeteor from the other directions can be defeated by a cannon in the same row/column or in the previous/next one
     */
    public List<Cannon> getCannons(Ship s, int diceResult) {
        List<Cannon> cannons;
        if (direction == Direction.UP) {
            if (s instanceof NormalShip) {
                return s.getCannons(direction, diceResult - 4);
            } else {
                return s.getCannons(direction, diceResult - 5);
            }
        } else if (direction == Direction.DOWN) {
            if (s instanceof NormalShip) {
                cannons = s.getCannons(direction, diceResult - 4);
                cannons.addAll(s.getCannons(direction, diceResult - 5));
                cannons.addAll(s.getCannons(direction, diceResult - 3));
                return cannons;
            } else {
                cannons = s.getCannons(direction, diceResult - 5);
                cannons.addAll(s.getCannons(direction, diceResult - 6));
                cannons.addAll(s.getCannons(direction, diceResult - 4));
                return cannons;
            }
        } else {
            cannons = s.getCannons(direction, diceResult - 5);
            cannons.addAll(s.getCannons(direction, diceResult - 6));
            cannons.addAll(s.getCannons(direction, diceResult - 4));
            return cannons;
        }
    }
}