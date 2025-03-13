package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.ship.Direction;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import it.polimi.ingsw.gc20.model.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class HeavyMeteorTest {

    private HeavyMeteor heavyMeteor;
    private Ship mockNormalShip;
    private Ship mockOtherShip;
    private List<Cannon> cannons1, cannons2, cannons3;

    @BeforeEach
    void setUp() {
        heavyMeteor = new HeavyMeteor();
        mockNormalShip = Mockito.mock(NormalShip.class);
        mockOtherShip = Mockito.mock(Ship.class);

        // Create test cannon lists
        cannons1 = new ArrayList<>();
        cannons1.add(new Cannon());

        cannons2 = new ArrayList<>();
        cannons2.add(new Cannon());
        cannons2.add(new Cannon());

        cannons3 = new ArrayList<>();
        cannons3.add(new Cannon());
    }

    @Test
    void getCannonsShouldReturnCorrectCannonsBasedOnDirection() {
        // Mock the ship's getCannons method responses
        when(mockNormalShip.getCannons(any(Direction.class), anyInt())).thenReturn(new ArrayList<>());
        when(mockNormalShip.getCannons(Direction.UP, 4)).thenReturn(cannons1);
        when(mockNormalShip.getCannons(Direction.DOWN, 4)).thenReturn(cannons1);
        when(mockNormalShip.getCannons(Direction.DOWN, 3)).thenReturn(cannons2);
        when(mockNormalShip.getCannons(Direction.DOWN, 5)).thenReturn(cannons3);

        // Test UP direction with normal ship
        heavyMeteor.direction = Direction.UP;
        List<Cannon> result = heavyMeteor.getCannons(mockNormalShip, 8);
        assertEquals(cannons1, result);

        // Test DOWN direction with normal ship
        heavyMeteor.direction = Direction.DOWN;
        result = heavyMeteor.getCannons(mockNormalShip, 8);

        // The result should contain all cannons from the three lists
        assertEquals(4, result.size());
        assertTrue(result.containsAll(cannons1));
        assertTrue(result.containsAll(cannons2));
        assertTrue(result.containsAll(cannons3));
    }
}