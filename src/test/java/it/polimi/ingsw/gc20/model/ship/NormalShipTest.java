package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NormalShipTest {

    private NormalShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin Cabin1;
    private CargoHold cargoHold;

    @BeforeEach
    void setUp() {
        // Create a new NormalShip
        ship = new NormalShip();

        // Create components
        upCannon = new Cannon();
        upCannon.setOrientation(Direction.UP);
        upCannon.setPower(1);

        downCannon = new Cannon();
        downCannon.setOrientation(Direction.DOWN);
        downCannon.setPower(2);

        singleEngine = new Engine();
        singleEngine.setDoublePower(false);

        doubleEngine = new Engine();
        doubleEngine.setDoublePower(true);

        battery = new Battery();
        battery.getEnergy().add(new Energy());
        battery.getEnergy().add(new Energy());

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);
        Cabin1.getAstronauts().add(new Astronaut());

        cargoHold = new CargoHold();
        Cargo c1 = new Cargo(CargoColor.BLUE);
        Cargo c2 = new Cargo(CargoColor.GREEN);
        cargoHold.loadCargo(c1);
        cargoHold.loadCargo(c2);

        // Add components to ship at valid positions
        ship.addComponent(upCannon, 1, 3);
        ship.addComponent(downCannon, 3, 3);
        ship.addComponent(singleEngine, 3, 2);
        ship.addComponent(doubleEngine, 3, 4);
        ship.addComponent(battery, 2, 2);
        ship.addComponent(Cabin1, 2, 4);
        ship.addComponent(cargoHold, 1, 2);
    }

    @Test
    void getFirstComponent() {
        assertEquals(cargoHold, ship.getFirstComponent(Direction.UP, 2));
        assertEquals(singleEngine, ship.getFirstComponent(Direction.DOWN, 2));
        assertEquals(Cabin1, ship.getFirstComponent(Direction.RIGHT, 2));
        //assertEquals(null, ship.getFirstComponent(Direction.UP, 6));
        assertEquals(battery, ship.getFirstComponent(Direction.LEFT, 2));
    }

    @Test
    void getShield() {
    }

    @Test
    void getCannons() {
        // Test getting UP cannons at column 3
        List<Cannon> upCannons = ship.getCannons(Direction.UP, 3);
        assertTrue(upCannons.contains(upCannon), "The list should contain upCannon");
        assertEquals(1, upCannons.size(), "The list should contain exactly one cannon");

        // Test getting DOWN cannons at column 3
        List<Cannon> downCannons = ship.getCannons(Direction.DOWN, 3);
        assertTrue(downCannons.contains(downCannon), "The list should contain downCannon");
        assertEquals(1, downCannons.size(), "The list should contain exactly one cannon");

        // Test getting cannons in a direction with no cannons
        List<Cannon> leftCannons = ship.getCannons(Direction.LEFT, 3);
        assertTrue(leftCannons.isEmpty(), "The list should be empty");
    }

    @Test
    void getAllExposed() {
    }

    @Test
    void isValid() {
    }

    @Test
    void epidemic() {
        Astronaut a = new Astronaut();
        Astronaut b = new Astronaut();
        Cabin1.setAstronauts(List.of(a));
        StartingCabin stCabin = (StartingCabin) ship.getComponentAt(2,3);
        stCabin.setAstronauts(List.of(b));

        Map<Direction, ConnectorEnum> connectorsC1 = new HashMap<>();
        connectorsC1.put(Direction.LEFT, ConnectorEnum.S);
        Map<Direction, ConnectorEnum> connectorsStart = new HashMap<>();
        connectorsStart.put(Direction.RIGHT, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsC1);
        stCabin.setConnectors(connectorsStart);

       ship.epidemic();
       assertEquals(0, Cabin1.getAstronauts().size());
       assertEquals(0, stCabin.getAstronauts().size());

    }

    @Test
    void firePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);

        assertEquals(3, ship.firePower(cannons, 1));
    }
}