package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import java.util.*;

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

    /**
     * @param controller
     * @param model
     */
    @Override
    public void setState(GameController controller, GameModel model) {

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
     * @param l is the list of cabins that lose a crew member
     * @implNote The player loses the crew members
     */
    public void EffectFailure(Player p, List<Cabin> l) {
        for (Cabin c : l) {
            p.getShip().unloadCrew(c);
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