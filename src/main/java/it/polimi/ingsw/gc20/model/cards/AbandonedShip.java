package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

/**
 * @author GC20
 */
public class AbandonedShip extends AdventureCard {

    private int lostCrew;
    private int credits;
    private int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShip() {
        super();
        lostCrew = 0;
        credits = 0;
        lostDays = 0;
    }

    @Override
    public void setState(GameController gc, GameModel gm) {
        State state = new AbandonedShipState(gc, gm, lostCrew, credits, lostDays);
        gc.setState(state);
    }

    /**
     * Setter method for lostCrew
     * @param lostCrew lost crew
     */

    public void setLostCrew(int lostCrew) {
        this.lostCrew = lostCrew;
    }

    /**
     * Getter method for lostCrew
     * @return lostCrew
     */

    public int getLostCrew() {
        return lostCrew;
    }

    /**
     * Setter method for credits
     * @param credits credits
     */

    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Getter method for credits
     * @return credits
     */

    public int getCredits() {
        return credits;
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
}