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

    /**
     * Sets up the test environment for `NormalShipTest` by initializing a new ship and its components.
     * <p>
     * The setup involves:
     * - Creating a new `NormalShip` instance.
     * - Initializing various components (`Cannon`, `Engine`, `Battery`, `Cabin`, `CargoHold`) with specific configurations.
     * - Adding these components to the ship at specific positions.
     * - Configuring connectors for each component to match its intended functionality.
     * - Initializing astronauts in the ship to simulate the test scenario.
     * <p>
     * This method is executed before each test to ensure a consistent and isolated test environment.
     * If adding components fails, the test setup will report an error.
     */
    @BeforeEach
    void setUp() {
        // Create a new NormalShip
        ship = new NormalShip();

        // Create components
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

    /**
     * Tests the functionality of getting and setting the color that the cabin can host within the ship.
     * <p>
     * Validates the following scenarios:
     * - The default color that the cabin can host is `AlienColor.NONE`.
     * - After setting a new color (e.g., `AlienColor.BROWN`), the cabin correctly reflects the updated
     *   color and allows retrieval of the same.
     * <p>
     * The test ensures consistency in the behavior of the `getColorHostable()` and `setColorHostable()`
     * methods from the `NormalShip` class.
     */
    @Test
    void ColorHostable(){
        assertEquals(AlienColor.NONE, ship.getColorHostable());
        ship.setColorHostable(AlienColor.BROWN);
        assertEquals(AlienColor.BROWN, ship.getColorHostable());
    }

    /**
     * Tests the behavior of the {@code getFirstComponent} method of the {@code NormalShip} class.
     * <p>
     * This method verifies that the first component encountered in a specific direction and
     * at a particular row/column of the ship's board is correctly identified. The test ensures that the
     * logic related to directions (UP, DOWN, LEFT, RIGHT) and boundary validation is implemented correctly.
     * <p>
     * The following scenarios are validated:
     * - Retrieving the first component in the UP direction for a given column.
     * - Retrieving the first component in the DOWN direction for a given column.
     * - Retrieving the first component in the RIGHT direction for a given row.
     * - Ensuring {@code null} is returned when no component exists in the specified direction and position.
     * - Retrieving the first component in the LEFT direction for a given row.
     * <p>
     * Assertions:
     * - The returned component matches the expected component in valid cases.
     * - {@code null} is returned for out-of-bound indices or when no component exists in the specified path.
     */
    @Test
    void getFirstComponent() {
        assertEquals(cargoHold, ship.getFirstComponent(Direction.UP, 2));
        assertEquals(singleEngine, ship.getFirstComponent(Direction.DOWN, 2));
        assertEquals(Cabin1, ship.getFirstComponent(Direction.RIGHT, 2));
        assertNull(ship.getFirstComponent(Direction.UP, 6));
        assertEquals(battery, ship.getFirstComponent(Direction.LEFT, 2));
    }


    /**
     * Tests the functionality of the {@code addBookedToWaste} method in the {@code NormalShip} class.
     * <p>
     * Validates that booked cabins are successfully transferred to the waste when this method is invoked.
     * <p>
     * The test ensures the following:
     * - A cabin added to the booked list of the ship is correctly identified as booked.
     * - All booked cabins are removed from the booked list after invoking the {@code addBookedToWaste} method.
     * - All removed cabins are successfully added to the waste list.
     * - The count of cabins in the waste list matches the number of cabins that were previously booked.
     * <p>
     * Assertions:
     * - The booked list no longer contains cabins that were moved to waste.
     * - The waste list size increases correctly after the transfer.
     * - Exceptions are not thrown during the execution of the method.
     * <p>
     * If the booked cabins are not properly transferred or other issues occur, the test will fail.
     */
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

    /**
     * Tests the functionality of the {@code removeBooked} method in the {@code NormalShip} class.
     * <p>
     * The test validates the following scenarios:
     * - Successfully removes a booked component from the ship.
     * - Verifies that the removed component no longer exists in the booked list.
     * - Ensures that other booked components are unaffected when removing a specific component.
     * - Allows re-adding a previously removed booked component.
     * - Handles the removal of a component that was never booked, ensuring that a {@code ComponentNotFoundException} is thrown.
     * <p>
     * Assertions:
     * - The booked list does not contain the removed component after successful removal.
     * - The booked list still contains other components that are not removed.
     * - The method allows re-booking of removed components without errors.
     * - A {@code ComponentNotFoundException} is thrown when attempting to remove a component that was not booked.
     * <p>
     * If any of the above validations fail, the test will mark the method as incorrect.
     */
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

    /**
     * Verifies the functionality of the {@code getComponentAt} method in the {@code NormalShip} class.
     * <p>
     * This test confirms that the method correctly retrieves components located at specific row and
     * column indices in the ship's structure or returns {@code null} when no component exists at
     * the specified position.
     * <p>
     * Validates the following scenarios:
     * - Retrieve the correct component for valid positions with components present.
     * - Return {@code null} for out-of-bound positions or positions with no component.
     * <p>
     * Assertions:
     * - The component at specified valid indices matches the expected component.
     * - The method returns {@code null} for positions without components or invalid indices.
     * <p>
     * Failure of assertions will indicate issues in row/column boundary management or component retrieval logic.
     */
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

    /**
     * Tests the behavior of the {@code firePower} method in the {@code NormalShip} class.
     * <p>
     * Validates the following scenarios:
     * - Calculation of firepower when a set of cannons and a power level is provided.
     * - Proper handling when no cannons are present in the set.
     * - Accurate computation of firepower when multiple cannons with varying power levels
     *   are included in the set.
     * - Adjustments in firepower calculation when specific conditions, such as the presence
     *   of a "purple alien," are applied.
     * - Ensures that no exceptions occur during the calculation process.
     * <p>
     * Assertions:
     * - The returned firepower value matches the expected result for each test case.
     * - A failure in firepower calculation will trigger the test to fail with a specific
     *   failure message.
     */
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

    /**
     * Tests the functionality of the {@code enginePower} method in the {@code NormalShip} class.
     * <p>
     * Validates the following scenarios:
     * - Calculates the engine power correctly with a given number of double engines activated.
     * - Handles the reduction in engine power when specific components, such as single or double engines, are removed.
     * - Considers the influence of a brown alien on the engine power calculation.
     * - Ensures proper handling of the {@code ComponentNotFoundException} when attempting to kill a non-existent component.
     * <p>
     * Assertions:
     * - The returned engine power value correctly reflects the number of active engines and the presence of a brown alien.
     * - The engine power reduces appropriately when components are removed.
     * - The engine power computation does not fail unexpectedly despite the removal or non-existence of components.
     * <p>
     * The test ensures accurate and reliable behavior of the {@code enginePower} method in different scenarios.
     */
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


    /**
     * Tests the functionality of the {@code setComponentAt} method in the {@code NormalShip} class.
     * <p>
     * Validates the behavior of placing a new component at a specific row and column on the ship's board.
     * Ensures that the correct component is placed at the specified position and the changes
     * are reflected when retrieving the component from the same position.
     * <p>
     * The test includes the following validations:
     * - Successfully sets a new component (e.g., {@code Cannon}) at a valid position on the board.
     * - Verifies that the {@code getComponentAt} method retrieves the expected component after placement.
     * - Ensures that no exceptions, such as {@code InvalidTileException}, are thrown during valid operations.
     * <p>
     * Assertions:
     * - The component set at the given coordinates matches the expected new component.
     * - The test fails with a specific error message if the component cannot be set due to an invalid position or other issues.
     */
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


    /**
     * Tests the functionality of updating the life support system for a spaceship's cabin.
     * It verifies if the cabin's alien color updates properly when life support is added and removed.
     * <p>
     * The test ensures the following:
     * 1. The initial alien color of the cabin is verified to be NONE.
     * 2. A LifeSupport component is created and its color is set.
     * 3. The proper connectors for both the cabin and the life support system are configured,
     *    and the life support component is added to the spaceship.
     * 4. After adding the life support, the cabin's alien color should change to match the life support's color.
     * 5. The life support system is then removed, and the cabin's alien color is verified to revert back to NONE.
     * <p>
     * Exceptions are verified to ensure no failures occur in adding or removing the life support component.
     * <p>
     * The test will fail if the life support addition or removal does not correctly update the cabin's color
     * or if any unexpected exceptions occur.
     */
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


    /**
     * Tests the functionality of the removeBooked method.
     * <p>
     * This test ensures that the method correctly removes a specified Cabin
     * object from the list of booked cabins in a Ship instance. It verifies
     * that the removed Cabin is no longer present in the booked cabins list
     * while other cabins remain unaffected. The test also handles any
     * unexpected exceptions and fails the test if such occur.
     * <p>
     * Assertions:
     * - After adding a Cabin to the booked list, it is verified to be present
     *   in the list.
     * - When a Cabin is removed using removeBooked, it is verified to no longer
     *   exist in the booked list.
     * - Other cabins in the list remain unaffected by the removal operation.
     * <p>
     * Failure Condition:
     * - If an exception is thrown during the execution, the test will fail with
     *   a corresponding failure message.
     */
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

    /**
     * Tests the getBooked() method of the Ship class to verify that it correctly
     * retrieves a list of all booked components, such as Cabins.
     * <p>
     * The method first adds cabins to the booked list using the addBooked() method
     * and then validates that these cabins are included in the list returned by
     * getBooked(). It uses assertions to check that the expected components are
     * present in the retrieved list of booked components.
     * <p>
     * If any exception occurs during the test, it fails the test with an appropriate
     * error message.
     */
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

    /**
     * Tests the `addBooked` method to ensure that a cabin can be successfully added
     * to the list of booked cabins associated with a ship.
     * <p>
     * The test initializes a new `Cabin` object, invokes the `addBooked` method
     * to add the cabin to the ship's booked list, and asserts that the cabin is
     * present in the booked list after the addition. If any exception is thrown
     * during the process, the test fails with an appropriate message.
     */
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

    /**
     * Tests the getRows method of the Ship class.
     * Verifies that the number of rows returned by the getRows method matches the expected value.
     */
    @Test
    void getRows() {
        assertEquals(5, ship.getRows());
    }

    /**
     * Tests the getCols method of the Ship object.
     * Ensures that the method returns the correct number of columns.
     * <p>
     * The test verifies that the number of columns returned by the
     * getCols method matches the expected value.
     */
    @Test
    void getCols() {
        assertEquals(7, ship.getCols());
    }


    /**
     * Tests the functionality of adding and removing a life support component
     * from a ship and its impact on the connected cabin's properties and behavior.
     * <p>
     * The method performs the following actions:
     * 1. Initializes a set of connectors with a specific configuration and assigns
     *    them to the cabin used for this test.
     * 2. Adds a life support component to a specified position in the ship layout
     *    and verifies its effect on the cabin's color to match the life support's color.
     * 3. Removes the life support component and confirms that the cabin's color
     *    resets to its default state.
     * 4. Performs additional testing to validate the removal of life support,
     *    such as loading and unloading the crew from the cabin and validating
     *    that an exception is appropriately thrown if the life support is
     *    removed while its absence impacts aliens in the connected cabin.
     * <p>
     * Assertions and exception handling ensure the correctness of the life
     * support addition, removal, and associated behaviors.
     */
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


    /**
     * Tests the functionality of adding an alien to a cabin in the spaceship while validating
     * various constraints and scenarios related to alien placement and cabin conditions.
     * <p>
     * This method performs the following validations:
     * 1. Ensures that astronauts are removed from the cabin before adding an alien.
     * 2. Configures the cabin with a proper set of connectors required for life support.
     * 3. Adds a life support component to the spaceship and verifies the ability to add an alien of a specific color.
     * 4. Verifies that the alien color and placement are correctly updated in the respective cabin.
     * 5. Tests the prevention of adding multiple aliens of the same color, ensuring that an appropriate exception is thrown.
     * 6. Verifies the restriction that prevents simultaneously having astronauts and aliens in the same cabin.
     * 7. Removes components and re-adds life support to validate subsequent alien addition constraints.
     * 8. Ensures that adding an alien when one of the same color is already designated as present throws the proper exception.
     * <p>
     * The method uses assertions and exception handling to verify the correct behavior of the spaceship's alien placement logic.
     * <p>
     * Exceptions handled:
     * - {@link InvalidAlienPlacement}: Thrown when conditions for valid alien placement are violated.
     * - {@link InvalidTileException}: Thrown if the life support component cannot be added to the specified location.
     * - {@link ComponentNotFoundException}: Thrown when attempting to remove a non-existing component.
     * <p>
     * Assertions:
     * - Ensures the correct alien color is placed in the cabin.
     * - Verifies appropriate exceptions are thrown under invalid conditions.
     * - Confirms that life support components are properly managed.
     */
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

    /**
     * Tests the functionality and behavior of the crew-related methods and alien state of the {@code Ship} class.
     * <p>
     * The test verifies the following:
     * - Initial crew count and alien presence.
     * - Updates to the crew count and alien state after specific actions, such as unloading crew,
     *   toggling the presence of brown or purple aliens, and handling both alien types simultaneously.
     * - Ensures proper exception handling when attempting to unload crew from an empty cabin,
     *   failing the test if an {@code EmptyCabinException} is thrown unexpectedly.
     * <p>
     * Assertions include:
     * - The correctness of the crew count after each relevant operation.
     * - The alien color state changes in response to the toggling of {@code brownAlien} and {@code purpleAlien}.
     */
    @Test
    void testCrew(){
        try {
            assertEquals(4, ship.crew());
            ship.unloadCrew(Cabin1);
            assertEquals(AlienColor.NONE, ship.getAliens());
            assertEquals(3, ship.crew());
            ship.brownAlien = true;
            assertEquals(AlienColor.BROWN, ship.getAliens());
            assertEquals(4, ship.crew());
            ship.purpleAlien = true;
            assertEquals(AlienColor.BOTH, ship.getAliens());
            assertEquals(5, ship.crew());
            ship.brownAlien = false;
            assertEquals(AlienColor.PURPLE, ship.getAliens());
        } catch (EmptyCabinException _){
            fail("Failed to unload crew");
        }
    }

    /**
     * Tests the functionality of the addComponent method in the Ship class.
     * <p>
     * The method ensures that components can be added to valid positions
     * on the ship's grid and verifies that an InvalidTileException is thrown
     * when attempting to add a component to an invalid position.
     * <p>
     * Successful addition:
     * - A new component (e.g., Cannon) is added to a valid tile.
     * - The position of the component is verified using assertions.
     * <p>
     * Exception handling:
     * - Attempts to add a component to an invalid position.
     * - Verifies that the appropriate exception is thrown with the correct message.
     * <p>
     * This test ensures the integrity of the component addition logic in the Ship class.
     */
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