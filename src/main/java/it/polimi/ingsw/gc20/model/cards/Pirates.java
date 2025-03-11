package it.polimi.ingsw.gc20.model.cards;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;

/**
 * @author GC20
 */
public class Pirates extends AdventureCard {
    private List<Projectile> cannonFire;
    private int firePower;
    private int credits;
    private int lostDays;

    /**
     * Default constructor
     */
    public Pirates() {
        super();
        firePower = 0;
        credits = 0;
        lostDays = 0;
    }

    public void setCannonFire(List<Projectile> cannonFire) {
        this.cannonFire = cannonFire;
    }

    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    public List<Projectile> getCannonFire() {
        return cannonFire;
    }

    public int getFirePower() {
        return firePower;
    }

    public int getCredits() {
        return credits;
    }

    public int getLostDays() {
        return lostDays;
    }

    /**
     * @param p is the player that has drawn the card
     * @param g is the game
     * @implNote the player moves back of lostDays days and gains credits
     */
    public void EffectSuccess(Player p, Game g) {
        g.move(p, -lostDays);
        p.addCredits(credits);
    }

    /**
     * @param p is the player that has drawn the card
     * @param g is the game
     * @return the list of projectiles that will be fired
     * @implNote the projectiles are fired in the order they are in the list
     * @apiNote the controller needs to obtain the list of fires and apply the rules specified in Projectile
     * @see Projectile
     */
    public List<Projectile> EffectFailure(Player p, Game g) {
        return this.getCannonFire();
    }
}