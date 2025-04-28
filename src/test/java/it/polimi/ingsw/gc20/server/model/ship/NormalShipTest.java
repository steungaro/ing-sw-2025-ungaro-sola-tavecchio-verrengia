package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.*;

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
        battery.setAvailableEnergy(2);

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);

        cargoHold = new CargoHold();
        cargoHold.setSlots(3);

        // Add components to ship at valid positions
        try {
            ship.addComponent(upCannon, 1, 3);
            ship.addComponent(downCannon, 3, 3);
            ship.addComponent(singleEngine, 3, 2);
            ship.addComponent(doubleEngine, 3, 4);
            ship.addComponent(battery, 2, 2);
            ship.addComponent(Cabin1, 2, 4);
            ship.addComponent(cargoHold, 1, 2);
        } catch (Exception _) {
            fail("Failed to add components to ship");
        }

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

        ship.initAstronauts();
    }

    @Test
    void ColorHostable(){
        assertEquals(AlienColor.NONE, ship.getColorHostable());
        ship.setColorHostable(AlienColor.BROWN);
        assertEquals(AlienColor.BROWN, ship.getColorHostable());
    }

    @Test
    void getFirstComponent() {
        assertEquals(cargoHold, ship.getFirstComponent(Direction.UP, 2));
        assertEquals(singleEngine, ship.getFirstComponent(Direction.DOWN, 2));
        assertEquals(Cabin1, ship.getFirstComponent(Direction.RIGHT, 2));
        assertNull(ship.getFirstComponent(Direction.UP, 6));
        assertEquals(battery, ship.getFirstComponent(Direction.LEFT, 2));
    }


    @Test
    void addBookedToWaste (){
        try {
            Cabin cabin3 = new Cabin();
            ship.addBooked(cabin3);
            assertTrue(ship.getBooked().contains(cabin3));

            Cabin cabin4 = new Cabin();
            ship.addBooked(cabin4);
            assertTrue(ship.getBooked().contains(cabin4));

            ship.addBookedToWaste();
            assertFalse(ship.getBooked().contains(cabin3));
            assertFalse(ship.getBooked().contains(cabin4));
            assertEquals(2, ship.getWaste().size());
        } catch (Exception _){
            fail("Failed to add booked components to waste");
        }
    }

    @Test
    void removeBooked() {
        Cabin cabin3 = new Cabin();
        Cabin cabin4 = new Cabin();
        Cabin cabin5 = new Cabin();
        try {

            ship.addBooked(cabin3);
            assertTrue(ship.getBooked().contains(cabin3));

            ship.addBooked(cabin4);
            assertTrue(ship.getBooked().contains(cabin4));
        } catch (Exception _){
            fail("Failed to add booked components");
        }
        try {
            ship.addBooked(cabin5);
        } catch (NoSpaceException e) {
            assertEquals("Already 2 booked components", e.getMessage());
        }
        try {
            ship.removeBooked(cabin3);
            assertFalse(ship.getBooked().contains(cabin3));
            assertTrue(ship.getBooked().contains(cabin4));
            ship.addBooked(cabin3);
            ship.removeBooked(cabin4);
        } catch (Exception _) {
            fail("Failed to remove booked components");
        }
        assertThrows (ComponentNotFoundException.class, ()-> ship.removeBooked(cabin5));
    }

    @Test
    void getComponentAt() {
        assertEquals(upCannon, ship.getComponentAt(1, 3));
        assertEquals(singleEngine, ship.getComponentAt(3, 2));
        assertEquals(doubleEngine, ship.getComponentAt(3, 4));
        assertEquals(battery, ship.getComponentAt(2, 2));
        assertEquals(Cabin1, ship.getComponentAt(2, 4));
        assertEquals(cargoHold, ship.getComponentAt(1, 2));
        assertNull(ship.getComponentAt(0, 0));
    }

    @Test
    void testFirePower() {
        try {
            Set<Cannon> cannons = new HashSet<>();
            cannons.add(downCannon);

            assertEquals(2, ship.firePower(cannons, 1));

            // Test with no cannons
            Set<Cannon> noCannons = new HashSet<>();
            assertEquals(1, ship.firePower(noCannons, 0));

            // Test with multiple cannons
            Cannon extraCannon = new Cannon();
            extraCannon.setPower(2);
            cannons.add(extraCannon);

            assertEquals(4, ship.firePower(cannons, 2));
            // Test with a purple alien
            ship.purpleAlien = true;
            assertEquals(6, ship.firePower(cannons, 2));
        } catch (Exception _){
            fail("Failed to calculate fire power");
        }
    }

    @Test
    void enginePower() {
        try {
            assertEquals(3, ship.enginePower(1));
            ship.killComponent(singleEngine);
            assertEquals(2, ship.enginePower(1));
            ship.killComponent(doubleEngine);
            assertEquals(0, ship.enginePower(0));
            ship.brownAlien = true;
            assertEquals(2, ship.enginePower(0));
        } catch (ComponentNotFoundException e){
            throw new RuntimeException();
        }
    }


    @Test
    void setComponentAt() {
        Cannon newCannon = new Cannon();
        try {
            ship.setComponentAt(newCannon, 1, 5);
        } catch (InvalidTileException _) {
            fail("Failed to set component at cannon");
        }
        assertEquals(newCannon, ship.getComponentAt(1, 5));
    }

    @Test
    void unloadCrew(){
        try {
            assertEquals(2, Cabin1.getAstronauts());
            ship.unloadCrew(Cabin1);
            assertEquals(1, Cabin1.getAstronauts());
            ship.unloadCrew(Cabin1);
            assertEquals(0, Cabin1.getAstronauts());
        } catch (EmptyCabinException _) {
            fail("Failed to unload crew");
        }
        try {
            ship.unloadCrew(Cabin1);
        } catch (EmptyCabinException e) {
            assertEquals("Empty cabin", e.getMessage());
        }
        LifeSupport ls = new LifeSupport();
        ls.setColor(AlienColor.BROWN);
        Map<Direction, ConnectorEnum> connectorsLifeSupport = new HashMap<>();
        connectorsLifeSupport.put(Direction.RIGHT, ConnectorEnum.U);
        connectorsLifeSupport.put(Direction.LEFT, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.UP, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.DOWN, ConnectorEnum.S);
        try {
            ls.setConnectors(connectorsLifeSupport);
            ship.addComponent(ls, 1, 4);
            ship.addAlien(AlienColor.BROWN, Cabin1);
            assertTrue(ship.brownAlien);
            ship.unloadCrew(Cabin1);
            assertEquals(0, Cabin1.getOccupants());
            assertFalse(ship.brownAlien);
        } catch (Exception _){
            fail("Failed to unload crew with life support");
        }

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
        try {
            ship.addComponent(lifeSupport, 2, 5);
        } catch (InvalidTileException _) {
            fail("Failed to add life support");
        }
        assertEquals(AlienColor.BROWN, Cabin1.getCabinColor());

        //Remove lifeSupport
        try {
            ship.killComponent(lifeSupport);
        } catch (ComponentNotFoundException e){
            throw new RuntimeException();
        }
        assertEquals(AlienColor.NONE, Cabin1.getCabinColor());

    }



    @Test
    void removeBookedTest() {
        try {
            Cabin cabin3 = new Cabin();
            ship.addBooked(cabin3);
            assertTrue(ship.getBooked().contains(cabin3));

            Cabin cabin4 = new Cabin();
            ship.addBooked(cabin4);
            assertTrue(ship.getBooked().contains(cabin4));

            ship.removeBooked(cabin3);
            assertFalse(ship.getBooked().contains(cabin3));
            assertTrue(ship.getBooked().contains(cabin4));
        } catch (Exception _){
            fail("Failed to remove booked components");
        }
    }

    @Test
    void getBooked() {
        try {
            Cabin cabin3 = new Cabin();
            ship.addBooked(cabin3);
            assertTrue(ship.getBooked().contains(cabin3));

            Cabin cabin4 = new Cabin();
            ship.addBooked(cabin4);
            assertTrue(ship.getBooked().contains(cabin4));

            List<Component> booked = ship.getBooked();
            assertTrue(booked.contains(cabin3));
            assertTrue(booked.contains(cabin4));
        } catch (Exception _){
            fail("Failed to get booked components");
        }
    }

    @Test
    void addBooked() {
        try {
            Cabin cabin3 = new Cabin();
            ship.addBooked(cabin3);
            assertTrue(ship.getBooked().contains(cabin3));
        } catch (Exception _){
            fail("Failed to add booked components");
        }
    }

    @Test
    void getRows() {
        assertEquals(5, ship.getRows());
    }

    @Test
    void getCols() {
        assertEquals(7, ship.getCols());
    }


    @Test
    void updateLifeSupportRemove() {
        Map<Direction, ConnectorEnum> connectorsLifeSupport = new HashMap<>();
        connectorsLifeSupport.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.LEFT, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.UP, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.DOWN, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsLifeSupport);

        // Add life support
        LifeSupport lifeSupport = new LifeSupport();
        lifeSupport.setColor(AlienColor.BROWN);
        lifeSupport.setConnectors(connectorsLifeSupport);
        try {
            ship.addComponent(lifeSupport, 2, 5);
        } catch (InvalidTileException _) {
            fail("Failed to add life support");
        }

        assertEquals(AlienColor.BROWN, Cabin1.getCabinColor());


        // Test removing life support
        try {
            ship.killComponent(lifeSupport);
        } catch (ComponentNotFoundException e){
            throw new RuntimeException();
        }
        assertEquals(AlienColor.NONE, Cabin1.getCabinColor());
        try {
            ship.addComponent(lifeSupport, 2, 5);
            ship.unloadCrew(Cabin1);
            ship.unloadCrew(Cabin1);
            ship.addAlien(AlienColor.BROWN, Cabin1);
        } catch (Exception _){
            fail("Failed to add life support");
        }
        try {
            ship.killComponent(lifeSupport);
        } catch (Exception e) {
            assertEquals("Alien died because of lack of support", e.getMessage());
        }
    }

    @Test
    void addAlien() {
        // Test adding an alien to the cabin
        Cabin1.unloadAstronaut();
        Cabin1.unloadAstronaut();

        Map<Direction, ConnectorEnum> connectorsLifeSupport = new HashMap<>();
        connectorsLifeSupport.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.LEFT, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.UP, ConnectorEnum.S);
        connectorsLifeSupport.put(Direction.DOWN, ConnectorEnum.S);
        Cabin1.setConnectors(connectorsLifeSupport);
        LifeSupport lifeSupport = new LifeSupport();
        // Add life support
        try {
            lifeSupport.setColor(AlienColor.BROWN);
            lifeSupport.setConnectors(connectorsLifeSupport);
            ship.addComponent(lifeSupport, 2, 5);

            ship.addAlien(AlienColor.BROWN, Cabin1);
            assertEquals(AlienColor.BROWN, Cabin1.getAlienColor());
            assertTrue(Cabin1.getAlien());
            ship.unloadCrew(Cabin1);
        } catch (Exception _){
            fail("Failed to add alien");
        }

        ship.brownAlien = true;
        try {
            ship.addAlien(AlienColor.BROWN, Cabin1);
        } catch (InvalidAlienPlacement e) {
            assertEquals("Brown alien already present", e.getMessage());
        }
        ship.brownAlien = false;
        ship.initAstronauts();
        try{
            ship.addAlien(AlienColor.BROWN, Cabin1);
        } catch (InvalidAlienPlacement e) {
            assertEquals("Cannot have both astronauts and aliens in the same cabin.", e.getMessage());
        }
        try {
            ship.killComponent(lifeSupport);
        } catch (ComponentNotFoundException e) {
            throw new RuntimeException();
        }
        lifeSupport.setColor(AlienColor.PURPLE);
        try {
            ship.addComponent(lifeSupport, 2, 5);
        } catch (InvalidTileException _) {
            fail("Failed to add life support");
        }
        ship.purpleAlien = true;
        assertThrows(InvalidAlienPlacement.class, () -> ship.addAlien(AlienColor.PURPLE, Cabin1));
    }

    @Test
    void testCrew(){
        try {
            assertEquals(4, ship.crew());
            ship.unloadCrew(Cabin1);
            assertEquals(3, ship.crew());
            ship.brownAlien = true;
            assertEquals(4, ship.crew());
            ship.purpleAlien = true;
            assertEquals(5, ship.crew());
        } catch (EmptyCabinException _){
            fail("Failed to unload crew");
        }
    }

    @Test
    void addComponent() {
        Cannon newCannon = new Cannon();
        try {
            ship.addComponent(newCannon, 1, 5);
        } catch (InvalidTileException e) {
            fail("Failed to add component");
        }
        assertEquals(newCannon, ship.getComponentAt(1, 5));

        // Test adding a component at an invalid position
        Exception exception = assertThrows(InvalidTileException.class, () -> ship.addComponent(newCannon, 6, 6));
        assertEquals("Invalid position", exception.getMessage());
    }
}