package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.components.CargoHold;

/**
 * @author GC20
 */
public class CombatZone extends AdventureCard {
    private int lostDays;
    private int lostCargo;
    private List<Projectile> cannonFire;

    /**
     * Default constructor
     */
    public CombatZone() {
        lostDays = 0;
        lostCargo = 0;
        cannonFire = new ArrayList<Projectile>();
    }

    /**
     * Setter method for lostDays
     * @param lostDays lost days
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
     * Setter method for lostCargo
     * @param lostCargo lostCargo
     * @implSpec cargo is always the most valuable cargo for player
     */
    public void setLostCargo(int lostCargo) {
        this.lostCargo = lostCargo;
    }

    /**
     * Getter method for lostCargo
     * @return lostCargo
     * @implSpec cargo is always the most valuable cargo for player
     */
    public Integer getLostCargo() {
        return lostCargo;
    }

    /**
     * Setter method for cannonFire
     * @param cannonFire cannonFire
     */
    public void setCannonFire(List<Projectile> cannonFire) {
        this.cannonFire = cannonFire;
    }

    /**
     * Getter method for cannonFire
     * @return cannonFire
     */
    public List<Projectile> getCannonFire() {
        return cannonFire;
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param g game
     * @implNote the player loses days
     */
    public void EffectLostDays(Player p, Game g) {
        g.move(p, -lostDays);
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param g game
     * @param c cargo lost
     * @implNote the player loses cargo
     */
    public void EffectLostCargo(Player p, Game g, List<Cargo> c) {
        for(Cargo i : c) {
            CargoHold ch = i.getCargoHold();
            ch.unloadCargo(i);
        }
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param g game
     * @implNote the player is fired at
     * @? should we delete this method and let the controller loop through the cannonFire list?
     * @TODO: check if this method is needed
     */
    public void EffectCannonFire(Player p, Game g) {
        for (Projectile projectile : cannonFire) {
            projectile.Fire(p.getShip(), diceResult);
        }
    }
}