package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BatteryTest {
    private Battery battery;

    @BeforeEach
    void setUp() {
        battery = new Battery();
        Map<Direction, ConnectorEnum> connectors = battery.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        battery.setConnectors(connectors);
    }

    @Test
    void testBattery() {
        battery.setSlots(2);
        battery.setAvailableEnergy(2);
        assertEquals(2, battery.getSlots());
        assertEquals(2, battery.getAvailableEnergy());
        battery.useEnergy();
        assertEquals(1, battery.getAvailableEnergy());
        battery.useEnergy();
        battery.useEnergy();
        //ship will assert that no battery is going to be used if there is no energy
        assertEquals(-1, battery.getAvailableEnergy());
    }

    @Test
    void testsetGetTile(){
        Tile tile = new Tile();
        battery.setTile(tile);
        assertEquals(tile, battery.getTile());
    }

    @Test
    void testRotateClockwise() {
        battery.rotateClockwise();
        assertEquals(1, battery.getRotation());
        assertEquals(ConnectorEnum.D, battery.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.U, battery.getConnectors().get(Direction.RIGHT));
        assertEquals(ConnectorEnum.S, battery.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.ZERO, battery.getConnectors().get(Direction.LEFT));
        assertEquals(1, battery.getRotation());
        ViewComponent component = battery.createViewComponent();
        assertEquals(1, component.rotation);
    }
}
