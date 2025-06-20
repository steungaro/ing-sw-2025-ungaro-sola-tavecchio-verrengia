package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CannonTest {
    private Cannon cannon;

    @BeforeEach
    void setUp() {
        cannon = new Cannon();
        Map<Direction, ConnectorEnum> connectors = cannon.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        cannon.setConnectors(connectors);
    }

    @Test
    void testSetAndGetPower(){
        cannon.setPower(5);
        assertEquals(5, cannon.getPower());
    }

    @Test
    void rotate() {

        cannon.rotateClockwise();
        assertEquals(Direction.RIGHT, cannon.getRotation());
        assertEquals(ConnectorEnum.S, cannon.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.D, cannon.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.ZERO, cannon.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.U, cannon.getConnectors().get(Direction.RIGHT));

        cannon.rotateCounterclockwise();
        assertEquals(Direction.UP, cannon.getRotation());
        assertEquals(ConnectorEnum.ZERO, cannon.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.U, cannon.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.D, cannon.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.S, cannon.getConnectors().get(Direction.RIGHT));

    }
    @Test
    void testCreateViewComponent(){
        Map<Direction, ConnectorEnum> connectors = cannon.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        cannon.setConnectors(connectors);
        ViewComponent viewComponent = cannon.createViewComponent();
        assertNotNull(viewComponent);
    }

}
