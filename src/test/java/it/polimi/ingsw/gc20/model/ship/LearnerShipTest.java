package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LearnerShipTest {

    private LearnerShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin Cabin1;
    private CargoHold cargoHold;

    @BeforeEach
    void setUp() {
        ship = new LearnerShip();

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
        ship.addComponent(upCannon, 1, 2);
        ship.addComponent(downCannon, 3, 2);
        ship.addComponent(singleEngine, 3, 1);
        ship.addComponent(doubleEngine, 3, 3);
        ship.addComponent(battery, 2, 1);
        ship.addComponent(Cabin1, 2, 3);
        ship.addComponent(cargoHold, 1, 1);

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
        StartingCabin start = (StartingCabin) ship.getComponentAt(2,2);
        start.setConnectors(connectorsStartingCabin);
    }

    @Test
    void getRows() {
        assertEquals(5, ship.getRows());
    }

    @Test
    void getCols() {
        assertEquals(5, ship.getCols());
    }

    @Test
    void getComponentAt() {
        assertEquals(upCannon, ship.getComponentAt(1, 2));
        assertEquals(singleEngine, ship.getComponentAt(3, 1));
        assertEquals(doubleEngine, ship.getComponentAt(3, 3));
        assertEquals(battery, ship.getComponentAt(2, 1));
        assertEquals(Cabin1, ship.getComponentAt(2, 3));
        assertEquals(cargoHold, ship.getComponentAt(1, 1));
    }

    @Test
    void setComponentAt() {
        Cannon newCannon = new Cannon();
        ship.setComponentAt(newCannon, 1, 2);
        assertEquals(newCannon, ship.getComponentAt(1, 2));
    }

    @Test
    void addComponent() {
        Cannon newCannon = new Cannon();
        ship.addComponent(newCannon, 1, 3);
        assertEquals(newCannon, ship.getComponentAt(1, 3));
    }

    @Test
    void unloadCrew() {
        int NAstro = Cabin1.getAstronauts();
        ship.unloadCrew(Cabin1);
        assertEquals(NAstro-1, Cabin1.getAstronauts());
    }

    @Test
    void firePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);

        assertEquals(3, ship.firePower(cannons, 2));
        // ?????
    }

    @Test
    void enginePower() {
        assertEquals(11, ship.enginePower(5));
    }

    @Test
    void getTotalEnergy() {
        assertEquals(2, ship.getTotalEnergy());
    }

    @Test
    void crew(){
        assertEquals(ship.astronauts, ship.crew());
    }

    @Test
    void getAstronauts() {
        assertEquals(ship.getAstronauts(), ship.getAstronauts());
    }

    @Test
    void getFirstComponent() {
        assertEquals(ship.getFirstComponent(Direction.LEFT, 1), cargoHold);
        assertEquals(ship.getFirstComponent(Direction.UP, 1), cargoHold);
        assertEquals(ship.getFirstComponent(Direction.RIGHT, 1), upCannon);
        assertEquals(ship.getFirstComponent(Direction.DOWN, 1), singleEngine);

        assertEquals(ship.getFirstComponent(Direction.LEFT, 2), battery);
    }

    @Test
    void shield(){
        assertEquals(false, ship.getShield(Direction.LEFT));
        assertEquals(true, ship.getShield(Direction.UP));

        // TO DO
    }

    @Test
    void updateParametersRemove() {
        float singlePowerCannon = ship.singleCannonsPower;
        ship.updateParametersRemove(upCannon);
        assertEquals(singlePowerCannon-1, ship.singleCannonsPower, 0.0001f);

        float doublePowerCannon = ship.doubleCannonsPower;
        ship.updateParametersRemove(downCannon);
        assertEquals(doublePowerCannon-2, ship.doubleCannonsPower,0.0001f);

        // TO DO
    }
}