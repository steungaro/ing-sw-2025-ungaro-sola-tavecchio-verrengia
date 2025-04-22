package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void getComponent() {
        Tile tile = new Tile();
        Component component = new Cabin();
        try {
            tile.addComponent(component);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(component, tile.getComponent());
    }

    @Test
    void addComponent() {
        Tile tile = new Tile();
        Component component = new Cabin();
        try {
            tile.addComponent(component);
        } catch (InvalidTileException e){
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(component, tile.getComponent());

        assertThrows(InvalidTileException.class, () -> tile.addComponent(new Cabin()));
    }

    @Test
    void getAvailability() {
        Tile tile = new Tile();
        assertTrue(tile.getAvailability());

        Component component = new Cabin();
        try {
            tile.addComponent(component);
        } catch (InvalidTileException e){
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(tile.getAvailability());
    }

    @Test
    void setAvailability() {
        Tile tile = new Tile();
        tile.setAvailability(false);
        assertFalse(tile.getAvailability());

        tile.setAvailability(true);
        assertTrue(tile.getAvailability());
    }

    @Test
    void removeComponent() {
        Tile tile = new Tile();
        Component component = new Cabin();
        try {
            tile.addComponent(component);
            assertEquals(component, tile.getComponent());

            tile.removeComponent();
        } catch (InvalidTileException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertNull(tile.getComponent());
        assertTrue(tile.getAvailability());
        try {
            tile.removeComponent();
        } catch (InvalidTileException e) {
            assertEquals("Component does not exist", e.getMessage());
        }
    }
}