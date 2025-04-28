package it.polimi.ingsw.gc20.server.model.gamesets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HourglassTest {
    private Hourglass hourglass;

    @BeforeEach
    public void setup() {
        // Create an Hourglass instance with a period of 90 seconds
        hourglass = new Hourglass(10);
    }

    @Test
    public void testInitialState() {
        // Test: Verify that the period is correctly initialized
        assertEquals(10, hourglass.getPeriod(), "The initial period should be 90 seconds.");

        // Test: Verify that the turned counter starts at 0
        assertEquals(0, hourglass.getTurned(), "The turned counter should start at 0.");

        // Test: Verify that the remaining time is initially equal to the period
        assertEquals(10, hourglass.getRemainingTime(), "The remaining time should initially be 90 seconds.");
    }

    @Test
    public void testTurnHourglass() {
        // Test: Turn the hourglass for the first time
        hourglass.turn();

        // The remaining time should be reset to the period (90 seconds)
        assertEquals(10, hourglass.getRemainingTime(), "The remaining time should be 90 seconds after turning the hourglass.");

        // Test: After turning, the turned counter should be 1
        assertEquals(1, hourglass.getTurned(), "The turned counter should be 1 after the first turn.");
    }

    @Test
    public void testGetTotalElapsed() throws InterruptedException {
        // Test: Turn the hourglass for the first time
        hourglass.initCountdown();

        Thread.sleep(4900);
        int expectedElapsed = hourglass.getPeriod() - hourglass.getRemainingTime();
        assertEquals(5, expectedElapsed, "The total elapsed time should be 5 seconds after 5 seconds have passed.");
    }

    @Test
    public void testStopCountdown() throws InterruptedException {
        // Test: Turn the hourglass for the first time
        hourglass.initCountdown();

        // Now stop the countdown
        hourglass.stopCountdown();

        // After stopping, remaining time should not change
        int remainingTimeAfterStop = hourglass.getRemainingTime();

        Thread.sleep(5000);
        assertEquals(remainingTimeAfterStop, hourglass.getRemainingTime(), "The remaining time should not change after stopping the countdown.");
    }

    @Test
    public void testMultipleTurns() throws InterruptedException {
        // Test: Turn the hourglass three times
        hourglass.initCountdown();
        Thread.sleep(11000);
        hourglass.turn();
        Thread.sleep(11000);
        hourglass.turn();

        // Each turn should reset the remaining time to 10 seconds
        assertEquals(20, hourglass.getTotalElapsed(), "Total elapsed time should be 20 after 2 turns.");

        // The turned counter should be 2
        assertEquals(2, hourglass.getTurned(), "The turned counter should be 2 after two turns.");
    }

    @Test
    public void testMultipleCountdowns() throws InterruptedException {
        // Test: Turn the hourglass, let some time pass, and then stop and turn it again
        hourglass.initCountdown();

        // Simulate some time passing (simulate 1 second delay)
        Thread.sleep(900);  // This is not ideal for real tests, but works here for simplicity

        // Check if the remaining time is decreased by 1 second after the 1 second of sleep
        assertEquals(9, hourglass.getRemainingTime(), "The remaining time should be 9 seconds after 1 second has passed.");

        // Stop the countdown and turn it again
        hourglass.stopCountdown();
        hourglass.initCountdown();

        // After turning the hourglass again, the remaining time should be reset to 10
        assertEquals(10, hourglass.getRemainingTime(), "The remaining time should be reset to 10 seconds after a new turn.");
    }
}
