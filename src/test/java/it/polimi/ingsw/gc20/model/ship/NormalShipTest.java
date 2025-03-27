package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        battery.setSlots(2);
        battery.fillBattery();

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);
        Cabin1.setAstronauts(2);

        cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        cargoHold.loadCargo(CargoColor.BLUE);
        cargoHold.loadCargo(CargoColor.GREEN);

        // Add components to ship at valid positions
        ship.addComponent(upCannon, 1, 3);
        ship.addComponent(downCannon, 3, 3);
        ship.addComponent(singleEngine, 3, 2);
        ship.addComponent(doubleEngine, 3, 4);
        ship.addComponent(battery, 2, 2);
        ship.addComponent(Cabin1, 2, 4);
        ship.addComponent(cargoHold, 1, 2);

        // Setting the connectors
        Map<Direction, ConnectorEnum> connectorsCargoHold = new HashMap<>();
        connectorsCargoHold.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargoHold.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold.setConnectors(connectorsCargoHold);

        Map<Direction, ConnectorEnum> connectorsBattery = new HashMap<>();
        connectorsBattery.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsBattery.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorsBattery.put(Direction.UP, ConnectorEnum.D);
        connectorsBattery.put(Direction.DOWN, ConnectorEnum.S);
        battery.setConnectors(connectorsBattery);

        Map<Direction, ConnectorEnum> connectorsSingleEngine = new HashMap<>();
        connectorsSingleEngine.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.LEFT, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.UP, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.DOWN, ConnectorEnum.ZERO);
        singleEngine.setConnectors(connectorsSingleEngine);

        Map<Direction, ConnectorEnum> connectorsDoubleEngine = new HashMap<>();
        connectorsDoubleEngine.put(Direction.RIGHT, ConnectorEnum.D);
        connectorsDoubleEngine.put(Direction.LEFT, ConnectorEnum.S);
        connectorsDoubleEngine.put(Direction.UP, ConnectorEnum.U);
        connectorsDoubleEngine.put(Direction.DOWN, ConnectorEnum.S);
        doubleEngine.setConnectors(connectorsDoubleEngine);

        Map<Direction, ConnectorEnum> connectorsCabin1 = new HashMap<>();
        connectorsCabin1.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCabin1.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCabin1.put(Direction.UP, ConnectorEnum.S);
        connectorsCabin1.put(Direction.DOWN, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsCabin1);

        Map<Direction, ConnectorEnum> connectorsDownCannon = new HashMap<>();
        connectorsDownCannon.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsDownCannon.put(Direction.LEFT, ConnectorEnum.S);
        connectorsDownCannon.put(Direction.UP, ConnectorEnum.D);
        connectorsDownCannon.put(Direction.DOWN, ConnectorEnum.ZERO);
        downCannon.setConnectors(connectorsDownCannon);

        Map<Direction, ConnectorEnum> connectorsUpCannon = new HashMap<>();
        connectorsUpCannon.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsUpCannon.put(Direction.LEFT, ConnectorEnum.S);
        connectorsUpCannon.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U);
        upCannon.setConnectors(connectorsUpCannon);

        Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
        connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start = (StartingCabin) ship.getComponentAt(2,3);
        start.setConnectors(connectorsStartingCabin);

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
        assertEquals(7, ship.getAllExposed());
    }

    @Test
    void isValid() {
        assertTrue(ship.isValid());

        Map<Direction, ConnectorEnum> connectorsCargo = new HashMap<>();
        connectorsCargo.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargo.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargo.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargo.put(Direction.DOWN, ConnectorEnum.ZERO);
        cargoHold.setConnectors(connectorsCargo);

        assertTrue(!ship.isValid());

        connectorsCargo.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold.setConnectors(connectorsCargo);

        assertTrue(ship.isValid());

        StartingCabin start = (StartingCabin) ship.getComponentAt(2,3);
        Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
        connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.D);

        assertTrue(ship.isValid());

    }

    @Test
    void getShield(){
        assertEquals(false, ship.getShield(Direction.LEFT));
        Shield shield = new Shield();
        Direction[] coveredDirections = new Direction[4];
        coveredDirections[0] = (Direction.LEFT);
        shield.setCoveredSides(coveredDirections);

        ship.addComponent(shield, 1, 1);

        assertEquals(true, ship.getShield(Direction.LEFT));
        assertEquals(false, ship.getShield(Direction.RIGHT));
    }

    @Test
    void epidemic() {
        StartingCabin stCabin = (StartingCabin) ship.getComponentAt(2,3);
        stCabin.setAstronauts(2);

        Map<Direction, ConnectorEnum> connectorsC1 = new HashMap<>();
        connectorsC1.put(Direction.LEFT, ConnectorEnum.S);
        Map<Direction, ConnectorEnum> connectorsStart = new HashMap<>();
        connectorsStart.put(Direction.RIGHT, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsC1);
        stCabin.setConnectors(connectorsStart);

       ship.epidemic();
       assertEquals(1, Cabin1.getAstronauts());
       assertEquals(1, stCabin.getAstronauts());

    }

    @Test
    void firePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);

        assertEquals(3, ship.firePower(cannons, 1));
    }

    @Test
    void enginePower() {
        assertEquals(3, ship.enginePower(1));
        ship.killComponent(singleEngine);
        assertEquals(2, ship.enginePower(1));
        ship.killComponent(doubleEngine);
        assertEquals(0, ship.enginePower(0));
    }

    @Test
    void unloadCrew(){
        assertEquals(2, Cabin1.getAstronauts());
        ship.unloadCrew(Cabin1);
        assertEquals(1, Cabin1.getAstronauts());
        ship.unloadCrew(Cabin1);
        assertEquals(0, Cabin1.getAstronauts());
    }

    @Test
    void updateLifeSupport(){
        assertEquals(AlienColor.NONE,  Cabin1.getAlienColor());


        //Change color to Cabin
        LifeSupport lifeSupport = new LifeSupport();
        lifeSupport.setColor(AlienColor.BROWN);

        Map<Direction, ConnectorEnum> connectorsC1 = new HashMap<>();
        connectorsC1.put(Direction.RIGHT, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsC1);

        Map<Direction, ConnectorEnum> connectorsLife = new HashMap<>();
        connectorsLife.put(Direction.LEFT, ConnectorEnum.S);
        lifeSupport.setConnectors(connectorsLife);

        ship.addComponent(lifeSupport, 2, 5);
        assertEquals(AlienColor.BROWN, Cabin1.getCabinColor());

        //Remove lifeSupport
        ship.killComponent(lifeSupport);
        assertEquals(AlienColor.NONE, Cabin1.getCabinColor());

    }


    @Test
    void getColorHostable() {
    }

    @Test
    void setColorHostable() {
    }

    @Test
    void addBookedToWaste() {
    }

    @Test
    void removeBooked() {
    }

    @Test
    void getBooked() {
    }

    @Test
    void addBooked() {
    }

    @Test
    void getRows() {
    }

    @Test
    void getCols() {
    }

    @Test
    void getComponentAt() {
    }

    @Test
    void setComponentAt() {
    }

    @Test
    void testFirePower() {
    }

    @Test
    void testUnloadCrew() {
    }

    @Test
    void addAlien() {
    }

    @Test
    void addComponent() {
    }

    @Test
    void removeAlien() {
    }

    @Test
    void updateParametersRemove() {
    }
}