package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.*;

import java.util.*;

/**
 * @author GC20
 */
public class Smugglers extends AdventureCard {
    private int lostCargo;
    private int firePower;
    private int lostDays;
    private List<CargoColor> reward;

    /**
     * Default constructor
     */
    public Smugglers() {
        super();
        lostCargo = 0;
        firePower = 0;
        lostDays = 0;
        reward = new ArrayList<>();
    }

    /**
     * Setter method for lostCargo
     * @param lostCargo lostCargo
     */
    public void setLostCargo(int lostCargo) {
        this.lostCargo = lostCargo;
    }

    /**
     * Getter method for lostCargo
     * @return lostCargo
     */
    public int getLostCargo() {
        return lostCargo;
    }

    /**
     * Setter method for firePower
     * @param firePower firePower
     */
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     * Getter method for firePower
     * @return firePower
     */
    public int getFirePower() {
        return firePower;
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
     * Setter method for reward
     * @param reward reward
     * @implNote reward has to be already ordered by value
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
     * @param p is the player that has the effect applied onto
     * @param l is the list of cargo that the player has to lose
     * @implNote The player loses cargo
     * @apiNote The controller needs to verify that the player has enough cargo to lose (or call removeEnergy) and that the cargo lost is the most valuable owned by the player
     */
    public void EffectFailure(Player p, List<Cargo> l) {
        for (Cargo c : l) {
            p.getShip().unloadCargo(c);
        }
    }

    /**
     * @param p is the player that has the effect applied onto
     * @param g is the game where the player is playing
     * @return the list of cargo that the player gains
     * @implNote The player loses lostDays
     */
    public List<Cargo> EffectSuccess(Player p, Game g) {
        g.move(p, -lostDays);
        return reward.stream()
                .map(Cargo::new)
                .toList();
    }
}