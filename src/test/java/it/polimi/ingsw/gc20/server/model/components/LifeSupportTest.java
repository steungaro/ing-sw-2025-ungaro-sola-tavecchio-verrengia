package it.polimi.ingsw.gc20.server.model.components;


import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LifeSupportTest {
    LifeSupport ls;

    @BeforeEach
    void setUp() {
        ls = new LifeSupport();
    }

    @Test
    void isLifeSupport() {
        assertTrue(ls.isLifeSupport());
    }

    @Test
    void createViewComponent() {
        Map<Direction, ConnectorEnum> connectors = ls.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        ls.setConnectors(connectors);
        ViewComponent viewComponent = ls.createViewComponent();
        assertNotNull(viewComponent);

        Pipes pipes = new Pipes();
        Map<Direction, ConnectorEnum> connectors2 = pipes.getConnectors();
        connectors2.put(Direction.RIGHT, ConnectorEnum.S);
        connectors2.put(Direction.LEFT, ConnectorEnum.D);
        connectors2.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors2.put(Direction.UP, ConnectorEnum.U);
        pipes.setConnectors(connectors);
        ViewComponent viewComponent2 = pipes.createViewComponent();
        assertNotNull(viewComponent2);
    }
}
