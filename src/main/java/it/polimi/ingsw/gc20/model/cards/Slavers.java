package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;

/**
 * @author GC20
 */
public class Slavers extends AdventureCard {
    private Integer firePower;
    private Integer lostMembers;
    private Integer reward;
    private Integer lostDays;

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
    public void setFirePower(Integer firePower) {
        this.firePower = firePower;
    }

    /**
     * Getter method for the firepower
     * @return firepower
     */
    public Integer getFirePower() {
        return firePower;
    }

    /**
     * Setter method for the lostMembers
     * @param lostMembers lostMembers
     */
    public void setLostMembers(Integer lostMembers) {
        this.lostMembers = lostMembers;
    }

    /**
     * Getter method for the lostMembers
     * @return lostMembers
     */
    public Integer getLostMembers() {
        return lostMembers;
    }

    /**
     * Setter method for the reward
     * @param reward reward
     */
    public void setReward(Integer reward) {
        this.reward = reward;
    }

    /**
     * Getter method for the reward
     * @return reward
     */
    public Integer getReward() {
        return reward;
    }

    /**
     * Setter method for the lostDays
     * @param lostDays lostDays
     */
    public void setLostDays(Integer lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Getter method for the lostDays
     * @return lostDays
     */
    public Integer getLostDays() {
        return lostDays;
    }

    /**
     * @param p
     * @param g
     */
    public void EffectFailure(Player p, Game g) {
        // TODO implement here
    }

    /**
     * @param p
     * @param g
     */
    public void EffectSuccess(Player p, Game g) {
        // TODO implement here
    }

}