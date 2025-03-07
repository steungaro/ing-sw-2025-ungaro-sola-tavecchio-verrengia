package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.ship.*;

/**
 * @author GC20
 */
public class AbandonedShip extends AdventureCard {

    private Integer lostCrew;
    private Integer credits;
    private Integer lostDays;

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
     * @param lostCrew
     */

    public void setLostCrew(Integer lostCrew) {
        this.lostCrew = lostCrew;
    }

    /**
     * Getter method for lostCrew
     * @return lostCrew
     */

    public Integer getLostCrew() {
        return lostCrew;
    }

    /**
     * Setter method for credits
     * @param credits credits
     */

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    /**
     * Getter method for credits
     * @return credits
     */

    public Integer getCredits() {
        return credits;
    }

    /**
     * Setter method for lostDays
     * @param lostDays lostDays
     */

    public void setLostDays(Integer lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Getter method for lostDays
     * @return lostDays
     */

    public Integer getLostDays() {
        return lostDays;
    }

    /**
     * Applies card effect on player p, applying effect means the player accepted the card
     * @param p player that accepted the card
     * @param g game
     * @effect player p loses lostCrew crew members, gains credits credits and loses lostDays days
     * @TODO CREW MODIFICATION (maybe a list of components is needed)
     * @throws IllegalArgumentException if the player does not have enough crew members
     */
    public void Effect(Player p, Game g) {
        if (p.getShip().getCrew() < lostCrew) {
            throw new IllegalArgumentException("Not enough crew members for player " + p.getUsername());
        } else {
            // TODO CREW MODIFICATION
            p.addCredits(credits);
            g.move(p, -lostDays);
        }
    }

}