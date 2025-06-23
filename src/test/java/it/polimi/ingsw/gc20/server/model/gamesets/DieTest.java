package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.DieNotRolledException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    private Die die;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        die = new Die();
    }

    /**
     * Cleans up the test environment after each test case.
     * This method is called after each test to ensure that the die object is reset.
     */
    @AfterEach
    void tearDown() {
        die = null;
    }

    /**
     * Tests the rollDie method of the Die class.
     */
    @Test
    void rollDie() {
        int result = die.rollDie();
        assertTrue(result >= 1 && result <= 6, "Die roll should be between 1 and 6");
    }

    /**
     * Tests the getLastRolled method of the Die class.
     * It should throw a DieNotRolledException if the die has not been rolled yet.
     */
    @Test
    void getLastRolled() {
        assertThrows(DieNotRolledException.class, ()->die.getLastRolled());
        die.rollDie();
        try {
            int lastRolled = die.getLastRolled();
            assertTrue(lastRolled >= 1 && lastRolled <= 6, "Last rolled value should be between 1 and 6");
        } catch (DieNotRolledException e) {
            fail("Die should have been rolled, but exception was thrown: " + e.getMessage());
        }

    }
}