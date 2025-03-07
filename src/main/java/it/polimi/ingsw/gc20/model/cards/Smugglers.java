package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.Cargo;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;

import java.util.*;

/**
 * @author GC20
 */
public class Smugglers extends AdventureCard {
    private Integer lostCargo;
    private Integer firePower;
    private Integer lostDays;
    private List<Cargo> reward;

    /**
     * Default constructor
     */
    public Smugglers() {
        super();
        lostCargo = 0;
        firePower = 0;
        lostDays = 0;
        reward = new ArrayList<Cargo>();
    }

    /**
     * Setter method for lostCargo
     * @param lostCargo lostCargo
     */
    public void setLostCargo(Integer lostCargo) {
        this.lostCargo = lostCargo;
    }

    /**
     * Getter method for lostCargo
     * @return lostCargo
     */
    public Integer getLostCargo() {
        return lostCargo;
    }

    /**
     * Setter method for firePower
     * @param firePower firePower
     */
    public void setFirePower(Integer firePower) {
        this.firePower = firePower;
    }

    /**
     * Getter method for firePower
     * @return firePower
     */
    public Integer getFirePower() {
        return firePower;
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