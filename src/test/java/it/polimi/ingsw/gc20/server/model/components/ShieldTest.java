package it.polimi.ingsw.gc20.server.model.components;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ShieldTest {
    Shield shield;

    @BeforeEach
    void setUp() {
        shield = new Shield();
        Direction[] coveredSides = new Direction[2];
        coveredSides[0] = Direction.UP;
        coveredSides[1] = Direction.RIGHT;
        shield.setCoveredSides(coveredSides);
    }

    @Test
    void getCoveredSides() {
        Direction[] coveredSides = shield.getCoveredSides();
        assertSame(Direction.UP, coveredSides[0]);
        assertSame(Direction.RIGHT, coveredSides[1]);
    }

    @Test
    void rotate(){
        shield.rotateClockwise();
        assertSame(Direction.RIGHT, shield.getCoveredSides()[0]);
        assertSame(Direction.DOWN, shield.getCoveredSides()[1]);

        shield.rotateCounterclockwise();
        assertSame(Direction.UP, shield.getCoveredSides()[0]);
        assertSame(Direction.RIGHT, shield.getCoveredSides()[1]);
    }
}
