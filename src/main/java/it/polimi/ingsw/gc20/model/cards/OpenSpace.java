package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;

/**
 * @author GC20
 */
public class OpenSpace extends AdventureCard {

    /**
     * Default constructor
     */
    public OpenSpace() {
    }

    /**
     * @param p player
     * @param g game
     * @param power engine power
     * @implNote moves the player of the declared firepower
     */
    public void Effect(Player p, Game g, int power) {
        g.move(p, power);
    }
}