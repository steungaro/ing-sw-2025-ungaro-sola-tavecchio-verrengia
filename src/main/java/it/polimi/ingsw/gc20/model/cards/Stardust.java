package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.ship.Ship;

/**
 * @author GC20
 */
public class Stardust extends AdventureCard {

    /**
     * Default constructor
     */
    public Stardust() {
        super();
    }

    /**
     * @param p player affected by the card
     * @param g game
     * @implNote Each player loses a fly day for each exposed connector in their ship
     * @see Ship
     */
    public void Effect(Player p, Game g) {
        g.move(p, -p.getShip().getAllExposed());
        playCard();
    }
}