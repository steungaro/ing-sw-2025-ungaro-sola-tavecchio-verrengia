package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


class ShipTest {

    private TestShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin cabin;

    @BeforeEach
    void setUp() {
        // Create a concrete test implementation
        ship = new TestShip();

        // Create components
        upCannon = new Cannon();
        upCannon.setOrientation(Direction.UP);
        upCannon.setPower(1);

        downCannon = new Cannon();
        downCannon.setOrientation(Direction.DOWN);
        downCannon.setPower(2);

        singleEngine = new Engine();
        singleEngine.setPower(1);

        doubleEngine = new Engine();
        doubleEngine.setPower(2);

        battery = new Battery();
        cabin = new Cabin();

        // Set up tiles for components
        setupTiles();

        // Set up a known configuration
        ship.addComponent(upCannon, 1, 1);
        ship.addComponent(downCannon, 2, 1);
        ship.addComponent(singleEngine, 2, 2);
        ship.addComponent(doubleEngine, 1, 2);
        ship.addComponent(battery, 1, 3);
        ship.addComponent(cabin, 2, 3);
    }

     private void setupTiles() {

    }

    @Test
    void testGetCannonsByDirection() {
        // Test getting cannons pointing UP at column 1
        List<Cannon> upCannons = ship.getCannons(Direction.UP, 1);
        assertEquals(1, upCannons.size());
        assertEquals(Direction.UP, upCannons.get(0).getOrientation());

        // Test getting cannons pointing DOWN at column 1
        List<Cannon> downCannons = ship.getCannons(Direction.DOWN, 1);
        assertEquals(1, downCannons.size());
        assertEquals(Direction.DOWN, downCannons.get(0).getOrientation());

        // Test with an empty column
        List<Cannon> emptyCannons = ship.getCannons(Direction.UP, 2);
        assertTrue(emptyCannons.isEmpty());
    }

    @Test
    void testFirePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);

        ship.totalEnergy = 1;

        float power = ship.firePower(cannons, 1);
        assertEquals(2.0f, power); // 1 from singleCannonsPower + 2 from downCannon
    }

    @Test
    void testGetFirstComponent() {
        // Test getting the first component in the ship
        Component firstComponent = ship.getFirstComponent(Direction.UP, 1);
        assertEquals(upCannon, firstComponent);

        firstComponent = ship.getFirstComponent(Direction.DOWN, 1);
        assertEquals(downCannon, firstComponent);
        
    }

    private class TestShip extends Ship {
        private Tile[][] table = new Tile[4][4];

        // Constructor to initialize all tiles
        public TestShip() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    table[i][j] = new Tile();
                    table[i][j].setAvailability(true);
                }
            }
        }

        @Override
        public Integer getRows() {
            return 4;
        }

        @Override
        public Integer getCols() {
            return 4;
        }

        @Override
        protected Component getComponentAt(int row, int col) {
            if (row >= 0 && row < getRows() && col >= 0 && col < getCols())
                return table[row][col].getComponent();
            return null;
        }

        @Override
        protected void setComponentAt(Component c, int row, int col) {
            if (row >= 0 && row < getRows() && col >= 0 && col < getCols())
                table[row][col].addComponent(c);
        }

        @Override
        public void addComponent(Component c, int row, int col){
            if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
                setComponentAt( c, row, col);
                updateParameters(c, 1);
            }
        }
    }
}