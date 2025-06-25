package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.DieNotRolledException;

import java.util.*;

/**
 * Represents a six-sided die which can be rolled to produce a random result.
 * The class encapsulates functionality for rolling the die and retrieving the last rolled value.
 * It also throws an exception if the last rolled value is requested, but the die has not been rolled yet.
 */
public class Die {
    private int lastRolled;
    private final Random rand = new Random();

    /**
     * Default constructor for the Die class.
     * Initializes the die with no previously rolled value.
     */
    public Die() {
        this.lastRolled = 0;
    }

    /**
     * Rolls the die and generates a random number between 1 and 6.
     * Updates the last rolled value with the generated number.
     *
     * @return the value of the die roll, a random integer between 1 and 6 (inclusive)
     */
    public int rollDie() {
        this.lastRolled = rand.nextInt(6) + 1;
        return this.lastRolled;
    }

    /**
     * Retrieves the last rolled value of the die. If the die has not been rolled yet,
     * a DieNotRolledException is thrown.
     *
     * @return the value of the last roll, an integer between 1 and 6 (inclusive)
     * @throws DieNotRolledException if the die has not been rolled yet
     */
    public int getLastRolled() throws DieNotRolledException {
        if (this.lastRolled == 0) {
            throw new DieNotRolledException("Die not rolled yet");
        }
        return this.lastRolled;
    }

}