package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        battery.setAvailableEnergy(2);

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);

        cargoHold = new CargoHold();
        cargoHold.setSlots(3);

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
        connectorsUpCannon.put(Direction.UP, ConnectorEnum.S);
        connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U);
        upCannon.setConnectors(connectorsUpCannon);

        Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
        connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start = (StartingCabin) ship.getComponentAt(2,2);
        start.setConnectors(connectorsStartingCabin);

        ship.loadCargo(CargoColor.BLUE, cargoHold);
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
        assertNull(ship.getComponentAt(0, -1));
        assertNull(ship.getComponentAt(-1, 0));
        assertNull(ship.getComponentAt(5, 4));
        assertNull(ship.getComponentAt(4, 5));

    }

    @Test
    void setComponentAtInvalid() {
        Cannon newCannon = new Cannon();
        try {
            ship.setComponentAt(newCannon, 1, 2);
        } catch (IllegalArgumentException e) {
            assertEquals("Tile is not available", e.getMessage());
        }
        ship.setComponentAt(newCannon, -1, 0);
        ship.setComponentAt(newCannon, 0, -1);
        ship.setComponentAt(newCannon, 5, 4);
        ship.setComponentAt(newCannon, 4, 5);
    }

    @Test
    void setComponentAtValid() {
        Cannon newCannon = new Cannon();
        ship.setComponentAt(newCannon, 1, 3);
        assertEquals(newCannon, ship.getComponentAt(1, 3));
    }


    @Test
    void addComponentDoubleCannon() {
        Cannon newCannon = new Cannon();
        newCannon.setPower(2);
        newCannon.setOrientation(Direction.RIGHT);
        int initialPower = ship.doubleCannonsPower;
        ship.addComponent(newCannon, 1, 3);
        assertEquals(newCannon, ship.getComponentAt(1, 3));
        assertEquals (ship.doubleCannonsPower, initialPower+1);
    }

    @Test
    void addComponentSingleCannon() {
        Cannon newCannon = new Cannon();
        newCannon.setPower(1);
        newCannon.setOrientation(Direction.RIGHT);
        float initialPower = ship.singleCannonsPower;
        ship.addComponent(newCannon, 1, 3);
        assertEquals(newCannon, ship.getComponentAt(1, 3));
        assertEquals (ship.singleCannonsPower, initialPower+0.5f);
    }

    @Test
    void addComponentSingleEngine() {
        Engine newEngine = new Engine();
        newEngine.setDoublePower(false);
        newEngine.setOrientation(Direction.RIGHT);
        int initialPower = ship.singleEngines;
        ship.addComponent(newEngine, 1, 3);
        assertEquals(newEngine, ship.getComponentAt(1, 3));
        assertEquals (ship.singleEngines, initialPower+1);
    }

    @Test
    void addComponentDoubleEngine() {
        Engine newEngine = new Engine();
        newEngine.setDoublePower(true);
        newEngine.setOrientation(Direction.RIGHT);
        int initialPower = ship.doubleEngines;
        ship.addComponent(newEngine, 1, 3);
        assertEquals(newEngine, ship.getComponentAt(1, 3));
        assertEquals (ship.doubleEngines, initialPower+1);
    }

    @Test
    void addComponentBattery() {
        Battery newBattery = new Battery();
        newBattery.setSlots(2);
        newBattery.setAvailableEnergy(2);
        int initialPower = ship.getTotalEnergy();
        ship.addComponent(newBattery, 1, 3);
        assertEquals(newBattery, ship.getComponentAt(1, 3));
        assertEquals (ship.getTotalEnergy(), initialPower+2);
        ship.addComponent(newBattery, -1, 0);
        ship.addComponent(newBattery, 0, -1);
        ship.addComponent(newBattery, 5, 4);
        ship.addComponent(newBattery, 4, 5);
    }


    @Test
    void unloadCrew() {
        ship.initAstronauts();
        int num = ship.crew();
        int NAstro = Cabin1.getAstronauts();
        ship.unloadCrew(Cabin1);
        assertEquals(NAstro-1, Cabin1.getAstronauts());
        assertEquals (num-1, ship.crew());
        ship.unloadCrew(Cabin1);
        try {
            ship.unloadCrew(Cabin1);
        } catch (IllegalArgumentException e) {
            assertEquals("Empty cabin", e.getMessage());
        }
    }

    @Test
    void firePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);

        assertEquals(2, ship.firePower(cannons, 1));

        // Test with no cannons
        Set<Cannon> noCannons = new HashSet<>();
        assertEquals(1, ship.firePower(noCannons, 0));

        // Test with multiple cannons
        Cannon extraCannon = new Cannon();
        extraCannon.setOrientation(Direction.RIGHT);
        extraCannon.setPower(2);
        cannons.add(extraCannon);

        assertEquals(3, ship.firePower(cannons, 2));
    }

    @Test
    void enginePowerNotValid() {
        try {
            assertEquals(11, ship.enginePower(5));
        } catch (IllegalArgumentException e) {
            assertEquals("not enough double engines", e.getMessage());
        }
    }

    @Test
    void enginePowerValid() {
        assertEquals(3, ship.enginePower(1));
    }

    @Test
    void getTotalEnergy() {
        assertEquals(2, ship.getTotalEnergy());
    }

    @Test
    void crew(){
        assertEquals(0, ship.crew());
    }

    @Test
    void getAstronauts() {
        assertEquals(0, ship.getAstronauts());
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
        assertEquals(false, ship.getShield(Direction.UP));

        Shield shield = new Shield();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);

        Direction[] coveredSides = {Direction.UP, Direction.RIGHT};

        shield.setConnectors(connectors);
        shield.setCoveredSides(coveredSides);
        ship.addComponent(shield, 1, 3);

        assertEquals(true, ship.getShield(Direction.RIGHT));
        assertEquals(true, ship.getShield(Direction.UP));
        assertEquals(false, ship.getShield(Direction.DOWN));

    }

    @Test
    void getCannons () {
        List<Cannon> cannons = ship.getCannons(Direction.UP, 2);
        assertTrue(cannons.contains(upCannon));
        assertEquals(1, cannons.size());
        Cannon rightCannon = new Cannon();
        rightCannon.setOrientation(Direction.RIGHT);
        rightCannon.setPower(1);
        HashMap<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        rightCannon.setConnectors(connectors);
        ship.addComponent(rightCannon, 2, 4);
        cannons = ship.getCannons(Direction.RIGHT, 2);
        assertTrue(cannons.contains(rightCannon));
        assertEquals(1, cannons.size());
    }

    @Test
    void getAllExposed () {
        assertEquals(8, ship.getAllExposed());

    }

    @Test
    void isValid (){
        assertTrue(ship.isValid());
    }

    @Test
    void isValidInvalidConnection (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        ship.addComponent(pipe, 0, 2);
        assertFalse(ship.isValid());
    }

    @Test
    void isValidValidObstructedEngine (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        ship.addComponent(pipe, 4, 3);
        assertFalse(ship.isValid());
    }

    @Test
    void isValidValidObstructedCannon (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        ship.addComponent(pipe, 0, 2);
        assertFalse(ship.isValid());
    }

    @Test
    void isValidNotConnected(){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        ship.addComponent(pipe, 4, 4);
        assertFalse(ship.isValid());
    }

    @Test
    void waste(){
        assertEquals(0, ship.getWaste().size());
        Cannon cannon = new Cannon();
        Cannon cannon2 = new Cannon();
        ship.addToWaste(cannon);
        ship.addToWaste(cannon2);
        assertEquals(2, ship.getWaste().size());
    }

    @Test
    void killComponent(){
        assertTrue(ship.killComponent(downCannon));
        assertFalse(ship.killComponent(battery));
        assertEquals(2, ship.getWaste().size());
        assertEquals(0, ship.getTotalEnergy());
        assertEquals(0, ship.doubleCannonsPower);
    }

    @Test
    void cargo(){
        ship.unloadCargo (CargoColor.BLUE, cargoHold);
        assertEquals(0, ship.cargos.get(CargoColor.BLUE));
        ship.loadCargo(CargoColor.GREEN, cargoHold);
        ship.loadCargo(CargoColor.BLUE, cargoHold);
        assertEquals(1, ship.cargos.get(CargoColor.GREEN));
        assertEquals (1, ship.cargos.get(CargoColor.BLUE));
        ship.unloadCargo (cargoHold);
        Map<CargoColor, Integer> cargos = ship.getCargo();
        assertEquals(cargos.get(CargoColor.GREEN), ship.cargos.get(CargoColor.GREEN));
        assertEquals (cargos.get(CargoColor.BLUE), ship.cargos.get(CargoColor.BLUE));
        ship.loadCargo (CargoColor.GREEN, cargoHold);
        ship.loadCargo (CargoColor.BLUE, cargoHold);
        try {
            ship.loadCargo(CargoColor.GREEN, cargoHold);
        } catch (IllegalArgumentException e) {
            assertEquals("No available slots in cargo hold", e.getMessage());
        }
    }

    @Test
    void findComponent (){
        int[] position = ship.findComponent(upCannon);
        assertEquals(1, position[0]);
        assertEquals(2, position[1]);

        position = ship.findComponent(singleEngine);
        assertEquals(3, position[0]);
        assertEquals(1, position[1]);

        position = ship.findComponent(battery);
        assertEquals(2, position[0]);
        assertEquals(1, position[1]);

        position = ship.findComponent(Cabin1);
        assertEquals(2, position[0]);
        assertEquals(3, position[1]);

        position = ship.findComponent(cargoHold);
        assertEquals(1, position[0]);
        assertEquals(1, position[1]);
    }

    @Test
    void initAstronaut (){
        ship.initAstronauts();
        assertEquals(4, ship.crew());
    }

    @Test
    void epidemic() {
        ship.initAstronauts();
        ship.epidemic();
        assertEquals(2, ship.crew());
    }

    @Test
    void useEnergy(){
        int initialEnergy = ship.getTotalEnergy();
        ship.useEnergy(battery);
        assertEquals(initialEnergy-1, ship.getTotalEnergy());
        assertEquals(1, battery.getAvailableEnergy());
    }

    @Test
    void updateParametersRemove() {
        float singlePowerCannon = ship.singleCannonsPower;
        upCannon.updateParameter(ship, -1);
        assertEquals(singlePowerCannon-1, ship.singleCannonsPower, 0.0001f);

        float doublePowerCannon = ship.doubleCannonsPower;
        downCannon.updateParameter(ship, -1);
        assertEquals(doublePowerCannon-1, ship.doubleCannonsPower,0.0001f);

        int doubleEngines = ship.doubleEngines;
        doubleEngine.updateParameter(ship, -1);
        assertEquals(doubleEngines-1, ship.doubleEngines, 0.0001f);

        float singleEngines = ship.singleEngines;
        singleEngine.updateParameter(ship, -1);
        assertEquals(singleEngines-1, ship.singleEngines, 0.0001f);

        int totalEnergy = ship.getTotalEnergy();
        battery.updateParameter(ship, -1);
        assertEquals(totalEnergy-2, ship.getTotalEnergy());

        ship.initAstronauts();
        int astronauts = ship.astronauts;
        Cabin1.updateParameter(ship, -1);
        assertEquals(astronauts-2, ship.astronauts);
        ship.loadCargo(CargoColor.GREEN, cargoHold);
        cargoHold.updateParameter(ship, -1);
        assertEquals(0, ship.cargos.get(CargoColor.GREEN));
    }

    @Test
    void findValidOneElimination(){
        ship.killComponent(downCannon);
        ship.killComponent(battery);
        assertFalse(ship.isValid());
        ship.findValid(-1, -1);
        assertNull(ship.findComponent(singleEngine));
        assertTrue(ship.isValid());
    }

    @Test
    void findValidAllElimination(){
        ship.killComponent(downCannon);
        ship.killComponent(battery);
        assertFalse(ship.isValid());
        ship.findValid(3, 1);
        assertNull(ship.findComponent(downCannon));
        assertNull(ship.findComponent(battery));
        assertNull(ship.findComponent(cargoHold));
        assertNull(ship.findComponent(upCannon));
        assertNotNull(ship.findComponent(singleEngine));
        assertTrue(ship.isValid(3, 1));
    }
}