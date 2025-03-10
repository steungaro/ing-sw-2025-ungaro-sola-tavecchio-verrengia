package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.bank.*;

/**
 * @author GC20
 */
public class AbandonedStation extends AdventureCard {
    private int lostMembers;
    private List<CargoColor> reward;
    private int lostDays;

    /**
     * Default constructor
     */
    public AbandonedStation() {
        super();
        lostMembers = 0;
        reward = new ArrayList<CargoColor>();
        lostDays = 0;
    }

    /**
     * Setter method for lostMembers
     * @param lostMembers lostMembers
     */
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    /**
     * Getter method for lostMembers
     * @return lostMembers
     */
    public int getLostMembers() {
        return lostMembers;
    }

    /**
     * Setter method for reward
     * @param reward reward
     * @requires reward is already ordered by value
     */
    public void setReward(List<Cargo> reward) {
        this.reward = reward;
    }

    /**
     * Getter method for reward
     * @return reward
     */
    public List<Cargo> getReward() {
        return reward;
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
     * @param p is the player that has to be affected by the card
     * @param g is the game where the player is playing
     * @param c is the crew lost by the player
     * @effect the player loses the lostDays, the lostMembers and gains the reward
     * @IMPORTANT the reward must be added by the controller: use getReward() to get the reward
     */
    public void Effect(Player p, Game g, List<Crew> c) {
        g.move(p, -lostDays);

        for (Crew i : c) {
            if (i instanceof Alien) {
                Component cabin = i.getCabin();
                (Cabin) cabin.removeAliens(i);
            }
        }
    }
}