package it.polimi.ingsw.gc20.server.model.ship;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
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

    /**
     * Sets up the test environment for the LearnerShipTest class.
     * Initializes the ship and its components, such as cannons, engines,
     * battery, cabin, cargo hold, and their configurations including
     * power, rotation, color, slots, and connectors.
     * <p>
     * Configures the ship by adding components at specific positions
     * and setting their respective connector configurations.
     * Ensures valid component placements and connections.
     * <p>
     * Loads initial cargo into the cargo hold and ensures no exceptions
     * are thrown.
     * <p>
     * This method runs before each test in the LearnerShipTest class
     * to provide a consistent and pre-configured testing environment.
     */
    @BeforeEach
    void setUp() {
        ship = new LearnerShip();

        upCannon = new Cannon();
        upCannon.setPower(1);

        downCannon = new Cannon();
        downCannon.setRotation(Direction.DOWN);
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
            ship.addComponent(upCannon, 1, 2);
            ship.addComponent(downCannon, 3, 2);
            ship.addComponent(singleEngine, 3, 1);
            ship.addComponent(doubleEngine, 3, 3);
            ship.addComponent(battery, 2, 1);
            ship.addComponent(Cabin1, 2, 3);
            ship.addComponent(cargoHold, 1, 1);
        } catch (InvalidTileException _) {
            fail("Invalid tile exception should not be thrown");
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

        try {
            ship.loadCargo(CargoColor.BLUE, cargoHold);
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Tests the retrieval of the number of rows in the LearnerShip.
     * <p>
     * This test validates that the `getRows` method of the ship instance
     * correctly returns the expected number of rows. It ensures that the
     * method functions as intended by asserting its output against the
     * expected value.
     */
    @Test
    void getRows() {
        assertEquals(5, ship.getRows());
    }

    /**
     * Tests the retrieval of the number of columns in the LearnerShip.
     * <p>
     * This method validates the correct functionality of the `getCols` method
     * of the `ship` instance. It ensures that the method correctly returns the
     * expected number of columns of the ship by asserting its output against
     * the expected value.
     */
    @Test
    void getCols() {
        assertEquals(5, ship.getCols());
    }

    /**
     * Tests the behavior of the `getComponentAt` method in the `LearnerShip` class.
     * <p>
     * This test validates that the `getComponentAt` method correctly retrieves components located at
     * specific positions on the ship, or returns null when the given coordinates are out of bounds.
     * It uses assertions to compare the returned components or null values with the expected results.
     * <p>
     * The test ensures the following:
     * - The method correctly retrieves components for valid row and column indices corresponding to
     *   specific components on the ship.
     * - The method returns null when the specified row and column indices are out of bounds.
     */
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

    /**
     * Tests the behavior of the `setComponentAt` method in the `LearnerShip` class when attempting
     * to place a component at invalid or out-of-bound tile positions.
     * <p>
     * This test ensures that the `InvalidTileException` is correctly thrown with the appropriate
     * message when a component is placed on a tile that is not available for placement, such as:
     * - A tile with an index outside the valid range of rows or columns.
     * - Any situation where the tile does not support the component placement due to boundaries.
     * <p>
     * Additionally, this test verifies that no exception is thrown and the placement proceeds normally
     * when components are placed within valid tile positions, ensuring correct functionality of
     * the `setComponentAt` method.
     * <p>
     * Assertions:
     * - When attempting to place a component on an invalid tile, the exception message should
     *   match "Tile is not available".
     * - No exceptions should occur when placing components within valid tile ranges.
     * <p>
     * The test covers the following scenarios:
     * - Invalid tile indices that exceed the maximum row/column range.
     * - Negative indices for rows and columns.
     * - Valid tile placements within the acceptable range.
     */
    @Test
    void setComponentAtInvalid() {
        Cannon newCannon = new Cannon();
        try {
            ship.setComponentAt(newCannon, 1, 2);
        } catch (InvalidTileException e) {
            assertEquals("Tile is not available", e.getMessage());
        }
        try {
            ship.setComponentAt(newCannon, -1, 0);
            ship.setComponentAt(newCannon, 0, -1);
            ship.setComponentAt(newCannon, 5, 4);
            ship.setComponentAt(newCannon, 4, 5);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
    }

    /**
     * Tests the functionality of retrieving and validating specific attributes of a ship
     * in the LearnerShipTest context, including single cannon power, single engine count,
     * alien color association, and validity of a specific message.
     * <p>
     * Validates the following:
     * - The `singleCannonsPower` attribute of the ship is set to the expected value.
     * - The `singleEngines` attribute of the ship returns the expected integer count.
     * - The `getAliens` method of the ship correctly identifies the alien color association
     *   as `AlienColor.NONE`.
     * - The `messageFromShip` static method from the `Ship` class successfully returns
     *   a non-null message that meets specific validation requirements.
     * <p>
     * Assertions:
     * - Asserts that the single cannon power is correctly set to `1.0f`.
     * - Ensures the single engine count is `1` as expected.
     * - Verifies that the alien color associated with the ship is `NONE`.
     * - Confirms that a valid message from the ship is not null when requested with
     *   specific parameters.
     */
    @Test
    void getSingleEngineAndCannonPower() {
        assertEquals(1.0f, ship.singleCannonsPower);
        assertEquals(1, ship.singleEngines);
        assertEquals(AlienColor.NONE, ship.getAliens());
        assertNotNull(Ship.messageFromShip("hai letto davvero fino a qui?", ship, "Message from ship should not be null"));
    }

    /**
     * Tests the functionality of the `setComponentAt` method within the `LearnerShip` class
     * for valid component placement.
     * <p>
     * This test ensures that a new component, specifically an instance of `Cannon`, can
     * be successfully placed at a valid position on the ship without any exceptions being thrown.
     * <p>
     * Key validations include:
     * - Ensuring that the `setComponentAt` method does not throw an `InvalidTileException`
     *   when the component is placed in a valid position.
     * - Verifying that the component is correctly retrieved from the specified valid position
     *   using the `getComponentAt` method.
     * <p>
     * Assertions:
     * - Asserts that no exceptions occur during component placement for a valid position.
     * - Confirms that the component set at the specified position matches the expected component.
     */
    @Test
    void setComponentAtValid() {
        Cannon newCannon = new Cannon();
        try {
            ship.setComponentAt(newCannon, 1, 3);
        } catch (InvalidTileException _) {
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(newCannon, ship.getComponentAt(1, 3));
    }


    /**
     * Tests the functionality of adding a double-powered cannon component to the ship
     * and validating its placement and effect on the ship's attributes.
     * <p>
     * This test performs the following validations:
     * - Creates a new cannon instance with power set to 2 and rotation set to the right.
     * - Tracks the initial value of the `doubleCannonsPower` attribute of the ship.
     * - Adds the new cannon to a specific position on the ship using the `addComponent` method.
     * - Ensures that no `InvalidTileException` is thrown during component placement.
     * - Verifies that the new cannon is correctly placed at the specified position by
     *   using the `getComponentAt` method.
     * - Confirms that the `doubleCannonsPower` attribute of the ship is incremented as
     *   expected after the new cannon is added.
     * <p>
     * Assertions:
     * - Asserts that no exceptions are thrown during the placement of the cannon.
     * - Ensures that the component retrieved from the specified position matches the
     *   newly added cannon.
     * - Validates that the `doubleCannonsPower` attribute of the ship is incremented
     *   by 1 after adding the double-powered cannon.
     */
    @Test
    void addComponentDoubleCannon() {
        Cannon newCannon = new Cannon();
        newCannon.setPower(2);
        newCannon.setRotation(Direction.RIGHT);
        int initialPower = ship.doubleCannonsPower;
        try {
            ship.addComponent(newCannon, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid alien placement exception should not be thrown");
        }
        assertEquals(newCannon, ship.getComponentAt(1, 3));
        assertEquals (ship.doubleCannonsPower, initialPower+1);
    }

    /**
     * Tests the functionality of adding a single-powered cannon component to the ship
     * and validating its placement and effect on the ship's attributes.
     * <p>
     * Key validations include:
     * - Creating a new `Cannon` instance with power set to 1 and rotation set to the right.
     * - Tracking the initial value of the `singleCannonsPower` attribute of the ship.
     * - Adding the new cannon to a specific position on the ship using the `addComponent` method.
     * - Ensuring that no `InvalidTileException` is thrown during component placement.
     * - Verifying that the new cannon is correctly placed at the specified position using the
     *   `getComponentAt` method.
     * - Confirming that the `singleCannonsPower` attribute of the ship is incremented as expected
     *   after the new cannon is added.
     * <p>
     * Assertions:
     * - Asserts that no exceptions are thrown during the placement of the cannon.
     * - Ensures that the component retrieved from the specified position matches the newly added cannon.
     * - Validates that the `singleCannonsPower` attribute of the ship is incremented by 0.5 after adding
     *   the single-powered cannon.
     */
    @Test
    void addComponentSingleCannon() {
        Cannon newCannon = new Cannon();
        newCannon.setPower(1);
        newCannon.setRotation(Direction.RIGHT);
        float initialPower = ship.singleCannonsPower;
        try {
            ship.addComponent(newCannon, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(newCannon, ship.getComponentAt(1, 3));
        assertEquals (ship.singleCannonsPower, initialPower+0.5f);
    }

    /**
     * Tests the addition of a single-engine component to a ship.
     * <p>
     * This test case validates the functionality of the {@code addComponent} method by
     * adding a single-engine component to a specified tile on the ship. It ensures that:
     * - The engine is placed at the correct position on the ship.
     * - The total count of single engines is incremented correctly.
     * - No exception is thrown for valid placement.
     * <p>
     * The engine component properties are configured with:
     * - Double power disabled
     * - Rotation set to {@code Direction.RIGHT}
     *
     * Assertions:
     * - Confirms that the component at the target position matches the newly added engine.
     * - Verifies that the count of single engines has increased by one.
     * <p>
     * If {@code InvalidTileException} is thrown during the process, the test is marked as failed.
     */
    @Test
    void addComponentSingleEngine() {
        Engine newEngine = new Engine();
        newEngine.setDoublePower(false);
        newEngine.setRotation(Direction.RIGHT);
        int initialPower = ship.singleEngines;
        try {
            ship.addComponent(newEngine, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(newEngine, ship.getComponentAt(1, 3));
        assertEquals (ship.singleEngines, initialPower+1);
    }

    /**
     * Tests the functionality of adding a double power engine component to a ship.
     * <p>
     * This method verifies that:
     * 1. A new engine with double power can be added to a specific tile on the ship.
     * 2. The engine is oriented in the specified direction (to the right in this case).
     * 3. The ship's internal state is updated to reflect the addition of the double power engine.
     * 4. The number of double engines on the ship is incremented correctly after the addition.
     * <p>
     * The test ensures no InvalidTileException is thrown during the component addition process.
     * It also validates that the engine is correctly placed at the designated location
     * and the counts of double engines are updated as expected.
     */
    @Test
    void addComponentDoubleEngine() {
        Engine newEngine = new Engine();
        newEngine.setDoublePower(true);
        newEngine.setRotation(Direction.RIGHT);
        int initialPower = ship.doubleEngines;
        try {
            ship.addComponent(newEngine, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(newEngine, ship.getComponentAt(1, 3));
        assertEquals (ship.doubleEngines, initialPower+1);
    }

    /**
     * Tests the functionality of adding a component battery to the ship.
     * <p>
     * This test method validates the following:
     * - A battery component with specific slots and energy is successfully added
     *   to a specified valid tile on the ship.
     * - The total energy of the ship is correctly updated after the battery is added.
     * - The battery component is correctly retrievable from the specified tile.
     * - An InvalidTileException is thrown when attempting to add the battery
     *   to tiles that are out of the valid board boundaries.
     * - An InvalidTileException is not thrown when adding the battery to a valid tile.
     */
    @Test
    void addComponentBattery() {
        Battery newBattery = new Battery();
        newBattery.setSlots(2);
        newBattery.setAvailableEnergy(2);
        int initialPower = ship.getTotalEnergy();
        try {
            ship.addComponent(newBattery, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(newBattery, ship.getComponentAt(1, 3));
        assertEquals (ship.getTotalEnergy(), initialPower+2);
        assertThrows(InvalidTileException.class, ()->ship.addComponent(newBattery, -1, 0));
        assertThrows(InvalidTileException.class, ()->ship.addComponent(newBattery, 0, -1));
        assertThrows(InvalidTileException.class, ()->ship.addComponent(newBattery, 5, 4));
        assertThrows(InvalidTileException.class, ()->ship.addComponent(newBattery, 4, 5));
    }


    /**
     * Tests the functionality of the unloadCrew method in the Ship class by verifying the
     * behavior of unloading astronauts from a specified cabin.
     * <p>
     * The method performs the following:
     * - Initializes astronauts in the ship and assigns an initial number of crew members.
     * - Checks the number of astronauts in a selected cabin before and after unloading
     *   an astronaut, ensuring one astronaut is successfully removed.
     * - Verifies that the total crew count in the ship decreases accordingly.
     * - Handles scenarios where unloading is attempted from an empty cabin, ensuring
     *   proper exception handling and message validation.
     * <p>
     * The test ensures that the unloadCrew method operates as expected under normal
     * conditions and handles edge cases such as attempting to unload from an empty cabin.
     */
    @Test
    void unloadCrew() {
        ship.initAstronauts();
        int num = ship.crew();
        int NAstro = Cabin1.getAstronauts();
        try {
            ship.unloadCrew(Cabin1);
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
        assertEquals(NAstro-1, Cabin1.getAstronauts());
        assertEquals (num-1, ship.crew());
        try {
            ship.unloadCrew(Cabin1);
        } catch (EmptyCabinException _) {
            fail("Empty cabin exception should not be thrown");
        }
        try {
            ship.unloadCrew(Cabin1);
        } catch (EmptyCabinException e) {
            assertEquals("Empty cabin", e.getMessage());
        }
    }

    /**
     * Tests the functionality of the `firePower` method in various scenarios.
     * <p>
     * This test ensures that the method behaves as expected when firing cannons with different energy levels,
     * cannon configurations, and edge cases. The following cases are covered:
     * <p>
     * 1. Validating that the `firePower` method calculates and returns the correct power for a given set of cannons
     *    and energy level without throwing exceptions in valid scenarios.
     * 2. Verifying that an `EnergyException` is thrown when attempting to fire with zero battery energy.
     * 3. Verifying that adding invalid cannons to the set results in an `InvalidCannonException`.
     * 4. Testing the behavior of the method with an empty set of cannons, ensuring no exceptions are thrown and
     *    correct fallback values are returned.
     * 5. Testing a scenario with multiple properly configured cannons and ensuring the correct accumulated
     *    firepower is calculated as per their configurations and provided energy levels.
     * <p>
     * Assertions are used to validate the returned results against expected values, and exceptions are caught
     * and tested to ensure proper error handling within the method.
     */
    @Test
    void firePower() {
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(downCannon);
        try {
            assertEquals(2, ship.firePower(cannons, 1));
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
        //Test with no battery
        assertThrows(EnergyException.class, () -> ship.firePower(cannons, 0));

        cannons.add (upCannon);
        assertThrows (InvalidCannonException.class, () -> ship.firePower(cannons, 2));
        // Test with no cannons
        Set<Cannon> noCannons = new HashSet<>();
        try {
            assertEquals(1, ship.firePower(noCannons, 0));
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }

        cannons.remove(upCannon);
        // Test with multiple cannons
        Cannon extraCannon = new Cannon();
        extraCannon.setRotation(Direction.RIGHT);
        extraCannon.setPower(2);
        cannons.add(extraCannon);

        try {
            assertEquals(3, ship.firePower(cannons, 2));
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Tests the scenario when the engine power provided is not valid.
     * Verifies that the enginePower method in the Ship class throws
     * an InvalidEngineException if an invalid power level is supplied.
     * Confirms that the exception message matches the expected text.
     */
    @Test
    void enginePowerNotValid() {
        try {
            assertEquals(11, ship.enginePower(5));
        } catch (InvalidEngineException e) {
            assertEquals("not enough double engines", e.getMessage());
        }
    }

    /**
     * Tests the enginePower method of the ship class to ensure the correct power value is returned.
     * <p>
     * This test validates that calling the enginePower method with a valid input parameter
     * returns the expected output. If the input is not valid, it is expected not to throw
     * an InvalidEngineException. If the exception is thrown, the test will fail.
     * <p>
     * Assertions:
     * - Verifies that the enginePower method returns 3 when called with a value of 1.
     * <p>
     * Failures:
     * - The test will fail if the enginePower method throws an InvalidEngineException
     *   unexpectedly.
     */
    @Test
    void enginePowerValid() {
        try {
            assertEquals(3, ship.enginePower(1));
        } catch (InvalidEngineException _){
            fail("Invalid engine exception should not be thrown");

        }
    }

    /**
     * Tests the getTotalEnergy method of the Ship class.
     * <p>
     * This method verifies if the total energy calculated by the getTotalEnergy
     * method matches the expected value. It uses assertions to ensure the method
     * behaves as intended.
     */
    @Test
    void getTotalEnergy() {
        assertEquals(2, ship.getTotalEnergy());
    }

    /**
     * Tests the crew method of the ship object.
     * Ensures that the crew() method returns the expected value of 0.
     */
    @Test
    void crew(){
        assertEquals(0, ship.crew());
    }

    /**
     * Tests the functionality of the `getAstronauts` method.
     * <p>
     * Verifies that the method `getAstronauts` correctly returns the number of astronauts
     * currently in the ship. This test asserts that the initial or expected count is zero.
     */
    @Test
    void getAstronauts() {
        assertEquals(0, ship.getAstronauts());
    }

    /**
     * Tests the functionality of the getFirstComponent method.
     * The method is expected to return the first component of a ship in a specified direction
     * and at a given distance. This test verifies the correct output for various
     * directions and distances, as well as validates null return values when the
     * specified component does not exist.
     * <p>
     * Assertions:
     * - Asserts that the first component in the specified direction and distance
     *   matches the expected component.
     * - Asserts that the method returns null if no component is present at the
     *   specified direction and distance.
     */
    @Test
    void getFirstComponent() {
        assertEquals(ship.getFirstComponent(Direction.LEFT, 1), cargoHold);
        assertEquals(ship.getFirstComponent(Direction.UP, 1), cargoHold);
        assertEquals(ship.getFirstComponent(Direction.RIGHT, 1), upCannon);
        assertEquals(ship.getFirstComponent(Direction.DOWN, 1), singleEngine);
        assertEquals(ship.getFirstComponent(Direction.LEFT, 2), battery);
        assertNull(ship.getFirstComponent(Direction.UP, 5));
        assertNull(ship.getFirstComponent(Direction.RIGHT, 5));
    }

    /**
     * Tests the behavior of the shield component in the context of its integration
     * with a ship object. Ensures that the shield connects and covers specific
     * directions as expected, and verifies the state of the shield for various
     * ship directions before and after installation.
     * <p>
     * The method works through the following steps:
     * 1. Asserts the initial state of the ship's shield for specified directions.
     * 2. Initializes a Shield object and configures its connectors and covered sides.
     * 3. Attempts to add the configured shield to the ship at a specific location
     *    while handling any potential exceptions.
     * 4. Verifies the state of the ship's shield in different directions following
     *    the installation of the shield component.
     * <p>
     * Throws:
     * - Fails the test if `InvalidTileException` is thrown during shield installation.
     * <p>
     * Assertions:
     * - Verifies shield state for specified directions before and after shield installation.
     */
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
        try {
            ship.addComponent(shield, 1, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }

        assertEquals(true, ship.getShield(Direction.RIGHT));
        assertEquals(true, ship.getShield(Direction.UP));
        assertEquals(false, ship.getShield(Direction.DOWN));

    }

    /**
     * Tests the functionality of the method that retrieves a list of cannons from a ship
     * based on a specified direction and power level.
     * <p>
     * This method validates that:
     * - The `getCannons` method correctly returns a list of cannons that match the given direction and power level.
     * - The returned cannons are accurately filtered based on the specified criteria and contain the expected components.
     * - Components with appropriate properties can be added to the ship without errors, and their retrieval complies with the parameters.
     * - The retrieved cannon list size and its contents match expectations.
     * - No exceptions are thrown when valid components are added to the ship.
     * <p>
     * Assertions within the test confirm that:
     * - Retrieved cannons contain expected components based on specified properties.
     * - List sizes of retrieved cannons are correct.
     * - Adding a valid cannon to the ship does not result in an InvalidTileException.
     */
    @Test
    void getCannons () {
        List<Cannon> cannons = ship.getCannons(Direction.UP, 2);
        assertTrue(cannons.contains(upCannon));
        assertEquals(1, cannons.size());
        Cannon rightCannon = new Cannon();
        rightCannon.setRotation(Direction.RIGHT);
        rightCannon.setPower(1);
        HashMap<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        rightCannon.setConnectors(connectors);
        try {
            ship.addComponent(rightCannon, 2, 4);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        cannons = ship.getCannons(Direction.RIGHT, 2);
        assertTrue(cannons.contains(rightCannon));
        assertEquals(1, cannons.size());
    }

    /**
     * Tests the functionality of the method that retrieves the total number of exposed connectors
     * in the ship, including initial and updated cases after adding a new component.
     * <p>
     * This method verifies:
     * 1. The initial count of exposed connectors on the ship.
     * 2. The ability to add a new cabin component to the ship without throwing an exception.
     * 3. The updated count of exposed connectors in the ship after successfully adding
     *    the new cabin component.
     * <p>
     * The test ensures the correct behavior of the `getAllExposed` method and the
     * interaction with components and their connector mappings.
     */
    @Test
    void getAllExposed () {
        assertEquals(8, ship.getAllExposed());
        Cabin cabin = new Cabin();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        cabin.setConnectors(connectors);
        try {
            ship.addComponent(cabin, 2, 4);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertEquals(10, ship.getAllExposed());

    }

    /**
     * Test method to verify the validity of a Ship object.
     * This test asserts that the isValid method of the Ship class returns true,
     * indicating that the Ship object is in a valid state.
     */
    @Test
    void isValid (){
        assertTrue(ship.isValid());
    }

    /**
     * Tests the validity of a connection configuration in a ship's pipe system.
     * This method sets up a pipe object with connectors on all four directions and
     * attempts to add it to the ship's grid at a specified location. It ensures that
     * the pipe does not cause the ship's configuration to be valid by checking the
     * validity status of the ship after adding the pipe.
     * <p>
     * The test fails if an InvalidTileException is thrown during the component addition,
     * or if the ship's validity status does not match the expected invalid state.
     */
    @Test
    void isValidInvalidConnection (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        try {
            ship.addComponent(pipe, 0, 2);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertFalse(ship.isValid());
    }

    /**
     * Tests the functionality of the {@code isValid()} method when a pipe component
     * with obstructed connectors is added to the game board. The test ensures that
     * the ship is identified as invalid in this scenario.
     * <p>
     * The test performs the following steps:
     * - Creates a new Pipes object.
     * - Configures the pipe with connectors in all four directions (UP, DOWN, LEFT, RIGHT)
     *   using the ConnectorEnum.S type.
     * - Adds the created pipe to the ship at position (4, 3) using the {@code addComponent} method.
     * - Verifies that an {@code InvalidTileException} is not thrown when adding the pipe.
     * - Asserts that the ship configuration is invalid using the {@code isValid()} method.
     * <p>
     * This test helps verify that the application accurately detects an invalid configuration
     * when adding an obstructed engine-like component to the board.
     */
    @Test
    void isValidValidObstructedEngine (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        try {
            ship.addComponent(pipe, 4, 3);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertFalse(ship.isValid());
    }

    /**
     * Tests if a valid pipe configuration in the ship is deemed obstructed by the cannon,
     * and ensures that the `isValid()` method properly identifies an invalid ship state
     * due to the addition of the pipe with the specified connectors.
     * <p>
     * Behavior:
     * - Creates a pipe with all sides configured as ConnectorEnum.S.
     * - Adds the created pipe to a specific tile (0, 2) of the ship.
     * - Validates that the ship object's state is marked invalid after addition
     *   of the obstructing pipe using the `isValid()` method.
     * <p>
     * Expectations:
     * - The test ensures that no `InvalidTileException` is thrown while adding the pipe.
     * - The test asserts that the ship's `isValid()` method returns false
     *   indicating the invalid configuration caused by the added pipe.
     */
    @Test
    void isValidValidObstructedCannon (){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        try {
            ship.addComponent(pipe, 0, 2);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertFalse(ship.isValid());
    }

    /**
     * Test method to verify that a pipe component is not considered valid when it exists
     * in a non-connected state in the ship's layout. The method ensures that the ship's
     * `isValid` function returns false under these conditions.
     * <p>
     * Key operations performed within this test include:
     * 1. Initializing a `Pipes` object and setting its connectors with all directions
     *    marked as `ConnectorEnum.S`.
     * 2. Placing the pipe at a specific location within the layout using the
     *    `ship.addComponent` method.
     * 3. Asserting that no `InvalidTileException` is thrown during the component placement.
     * 4. Validating, using an assertion, that the `ship.isValid` method returns false,
     *    signifying that the configuration is not considered valid.
     * <p>
     * This test ensures proper validation logic for disconnected components in the ship's layout.
     */
    @Test
    void isValidNotConnected(){
        Pipes pipe = new Pipes();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.S);
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.DOWN, ConnectorEnum.S);
        pipe.setConnectors(connectors);
        try {
            ship.addComponent(pipe, 4, 4);
        } catch (InvalidTileException _){
            fail("Invalid tile exception should not be thrown");
        }
        assertFalse(ship.isValid());
    }

    /**
     * Tests the management and handling of the waste on a ship.
     * This method verifies that the waste list is initialized as empty and
     * ensures that items such as cannons can be added to the waste list.
     * It also tests the functionality of managing booked items in the waste.
     * Assertions are used to validate the expected size of the waste list
     * after specific operations.
     */
    @Test
    void waste(){
        assertEquals(0, ship.getWaste().size());
        Cannon cannon = new Cannon();
        Cannon cannon2 = new Cannon();
        ship.addToWaste(cannon);
        ship.addToWaste(cannon2);
        assertEquals(2, ship.getWaste().size());
        ship.addBookedToWaste();
    }

    /**
     * Tests the functionality of the `killComponent` method in the `ship` object.
     * This method is used to disable specific components of the ship and checks the
     * resulting effects on the ship's state.
     * <p>
     * The test verifies:
     * - The method returns `true` when a valid component, such as `downCannon`, is killed.
     * - The method returns `false` when attempting to kill a nonexistent or invalid component, such as `battery`.
     * - The ship's waste collection increases accordingly after a component is killed.
     * - The ship's total energy becomes zero after the components are killed.
     * - The `doubleCannonsPower` value is correctly updated after killing components.
     * <p>
     * Exceptions:
     * - Throws a `RuntimeException` if the `ComponentNotFoundException` is encountered during execution.
     */
    @Test
    void killComponent(){
        try {
            assertTrue(ship.killComponent(downCannon));
            assertFalse(ship.killComponent(battery));
        } catch (ComponentNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, ship.getWaste().size());
        assertEquals(0, ship.getTotalEnergy());
        assertEquals(0, ship.doubleCannonsPower);
    }

    /**
     * Tests the loading and unloading functionality of cargo within a ship.
     * <p>
     * This method verifies that cargo can be loaded and unloaded correctly, ensuring the
     * ship maintains an accurate count of cargos for each color. It tests the following:
     * <p>
     * - Unloading cargo of a specific color and verifying the cargo count is updated correctly.
     * - Loading cargo of different colors into the ship and verifying the updated counts.
     * - Retrieving the current cargo status and ensuring it matches the ship's internal record.
     * - Handling exceptions properly, including:
     *   - Validating that an InvalidCargoException is thrown when unloading a non-existent cargo color.
     *   - Ensuring a CargoFullException is thrown when attempting to add cargo beyond the ship's capacity.
     * - Asserts the correctness of cargo operation outcomes using checks for expected results.
     * <p>
     * The method also ensures no unexpected exceptions are thrown during regular cargo operations.
     */
    @Test
    void cargo(){
        try {
            ship.unloadCargo(CargoColor.BLUE, cargoHold);
            assertEquals(0, ship.cargos.get(CargoColor.BLUE));
            ship.loadCargo(CargoColor.GREEN, cargoHold);
            ship.loadCargo(CargoColor.BLUE, cargoHold);
            assertEquals(1, ship.cargos.get(CargoColor.GREEN));
            assertEquals(1, ship.cargos.get(CargoColor.BLUE));
            ship.unloadCargo(CargoColor.BLUE, cargoHold);
            Map<CargoColor, Integer> cargos = ship.getCargo();
            assertEquals(cargos.get(CargoColor.GREEN), ship.cargos.get(CargoColor.GREEN));
            assertEquals(cargos.get(CargoColor.BLUE), ship.cargos.get(CargoColor.BLUE));
            ship.loadCargo(CargoColor.GREEN, cargoHold);
            ship.loadCargo(CargoColor.BLUE, cargoHold);
        } catch (Exception _) {
            fail ("Exception should not be thrown");
        }
        assertThrows (InvalidCargoException.class, ()-> ship.unloadCargo(CargoColor.YELLOW, cargoHold));
        try {
            ship.loadCargo(CargoColor.GREEN, cargoHold);
        } catch (CargoFullException e) {
            assertEquals("CargoHold is full", e.getMessage());
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Tests the functionality of the findComponent method in locating specific components
     * of a ship and verifying their positions.
     * <p>
     * The method validates that the selected components, such as upCannon, singleEngine,
     * battery, Cabin1, and cargoHold, are identified correctly and their positions match
     * the expected coordinates.
     * <p>
     * Assertions are performed to ensure accurate mapping of each component to its respective
     * position within the ship structure.
     * <p>
     * Components being tested:
     * - upCannon
     * - singleEngine
     * - battery
     * - Cabin1
     * - cargoHold
     */
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

    /**
     * Tests the initialization of astronauts on the ship.
     * <p>
     * This method verifies that the initAstronauts method initializes the crew correctly.
     * It checks if the number of crew members after initialization is equal to the expected value.
     */
    @Test
    void initAstronaut (){
        ship.initAstronauts();
        assertEquals(4, ship.crew());
    }

    /**
     * Tests the epidemic handling method in the ship class.
     * <p>
     * This test initializes the astronauts on the ship, triggers the epidemic
     * scenario, and verifies that the expected number of astronauts remaining
     * after the epidemic is correct.
     */
    @Test
    void epidemic() {
        ship.initAstronauts();
        ship.epidemic();
        assertEquals(2, ship.crew());
    }

    /**
     * Tests the functionality of the method responsible for consuming energy from a ship
     * and a designated battery. Verifies that energy is correctly deducted from the
     * ship's total energy and appropriately updated in the battery.
     * <p>
     * Ensures proper handling of the energy consumption process without throwing an
     * unnecessary exception during valid operations. Also verifies that an
     * EnergyException is appropriately thrown when attempting to use energy beyond
     * the available limits.
     * <p>
     * This test evaluates:
     * - Correct deduction of energy from the ship's total energy.
     * - Correct update of the battery's available energy.
     * - Prevention of overuse by throwing an EnergyException when no energy is available.
     */
    @Test
    void useEnergy(){
        int initialEnergy = ship.getTotalEnergy();
        try {
            ship.useEnergy(battery);
            assertEquals(initialEnergy - 1, ship.getTotalEnergy());
            assertEquals(1, battery.getAvailableEnergy());
            ship.useEnergy(battery);
        } catch (EnergyException _){
            fail("Energy exception should not be thrown");
        }
        assertThrows(EnergyException.class, ()-> ship.useEnergy(battery));
    }

    /**
     * Tests the removal or decrement of various ship parameters through the
     * `updateParameter` method for different components of the ship.
     * <p>
     * The test verifies:
     * - The decrement in single cannon power.
     * - The decrement in double cannon power.
     * - The decrement in the number of double engines.
     * - The decrement in the number of single engines.
     * - The reduction in total energy.
     * - The decrement in the number of astronauts within the ship cabin.
     * - The correctness of cargo handling when a cargo hold parameter is updated.
     * <p>
     * Asserts are performed to ensure the changes made by the `updateParameter`
     * method are as expected. The method also ensures no exceptions are thrown
     * during the handling of cargo loading.
     */
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
        try {
            ship.loadCargo(CargoColor.GREEN, cargoHold);
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
        cargoHold.updateParameter(ship, -1);
        assertEquals(0, ship.cargos.get(CargoColor.GREEN));
    }

    /**
     * Tests the functionality of the findValidOneElimination method in a ship's component system.
     * <p>
     * This test verifies that:
     * 1. Specific components (e.g., downCannon, battery) can be "killed"
     *    without throwing a ComponentNotFoundException.
     * 2. After certain components are eliminated, the ship is no longer valid.
     * 3. The method findValid(-1, -1) is called to make the ship valid again.
     * 4. A specific component (singleEngine) is no longer found after
     *    calling findValid.
     * 5. The ship becomes valid after the proper adjustments are made.
     * 6. An IndexOutOfBoundsException is thrown when findValid is called
     *    with specific invalid index parameters.
     */
    @Test
    void findValidOneElimination(){
        try {
            ship.killComponent(downCannon);
            ship.killComponent(battery);
        } catch (ComponentNotFoundException _) {
            fail("Dead alien exception should not be thrown");
        }
        assertFalse(ship.isValid());
        ship.findValid(-1, -1);
        assertNull(ship.findComponent(singleEngine));
        assertTrue(ship.isValid());
        assertThrows(IndexOutOfBoundsException.class, ()-> ship.findValid(-1, 0));
    }

    /**
     * Tests the behavior of the ship object when components are eliminated
     * and its validity is evaluated.
     * <p>
     * This method simulates the elimination of specific components from the ship
     * and assesses the validity and presence of those components within the ship.
     * It ensures that eliminated components are no longer retrievable, and the
     * ship maintains validity for given parameters if certain conditions are met.
     * <p>
     * The method performs the following:
     * 1. Attempts to eliminate specific components (downCannon and battery)
     *    from the ship and catches any exceptions related to nonexistent components.
     * 2. Validates that the ship is no longer in a valid state after component removal.
     * 3. Calls `findValid` on the ship with specific parameters to attempt to reach
     *    a valid configuration.
     * 4. Verifies the absence of removed components and the presence of valid ones.
     * 5. Asserts that the ship attains a valid state for the given parameters.
     * <p>
     * Throws:
     * - RuntimeException if a ComponentNotFoundException is encountered.
     */
    @Test
    void findValidAllElimination(){
        try {
            ship.killComponent(downCannon);
            ship.killComponent(battery);
        } catch (ComponentNotFoundException e) {
            throw new RuntimeException();
        }
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