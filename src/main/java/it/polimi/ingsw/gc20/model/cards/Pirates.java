package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.PiratesState;
import it.polimi.ingsw.gc20.controller.states.State;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;

/**
 * @author GC20
 */
public class Pirates extends AdventureCard implements Enemy {
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
        cannonFire = new ArrayList<>();
    }

    @Override
    public void setState(GameController controller, GameModel model) {
        State state = new PiratesState(controller, model, firePower, cannonFire, credits, lostDays);
        controller.setState(state);
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
}