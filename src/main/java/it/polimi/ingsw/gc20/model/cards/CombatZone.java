package it.polimi.ingsw.gc20.model.cards;

import java.util.*;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.components.Component;

/**
 * @author GC20
 */
public class CombatZone extends AdventureCard {
    private Integer lostDays;
    private Integer lostCargo;
    private List<Projectile> cannonFire;

    /**
     * Default constructor
     */
    public CombatZone() {
        lostDays = 0;
        lostCargo = 0;
        cannonFire = new ArrayList<>();
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
     * @effect the player loses days
     */
    public void EffectLostDays(Player p, Game g) {
        g.move(p, -lostDays);
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param g game
     * @effect the player loses cargo
     * @TODO implement (a list of components is needed)
     */
    public void EffectLostCargo(Player p, Game g, List<Component> comps) {
        //TODO
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param g game
     * @effect the player is fired at
     */
    public void EffectCannonFire(Player p, Game g) {
        for (Projectile projectile : cannonFire) {
            projectile.Fire(p.getShip());
        }
    }
}