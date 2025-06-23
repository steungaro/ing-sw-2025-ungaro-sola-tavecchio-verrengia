package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.components.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    /**
     * Tests the getComponent method of the Tile class.
     * <p>
     * This test verifies that the getComponent method correctly returns the
     * component associated with the Tile instance after it has been added.
     * <p>
     * It ensures that:
     * - A component can be added to a tile without exceptions being thrown.
     * - The component retrieval via getComponent returns the correct instance.
     * <p>
     * The test initializes a Tile object, adds a component, and asserts that
     * the retrieved component matches the one that was added.
     */
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

    /**
     * Validates the behavior of the addComponent method in the Tile class.
     * <p>
     * This test ensures the proper functionality of adding components to a Tile instance.
     * Specific behaviors tested include:
     * <p>
     * - Successfully adding a single component to a tile when it is available.
     * - The component added being properly associated with the tile.
     * - Throwing an InvalidTileException when attempting to add a second component
     *   to a tile that is already occupied.
     * <p>
     * The test initializes a Tile object, verifies that the component is added
     * without exceptions, checks the association of the component with the tile,
     * and ensures an expected exception is thrown when attempting to add multiple
     * components to the same tile.
     */
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

    /**
     * Tests the behavior of the getAvailability method in the Tile class.
     * <p>
     * This test ensures that the availability status of a tile is properly
     * managed and accurately reflects its current state.
     * <p>
     * The test verifies the following scenarios:
     * - When a new Tile instance is initialized, its availability is true.
     * - After adding a component to the tile, its availability changes to false.
     * - The getAvailability method correctly returns the initial and updated
     *   availability status of the tile.
     * <p>
     * It initializes a Tile instance, validates its default availability,
     * adds a component to the tile, and ensures the method reflects the
     * expected change in availability after the operation.
     */
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

    /**
     * Tests the setAvailability method of the Tile class.
     * <p>
     * This test ensures that the setAvailability method properly updates the availability
     * status of the tile. It verifies that:
     * - Setting the availability to false updates the tile's status to unavailable.
     * - Setting the availability back to true makes the tile available again.
     * <p>
     * The test creates a Tile instance, manipulates its availability status using the
     * setAvailability method, and verifies the changes using assertions on the
     * getAvailability method.
     */
    @Test
    void setAvailability() {
        Tile tile = new Tile();
        tile.setAvailability(false);
        assertFalse(tile.getAvailability());

        tile.setAvailability(true);
        assertTrue(tile.getAvailability());
    }

    /**
     * Tests the functionality of the removeComponent method in the Tile class.
     * <p>
     * This test ensures that the removeComponent method behaves as expected
     * under various scenarios, including:
     * <p>
     * - Successfully removing a component from a tile, updating the tile's
     *   state, and setting its availability to true.
     * - Validating that the component is properly dissociated from the tile
     *   after removal.
     * - Handling cases where the removeComponent method is called on a tile
     *   that has no component, throwing the appropriate exception.
     * <p>
     * The test performs the following steps:
     * - Initializes a Tile object and adds a component to it.
     * - Removes the component from the tile and asserts that the tile's state
     *   changes appropriately.
     * - Attempts to remove a component from the tile when none is present
     *   and validates the exception message.
     */
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