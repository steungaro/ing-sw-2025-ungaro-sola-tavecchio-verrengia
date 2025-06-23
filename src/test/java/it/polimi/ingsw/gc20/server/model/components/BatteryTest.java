package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.ship.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BatteryTest {
    private Battery battery;

    /**
     * Sets up the Battery instance before each test.
     * Initializes the connectors for the battery.
     */
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

    /**
     * Test the set slots and available energy of the battery.
     * It checks that the battery can hold a certain number of slots and available energy,
     * and that it can use energy correctly.
     */
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

    /**
     * Test the set and get methods for the tile attribute of Battery.
     */
    @Test
    void testSetGetTile(){
        Tile tile = new Tile();
        battery.setTile(tile);
        assertEquals(tile, battery.getTile());
    }

    /**
     * Test the rotation clockwise functionality of the battery,
     * it checks that the connectors are updated correctly
     * and the rotation direction is set to RIGHT.
     * It also checks that the view component is created correctly
     * with the correct rotation component value.
     */
    @Test
    void testRotateClockwise() {
        battery.rotateClockwise();
        assertEquals(Direction.RIGHT, battery.getRotation());
        assertEquals(ConnectorEnum.D, battery.getConnectors().get(Direction.UP));
        assertEquals(ConnectorEnum.U, battery.getConnectors().get(Direction.RIGHT));
        assertEquals(ConnectorEnum.S, battery.getConnectors().get(Direction.DOWN));
        assertEquals(ConnectorEnum.ZERO, battery.getConnectors().get(Direction.LEFT));
        assertEquals(Direction.RIGHT, battery.getRotation());
        ViewComponent component = battery.createViewComponent();
        assertEquals(1, component.rotComp);
    }
}
