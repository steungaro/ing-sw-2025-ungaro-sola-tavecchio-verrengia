package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StartingCabinTest {
    StartingCabin cabin;

    /**
     * Sets up the StartingCabin instance before each test.
     * Initializes the cabin object.
     */
    @BeforeEach
    void setUp() {
        cabin = new StartingCabin();
    }

    /**
     * Tests that the starting cabin cannot load aliens,
     * and that it throws an exception when trying to set or unload an alien.
     */
    @Test
    void alien(){
        try {
            cabin.setAlien(AlienColor.BROWN);
        } catch (InvalidAlienPlacement e) {
            assertEquals("Cannot set an alien in the starting cabin", e.getMessage());
        }
        assertFalse(cabin.getAlien());
        assertEquals(AlienColor.NONE, cabin.getAlienColor());
        try {
            cabin.unloadAlien();
        } catch (InvalidAlienPlacement e) {
            assertEquals("Cannot unload an alien in the starting cabin", e.getMessage());
        }
    }

    /**
     * Test that if the starting cabin has a life support near, the color remains NONE,
     * and that it can be removed without changing the color.
     */
    @Test
    void Support(){
        LifeSupport ls = new LifeSupport();
        ls.setColor(AlienColor.BROWN);
        cabin.addSupport(ls);
        assertEquals(AlienColor.NONE, cabin.getCabinColor());
        cabin.removeSupport(ls);
        assertEquals(AlienColor.NONE, cabin.getCabinColor());
    }

    /**
     * Test that the view component of the starting cabin can be created correctly,
     */
    @Test
    void testCreateViewComponent() {
        Map<Direction, ConnectorEnum> connectors = cabin.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        cabin.setConnectors(connectors);
        ViewComponent viewComponent = cabin.createViewComponent();
        assertNotNull(viewComponent);
    }

}
