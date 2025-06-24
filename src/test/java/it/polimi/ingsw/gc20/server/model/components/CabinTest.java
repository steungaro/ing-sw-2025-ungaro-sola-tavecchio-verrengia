package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CabinTest {
    private Cabin cabin;

    /**
     * Sets up the Cabin instance before each test.
     * Initializes the cabin object.
     */
    @BeforeEach
    void setUp() {
        cabin = new Cabin();
    }

    /**
     * Tests the set, get, and unload methods for astronauts in the Cabin.
     */
    @Test
    void testSetAndGetAstronauts() {
        cabin.setAstronauts(1);
        assertEquals(1, cabin.getAstronauts());
        cabin.unloadAstronaut();
        assertEquals(0, cabin.getAstronauts());
    }

    /**
     * Test that the alien can be set and retrieved correctly,
     * and that it throws an exception when trying to set an alien
     * when there are astronauts present in the cabin.
     * It also checks that the cabin color is updated correctly
     * @throws InvalidAlienPlacement if the alien cannot be placed, not thrown in this test
     */
    @Test
    void testSetAndGetAlien() throws InvalidAlienPlacement {
        cabin.setColor(AlienColor.BROWN);
        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
        cabin.setAlien(AlienColor.BROWN);
        assertTrue(cabin.getAlien());
        assertEquals(AlienColor.BROWN, cabin.getAlienColor());
        cabin.unloadAlien();
        assertFalse(cabin.getAlien());
        cabin.setAstronauts(2);
        cabin.unloadAstronaut();
        cabin.unloadAstronaut();
        assertThrows(InvalidAlienPlacement.class, ()-> cabin.setAlien(AlienColor.PURPLE));
        cabin.setColor(AlienColor.PURPLE);
        assertEquals(AlienColor.BOTH, cabin.getCabinColor());
    }

    /**
     * Tests that the cabin correctly updates its color
     * when adding and removing life support systems.
     */
    @Test
    void testAddAndRemoveSupport() throws InvalidAlienPlacement {
        LifeSupport lifeSupport = new LifeSupport();
        lifeSupport.setColor(AlienColor.BROWN);
        cabin.addSupport(lifeSupport);
        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
        cabin.setAlien(AlienColor.BROWN);
        cabin.removeSupport(lifeSupport);
        assertFalse(cabin.getAlien());
        LifeSupport lifeSupport2 = new LifeSupport();
        lifeSupport2.setColor(AlienColor.PURPLE);
        cabin.addSupport(lifeSupport2);
        cabin.addSupport(lifeSupport);
        assertEquals(AlienColor.BOTH, cabin.getCabinColor());
        cabin.removeSupport(lifeSupport);
        assertEquals (AlienColor.PURPLE, cabin.getCabinColor());
    }

    /**
     * Tests the creation of a ViewComponent for the Cabin.
     * It checks that the cabin's connectors are set correctly,
     * the cabin has astronauts,
     * and the cabin color is set correctly.
     */
    @Test
    void testCreateViewComponent(){
        Map<Direction, ConnectorEnum> connectors = cabin.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        cabin.setConnectors(connectors);
        assertEquals("\u001B[33mB\u001B[0m", AlienColor.BROWN.getColorChar());
        cabin.setAstronauts(1);
        cabin.setColor(AlienColor.BROWN);
        ViewComponent viewComponent = cabin.createViewComponent();
        assertNotNull(viewComponent);
    }


}
