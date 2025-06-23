package it.polimi.ingsw.gc20.server.model.components;


import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ShieldTest {
    Shield shield;

    /**
     * Sets up the Shield instance before each test.
     * Initializes the shield object and sets its covered sides.
     */
    @BeforeEach
    void setUp() {
        shield = new Shield();
        Direction[] coveredSides = new Direction[2];
        coveredSides[0] = Direction.UP;
        coveredSides[1] = Direction.RIGHT;
        shield.setCoveredSides(coveredSides);
    }

    /**
     * Tests the getCoveredSides method of the Shield class.
     */
    @Test
    void getCoveredSides() {
        Direction[] coveredSides = shield.getCoveredSides();
        assertSame(Direction.UP, coveredSides[0]);
        assertSame(Direction.RIGHT, coveredSides[1]);
    }

    /**
     * Tests the rotateClockwise and rotateCounterclockwise methods of the Shield class.
     * It checks that the covered sides are updated correctly after each rotation.
     */
    @Test
    void rotate(){
        shield.rotateClockwise();
        assertSame(Direction.RIGHT, shield.getCoveredSides()[0]);
        assertSame(Direction.DOWN, shield.getCoveredSides()[1]);

        shield.rotateCounterclockwise();
        assertSame(Direction.UP, shield.getCoveredSides()[0]);
        assertSame(Direction.RIGHT, shield.getCoveredSides()[1]);
    }

    /**
     * Tests the creation of a ViewComponent from the Shield component.
     * It checks that the ViewComponent is not null after creation.
     */
    @Test
    void createViewComponent() {
        Map<Direction, ConnectorEnum> connectors = shield.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        shield.setConnectors(connectors);
        ViewComponent viewComponent = shield.createViewComponent();
        assertNotNull(viewComponent);
    }
}
