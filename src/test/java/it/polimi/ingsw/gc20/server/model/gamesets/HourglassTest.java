package it.polimi.ingsw.gc20.server.model.gamesets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HourglassTest {
    private Hourglass hourglass;

    @BeforeEach
    public void setup() {
        // Create an Hourglass instance with a period of 10 seconds
        hourglass = new Hourglass(10);
    }
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
