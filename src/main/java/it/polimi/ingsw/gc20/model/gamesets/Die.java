package it.polimi.ingsw.gc20.model.gamesets;

import java.io.*;
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
     * Function to get the last rolled value
     * @return int the last rolled value
     * @throws ArithmeticException if the die has not been rolled yet
     */
    public int getLastRolled() throws ArithmeticException {
        if (this.lastRolled == 0) {
            throw new ArithmeticException("Die not rolled yet");
        }
        return this.lastRolled;
    }

}