package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;

/**
 * @author GC20
 */
public class CombatZone extends AdventureCard {
    private int lostDays;
    private int lostCargo;
    private List<Projectile> cannonFire;
    private int lostCrew;

    /**
     * Default constructor
     */
    public CombatZone() {
        super();
        lostDays = 0;
        lostCargo = 0;
        lostCrew = 0;
        cannonFire = new ArrayList<>();
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
     * Getter method for lostCargo
     * @return lostCargo
     */
    public int getLostCargo() {
        return lostCargo;
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


    public void setLostCrew(int lostCrew) {
        this.lostCrew = lostCrew;
    }

    public int getLostCrew() {
        return lostCrew;
    }

    /**
     * @return the type of combat
     * @implNote if the card has lostCrew > 0, the method returns 1, else 0
     */
    public int combatType() {
        if (lostCrew > 0) {
            return 1;
        } else {
            return 0;
        }
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
     * @param l is the list of cabins that lost crew
     * @implNote the player loses days
     */
    public void EffectLostCrew(Player p, List<Cabin> l) {
        int count = 0;
        int f = 1;
        Cabin i = l.getFirst();
        while (count < lostCrew) {
            while(count < lostCrew && i.getAstronauts() > 0) {
                p.getShip().unloadCrew(i);
                count++;
            }
            if(f == l.size()) {
                break;
            }
            i = l.get(f);
            f++;
        }
    }

    /**
     * applies the effect of the card to the player
     * @param p player
     * @param c cargo lost
     * @implNote the player loses cargo, the cargo is removed from the cargo hold from the most valuable one
     */
    public void EffectLostCargo(Player p, List<CargoHold> c) {
        for (CargoHold i : c) {
            p.getShip().unloadCargo(i);
        }
    }

    /**
     * applies the effect of the card to the player
     * @apiNote the controller needs to verify for each fire the requirements and then apply the rules specified in Projectile
     * @see Projectile
     */
    public List<Projectile> EffectCannonFire() {
        return this.getCannonFire();
    }
}