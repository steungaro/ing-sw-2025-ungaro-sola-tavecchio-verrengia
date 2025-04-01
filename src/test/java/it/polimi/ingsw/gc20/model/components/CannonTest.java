package it.polimi.ingsw.gc20.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CannonTest {
    private Cannon cannon;

    @BeforeEach
    void setUp() {
        cannon = new Cannon();
    }

    @Test
    void testSetAndGetPower(){
        cannon.setPower(5);
        assertEquals(5, cannon.getPower());
    }

    @Test
    void testSetAndGetOrientation(){
        cannon.setOrientation(Direction.DOWN);
        assertEquals(Direction.DOWN, cannon.getOrientation());
        cannon.rotateClockwise();
        assertEquals(cannon.getOrientation(), Direction.LEFT);
    }
}
