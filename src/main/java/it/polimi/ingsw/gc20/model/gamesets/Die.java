package it.polimi.ingsw.gc20.model.gamesets;

import java.util.*;

/**
 * 
 */
public class Die {
    private int lastRolled;
    private final Random rand = new Random();

    public Die() {
        this.lastRolled = 0;
    }

    public int rollDie() {
        this.lastRolled = rand.nextInt(6) + 1;
        return this.lastRolled;
    }

    /**
     * @return the last rolled value
     */
    public int getLastRolled() {
        if (this.lastRolled == 0) {
            throw new ArithmeticException("Die not rolled yet");
        }
        return this.lastRolled;
    }

}