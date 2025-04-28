package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.model.ship.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BatteryTest {
    private Battery battery;

    @BeforeEach
    void setUp() {
        battery = new Battery();
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
}
