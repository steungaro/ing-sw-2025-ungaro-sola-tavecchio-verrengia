package it.polimi.ingsw.gc20.server.model.gamesets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HourglassTest {
    private Hourglass hourglass;

    /**
     * Sets up the test environment before each test case.
     * Initializes an instance of the Hourglass class with a period of 10 seconds.
     * This setup ensures a fresh and consistent starting state for each test in the suite.
     */
    @BeforeEach
    public void setup() {
        // Create an Hourglass instance with a period of 10 seconds
        hourglass = new Hourglass(10);
    }

    /**
     * Tests the functionality of the Hourglass class, including setting the period,
     * verifying initial state, tracking countdown behavior, and handling turns.
     * <p>
     * This method verifies the following:
     * - The period can be set and retrieved correctly.
     * - The initial state of the "turned" counter is zero.
     * - The countdown mechanism accurately tracks the remaining time.
     * - After the countdown, the total elapsed time and "turned" counter values are updated correctly.
     * - Turning the hourglass updates the "turned" counter as expected.
     *
     * @throws InterruptedException if the test thread is interrupted during the sleep period.
     */
    @Test
    void hourglassTest() throws InterruptedException {
        hourglass.setPeriod(1);
        assertEquals(1, hourglass.getPeriod(), "The period should be 1.");
        assertEquals(0, hourglass.getTurned(), "The turned counter should be 0.");
        hourglass.initCountdown();
        Thread.sleep(1000);
        assertEquals(0, hourglass.getRemainingTime(), "The remaining time should be 0 after 1 second.");
        assertEquals(1, hourglass.getTotalElapsed(), "The turned counter should be 1 after 1 second.");
        hourglass.turn();
        assertEquals(1, hourglass.getTurned());
    }
}
