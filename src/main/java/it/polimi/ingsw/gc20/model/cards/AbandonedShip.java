package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;

/**
 * @author GC20
 */
public class AbandonedShip extends AdventureCard {

    private int lostCrew;
    private int credits;
    private int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShip() {
        super();
        lostCrew = 0;
        credits = 0;
        lostDays = 0;
    }

    /**
     * Setter method for lostCrew
     * @param lostCrew lost crew
     */

    public void setLostCrew(int lostCrew) {
        this.lostCrew = lostCrew;
    }

    /**
     * Getter method for lostCrew
     * @return lostCrew
     */

    public int getLostCrew() {
        return lostCrew;
    }

    /**
     * Setter method for credits
     * @param credits credits
     */

    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Getter method for credits
     * @return credits
     */

    public int getCredits() {
        return credits;
    }

    /**
     * Setter method for lostDays
     * @param lostDays lostDays
     */

    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Getter method for lostDays
     * @return lostDays
     */

    public int getLostDays() {
        return lostDays;
    }

    /**
     * Applies card effect on player p, applying effect means the player accepted the card
     * @param p player that accepted the card
     * @param g game
     * @param l list of cabins to remove crew from
     * @implNote player p loses lostCrew crew members, gains credits and loses lostDays days
     */
    public void Effect(Player p, Game g, List<Cabin> l) {
        if (lostCrew > p.getShip().crew() || lostCrew > l.size()) {
            throw new IllegalArgumentException("Not enough crew members to accept the card");
        } else {
            for (Cabin cabin : l) {
                p.getShip().unloadCrew(cabin);
            }
        }

        p.addCredits(credits);
        g.move(p, -lostDays);
        playCard();
    }
}