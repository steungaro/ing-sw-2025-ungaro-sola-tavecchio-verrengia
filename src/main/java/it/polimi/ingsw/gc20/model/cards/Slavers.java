package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.SlaversState;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

/**
 * @author GC20
 */
public class Slavers extends AdventureCard implements Enemy{
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

    @Override
    public void setState(GameController controller, GameModel model) {
        controller.setState(new SlaversState(controller, model, firePower, lostMembers, reward, lostDays));
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
}