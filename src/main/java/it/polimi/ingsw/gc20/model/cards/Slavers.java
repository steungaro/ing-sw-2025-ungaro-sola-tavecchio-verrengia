package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.Ship;
import it.polimi.ingsw.gc20.model.bank.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.components.*;

/**
 * @author GC20
 */
public class Slavers extends AdventureCard {
    private int firePower;
    private int lostMembers;
    private int reward;
    private int lostDays;

    /**
     * Default constructor
     */
    public Slavers() {
        super();
        this.firePower = 0;
        this.lostMembers = 0;
        this.reward = 0;
        this.lostDays = 0;
    }

    /**
     * Setter method for the firepower
     * @param firePower firepower
     */
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     * Getter method for the firepower
     * @return firepower
     */
    public int getFirePower() {
        return firePower;
    }

    /**
     * Setter method for the lostMembers
     * @param lostMembers lostMembers
     */
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    /**
     * Getter method for the lostMembers
     * @return lostMembers
     */
    public int getLostMembers() {
        return lostMembers;
    }

    /**
     * Setter method for the reward
     * @param reward reward
     */
    public void setReward(int reward) {
        this.reward = reward;
    }

    /**
     * Getter method for the reward
     * @return reward
     */
    public int getReward() {
        return reward;
    }

    /**
     * Setter method for the lostDays
     * @param lostDays lostDays
     */
    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Getter method for the lostDays
     * @return lostDays
     */
    public int getLostDays() {
        return lostDays;
    }

    /**
     * @param p is the player that the card has to affect
     * @param g is the game
     * @param l is the lost members list
     * @implNote The player loses the crew members
     */
    public void EffectFailure(Player p, Game g, List<Crew> l) {
        for (Crew c : l) {
            Component comp = c.getCabin();
            if (c instanceof Alien && comp instanceof Cabin) {
                ((Cabin) comp).unloadAliens((Alien)c);
            } else if (c instanceof Astronaut && comp instanceof Cabin) {
                ((Cabin) comp).unloadAstronauts((Astronaut)c);
            } else if (c instanceof Astronaut && comp instanceof StartingCabin) {
                ((StartingCabin) comp).unloadAstronauts((Astronaut)c);
            }
        }
    }

    /**
     * @param p is the player that the card has to affect
     * @param g is the game
     * @implNote The player loses the lostDays and gains the reward
     */
    public void EffectSuccess(Player p, Game g) {
        p.addCredits(reward);
        g.move(p, -lostDays);
    }
}