package it.polimi.ingsw.gc20.model.cards;

import java.util.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.bank.*;

/**
 * @author GC20
 */
public class AbandonedStation extends AdventureCard {
    private int crewNeeded;
    private List<CargoColor> reward;
    private int lostDays;

    /**
     * Default constructor
     */
    public AbandonedStation() {
        super();
        crewNeeded = 0;
        reward = new ArrayList<>();
        lostDays = 0;
    }

    /**
     * Setter method for crewNeeded
     * @param crewNeeded crewNeeded
     */
    public void setCrewNeeded(int crewNeeded) {
        this.crewNeeded = crewNeeded;
    }

    /**
     * Getter method for crewNeeded
     * @return crewNeeded
     */
    public int getCrewNeeded() {
        return crewNeeded;
    }

    /**
     * Setter method for reward
     * @param reward reward
     * @implSpec  reward is already ordered by value
     */
    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    /**
     * Getter method for reward
     * @return reward
     */
    public List<CargoColor> getReward() {
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
     * @implNote The player loses the lostDays, and the list of cargo is returned to the caller
     * @apiNote The controller needs to verify that the player has enough crew members to accept the card
     */
    public List<Cargo> Effect(Player p, Game g) {
        g.move(p, -lostDays);

        return reward.stream()
                .map(Cargo::new)
                .toList();
    }
}