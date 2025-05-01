package it.polimi.ingsw.gc20.server.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EngineTest {
    Engine engine;

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

    @Test
    void rotate() {
        engine.setOrientation(Direction.UP);
        engine.rotateClockwise();
        assertEquals(Direction.RIGHT, engine.getOrientation());
        assertEquals(ConnectorEnum.S, engine.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.D, engine.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.ZERO, engine.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.U, engine.getConnectors().get(Direction.RIGHT));
        engine.rotateCounterclockwise();
        assertEquals(Direction.UP, engine.getOrientation());
        assertEquals(ConnectorEnum.ZERO, engine.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.U, engine.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.D, engine.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.S, engine.getConnectors().get(Direction.RIGHT));

    }
}
