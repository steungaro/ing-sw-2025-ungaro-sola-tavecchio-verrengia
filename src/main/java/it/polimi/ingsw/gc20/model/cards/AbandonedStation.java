package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.gamesets.Cargo;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.components.*;

/**
 * @author GC20
 */
public class AbandonedStation extends AdventureCard {
    private Integer lostMembers;
    private List<Cargo> reward;
    private Integer lostDays;

    /**
     * Default constructor
     */
    public AbandonedStation() {
        super();
        lostMembers = 0;
        reward = new ArrayList<>();
        lostDays = 0;
    }

    /**
     * Setter method for lostMembers
     * @param lostMembers lostMembers
     */
    public void setLostMembers(Integer lostMembers) {
        this.lostMembers = lostMembers;
    }

    /**
     * Getter method for lostMembers
     * @return lostMembers
     */
    public Integer getLostMembers() {
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
     * @param ch is the list of cargo holds of the player that will be loaded with the reward, in the same order as the reward
     * @param cabins is the list of cabins of the player that will be losing members
     * @param crew is the list of crew members lost in order of cabins
     * @param aliens is the list of aliens lost in order of cabins
     */
    public void Effect(Player p, Game g, List<CargoHold> ch, List<Cabin> cabins, List<Integer> crew, List<Boolean> aliens) {
        if (ch.size()!= reward.size()) {
            throw new IllegalArgumentException("Not enough cargo holds passed");
        }
        for (int i = 0; i < ch.size(); i++) {
            if(reward.get(i).equals(Cargo.RED)) {
                if (ch.get(i) instanceof SpecialCargoHold) {
                    ch.get(i).loadCargo(reward.get(i));
                } else {
                    throw new IllegalArgumentException("Can't load RED cargo in a normal cargo hold");
                }
            } else {
                ch.get(i).loadCargo(reward.get(i));
            }
        }

        for (int i = 0; i < lostMembers; i++) {
            if (aliens.get(i)) {
                cabins.get(i).removeAlien();
            } else {
                cabins.get(i).removeCrew(crew.get(i));
            }
        }
    }


}