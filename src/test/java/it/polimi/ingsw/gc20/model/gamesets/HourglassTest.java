package it.polimi.ingsw.gc20.model.gamesets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import static org.junit.jupiter.api.Assertions.*;

public class HourglassTest {
    private Hourglass hourglass;

    @BeforeEach
    private void setup() {
        hourglass = new Hourglass(1);
    }

    @Test
    void testSetAndGetPeriod (){
        hourglass.setPeriod(1);
        assertEquals(hourglass.getPeriod(), 1);
    }

    @Test
    void testGetRemainingTime (){
        assertEquals(hourglass.getRemainingTime(), hourglass.getPeriod());
    }

    @Test
    void testTurn(){
        hourglass.turn();
        assertEquals(hourglass.getRemainingTime(), hourglass.getPeriod());
        assertEquals(hourglass.getTurned(), 1);
    }

    @Test
    void testInitCountdown () throws InterruptedException {
        Hourglass hourglass = new Hourglass(5);
        hourglass.initCountdown();
        Thread.sleep(3000);
        int remainingTime = hourglass.getRemainingTime();
        assertTrue(remainingTime <= 2, "Il tempo rimanente dovrebbe essere <= 2 ma è " + remainingTime);
        Thread.sleep(3000);
        assertEquals(0, hourglass.getRemainingTime(), "Il tempo rimanente dovrebbe essere 0");
    }

}
