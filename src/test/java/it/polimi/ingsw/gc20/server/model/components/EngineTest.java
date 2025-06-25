package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EngineTest {
    Engine engine;

    /**
     * Sets up the Engine instance before each test.
     * Initializes the connectors for the engine.
     */
    @BeforeEach
    void setUp() {
        engine = new Engine();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        engine.setConnectors(connectors);
    }

    /**
     * Tests that the engine can be rotated clockwise and counterclockwise,
     * and that the connectors are updated accordingly.
     * It checks the orientation and the connectors after each rotation.
     */
    @Test
    void rotate() {
        engine.rotateClockwise();
        assertEquals(Direction.LEFT, engine.getOrientation());
        assertEquals(ConnectorEnum.S, engine.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.D, engine.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.ZERO, engine.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.U, engine.getConnectors().get(Direction.RIGHT));
        engine.rotateCounterclockwise();
        assertEquals(Direction.DOWN, engine.getOrientation());
        assertEquals(ConnectorEnum.ZERO, engine.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.U, engine.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.D, engine.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.S, engine.getConnectors().get(Direction.RIGHT));
    }

    /**
     * Tests the creation of a ViewComponent from the Engine.
     */
    @Test
    void createViewComponent() {
        Map<Direction, ConnectorEnum> connectors = engine.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        engine.setConnectors(connectors);
        ViewComponent viewComponent = engine.createViewComponent();
        assertNotNull(viewComponent);
    }
}
