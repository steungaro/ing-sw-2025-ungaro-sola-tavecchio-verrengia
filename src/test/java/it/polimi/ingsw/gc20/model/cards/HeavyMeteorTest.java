package it.polimi.ingsw.gc20.model.cards;
import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.model.components.Direction;

import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import it.polimi.ingsw.gc20.model.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HeavyMeteorTest {

    private HeavyMeteor heavyMeteor;
    private TestNormalShip normalShip;
    private TestCustomShip customShip;
    private Cannon cannon1, cannon2, cannon3, cannon4;
    private List<Cannon> cannons1, cannons2, cannons3;

    @BeforeEach
    void setUp() {
        heavyMeteor = new HeavyMeteor();
        normalShip = new TestNormalShip();
        customShip = new TestCustomShip();

        // Create test cannons
        cannon1 = new Cannon();
        cannon2 = new Cannon();
        cannon3 = new Cannon();
        cannon4 = new Cannon();

        // Create cannon lists
        cannons1 = List.of(cannon1);
        cannons2 = List.of(cannon2);
        cannons3 = List.of(cannon3, cannon4);

        // Set up test data in ships
        normalShip.setCannonAtPosition(Direction.UP, 4, cannons1);
        normalShip.setCannonAtPosition(Direction.DOWN, 4, cannons1);
        normalShip.setCannonAtPosition(Direction.DOWN, 3, cannons2);
        normalShip.setCannonAtPosition(Direction.DOWN, 5, cannons3);

        customShip.setCannonAtPosition(Direction.UP, 3, cannons1);
        customShip.setCannonAtPosition(Direction.DOWN, 4, cannons1);
        customShip.setCannonAtPosition(Direction.DOWN, 2, cannons2);
        customShip.setCannonAtPosition(Direction.DOWN, 4, cannons3);
    }

    @Test
    void getCannonsShouldReturnCorrectCannonsForUpDirectionWithNormalShip() {
        heavyMeteor.direction = Direction.UP;
        List<Cannon> result = heavyMeteor.getCannons(normalShip, 8);

        assertEquals(1, result.size());
        assertTrue(result.contains(cannon1));
    }

    @Test
    void getCannonsShouldReturnCorrectCannonsForDownDirectionWithNormalShip() {
        heavyMeteor.direction = Direction.DOWN;
        List<Cannon> result = heavyMeteor.getCannons(normalShip, 8);

        assertEquals(4, result.size());
        assertTrue(result.contains(cannon1));
        assertTrue(result.contains(cannon2));
        assertTrue(result.contains(cannon3));
        assertTrue(result.contains(cannon4));
    }

    @Test
    void getCannonsShouldReturnCorrectCannonsForLeftDirectionWithNormalShip() {
        heavyMeteor.direction = Direction.LEFT;
        normalShip.setCannonAtPosition(Direction.LEFT, 4, cannons1);
        normalShip.setCannonAtPosition(Direction.LEFT, 5, cannons2);
        normalShip.setCannonAtPosition(Direction.LEFT, 6, cannons3);

        List<Cannon> result = heavyMeteor.getCannons(normalShip, 10);

        assertEquals(4, result.size());
        assertTrue(result.contains(cannon1));
        assertTrue(result.contains(cannon2));
        assertTrue(result.contains(cannon3));
        assertTrue(result.contains(cannon4));
    }

    @Test
    void getCannonsShouldReturnCorrectCannonsForUpDirectionWithCustomShip() {
        heavyMeteor.direction = Direction.UP;
        List<Cannon> result = heavyMeteor.getCannons(customShip, 8);

        assertEquals(1, result.size());
        assertTrue(result.contains(cannon1));
    }

    // Custom test implementation classes
    private static class TestNormalShip extends NormalShip {
        private final Map<Direction, Map<Integer, List<Cannon>>> cannonsMap = new HashMap<>();

        public void setCannonAtPosition(Direction dir, int pos, List<Cannon> cannons) {
            cannonsMap.computeIfAbsent(dir, k -> new HashMap<>()).put(pos, new ArrayList<>(cannons));
        }

        public List<Cannon> getCannons(Direction direction, int position) {
            return cannonsMap.getOrDefault(direction, Collections.emptyMap())
                    .getOrDefault(position, new ArrayList<>());
        }
    }

    private static class TestCustomShip extends Ship {
        private final Map<Direction, Map<Integer, List<Cannon>>> cannonsMap = new HashMap<>();

        @Override
        public Integer getRows() {
            return 10; // Return an appropriate value for testing
        }

        @Override
        public Integer getCols() {
            return 10; // Return an appropriate value for testing
        }

        @Override
        protected Component getComponentAt(int row, int col) {
            return null;
        }

        @Override
        protected void setComponentAt(Component c, int row, int col) {

        }

        public void setCannonAtPosition(Direction dir, int pos, List<Cannon> cannons) {
            cannonsMap.computeIfAbsent(dir, k -> new HashMap<>()).put(pos, new ArrayList<>(cannons));
        }

        public List<Cannon> getCannons(Direction direction, int position) {
            return cannonsMap.getOrDefault(direction, Collections.emptyMap())
                    .getOrDefault(position, new ArrayList<>());
        }
    }
}