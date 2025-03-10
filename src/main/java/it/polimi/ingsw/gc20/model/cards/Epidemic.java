package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.Ship;

/**
 * @author GC20
 */
public class Epidemic extends AdventureCard {

    /**
     * Default constructor
     */
    public Epidemic() {
    }

    /**
     * @param p
     */
    public void Effect(Player p) {
        Ship s = p.getShip();
    }

}