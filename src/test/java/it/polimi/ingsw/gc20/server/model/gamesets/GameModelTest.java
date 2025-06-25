package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    int level = 2;
    List<String> players = new ArrayList<>();
    String gameId = "1";
    GameModel gameModel = new GameModel();

    /**
     * Sets up the test environment before each test case.
     * This method initializes the player list with two player names.
     */
    @BeforeEach
    void setUp() {
        players.add("player1");
        players.add("player2");
    }

    private NormalShip ship, ship2;

    /**
     * Sets up the test environment before each test case.
     * This method initializes two NormalShip objects with various components and connectors.
     * It also loads cargo into the ships and initializes astronauts.
     * The setup is done to ensure that the ships are ready for testing various game model functionalities.
     */
    @BeforeEach
    void setUp2() {
        // Create a new NormalShip
        ship = new NormalShip();
        ship2 = new NormalShip();

        // Create components
        Cannon upCannon = new Cannon();
        upCannon.setPower(1);

        Cannon downCannon = new Cannon();
        downCannon.setRotation(Direction.DOWN);
        downCannon.setPower(2);

        Engine singleEngine = new Engine();
        singleEngine.setDoublePower(false);

        Engine doubleEngine = new Engine();
        doubleEngine.setDoublePower(true);

        Battery battery = new Battery();
        battery.setSlots(2);
        battery.setAvailableEnergy(2);

        Battery battery2 = new Battery();
        battery2.setSlots(2);
        battery2.setAvailableEnergy(2);

        Cabin cabin1 = new Cabin();
        cabin1.setColor(AlienColor.NONE);

        Cabin cabin2 = new Cabin();
        cabin2.setColor(AlienColor.NONE);

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);

        CargoHold cargoHold2 = new CargoHold();
        cargoHold2.setSlots(3);


        try {
            // Add components to ship at valid positions
            ship.addComponent(upCannon, 1, 3);
            ship.addComponent(downCannon, 3, 3);
            ship.addComponent(singleEngine, 3, 2);
            ship.addComponent(doubleEngine, 3, 4);
            ship.addComponent(battery, 2, 2);
            ship.addComponent(cabin1, 2, 4);
            ship.addComponent(cargoHold, 1, 2);

            ship2.addComponent(upCannon, 1, 3);
            ship2.addComponent(downCannon, 3, 3);
            ship2.addComponent(singleEngine, 3, 2);
            ship2.addComponent(doubleEngine, 3, 4);
            ship2.addComponent(battery2, 2, 2);
            ship2.addComponent(cabin2, 2, 4);
            ship2.addComponent(cargoHold2, 1, 2);
        } catch (InvalidTileException e){
            fail("Exception should not be thrown");
        }


        // Setting the connectors
        Map<Direction, ConnectorEnum> connectorsCargoHold = new HashMap<>();
        connectorsCargoHold.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargoHold.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold.setConnectors(connectorsCargoHold);

        Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
        connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargoHold2.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold2.setConnectors(connectorsCargoHold2);

        Map<Direction, ConnectorEnum> connectorsBattery = new HashMap<>();
        connectorsBattery.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsBattery.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorsBattery.put(Direction.UP, ConnectorEnum.D);
        connectorsBattery.put(Direction.DOWN, ConnectorEnum.S);
        battery.setConnectors(connectorsBattery);

        Map<Direction, ConnectorEnum> connectorsBattery2 = new HashMap<>();
        connectorsBattery2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsBattery2.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorsBattery2.put(Direction.UP, ConnectorEnum.D);
        connectorsBattery2.put(Direction.DOWN, ConnectorEnum.S);
        battery.setConnectors(connectorsBattery2);

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
        cabin1.setConnectors(connectorsCabin1);

        Map<Direction, ConnectorEnum> connectorsCabin2 = new HashMap<>();
        connectorsCabin2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCabin2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCabin2.put(Direction.UP, ConnectorEnum.S);
        connectorsCabin2.put(Direction.DOWN, ConnectorEnum.S);
        cabin2.setConnectors(connectorsCabin2);

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
        StartingCabin start = (StartingCabin) ship.getComponentAt(2, 3);
        start.setConnectors(connectorsStartingCabin);

        Map<Direction, ConnectorEnum> connectorsStartingCabin2 = new HashMap<>();
        connectorsStartingCabin2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start2 = (StartingCabin) ship2.getComponentAt(2, 3);
        start2.setConnectors(connectorsStartingCabin2);

        Map<Direction, ConnectorEnum> connectorBattery3 = new HashMap<>();
        connectorBattery3.put(Direction.RIGHT, ConnectorEnum.S);
        connectorBattery3.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorBattery3.put(Direction.UP, ConnectorEnum.ZERO);
        connectorBattery3.put(Direction.DOWN, ConnectorEnum.ZERO);
        Battery battery3 = new Battery();
        battery3.setConnectors(connectorBattery3);
        try {
            ship2.addComponent(battery3, 1, 1);
        } catch (InvalidTileException e){
            fail("Exception should not be thrown");
        }

        try {
            ship.loadCargo(CargoColor.GREEN, cargoHold);
            ship.loadCargo(CargoColor.BLUE, cargoHold);
            ship2.loadCargo(CargoColor.GREEN, cargoHold2);
            ship2.loadCargo(CargoColor.BLUE, cargoHold2);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        ship.initAstronauts();
        ship2.initAstronauts();
    }

    /**
     * Tests the default constructor of the GameModel class.
     * It checks if the gameModel object is not null and if the game is initialized to null.
     */
    @Test
    void defaultConstructor() {
        assertNotNull(gameModel);
        assertNull(gameModel.getGame());
    }

    /**
     * Tests the setLevel method of the GameModel class.
     * It sets a level and checks if the level is correctly set in the gameModel object.
     */
    @Test
    void setLevel(){
        gameModel.setLevel(level);
        assertEquals(level, gameModel.getLevel());
    }

    /**
     * Tests the setPlayers method of the GameModel class.
     */
    @Test
    void setGame (){
        Game game = new Game();
        gameModel.setGame(game);
        assertEquals(game, gameModel.getGame());
    }

    /**
     * Tests the setActiveCard method of the GameModel class.
     * This method assigns a specific AdventureCard as the active card in the game model.
     * It validates
     * that the active card is correctly set
     * by checking its non-null status and ensuring it matches the assigned card.
     */
    @Test
    void setActiveCard() {
        AdventureCard card = new AdventureCard();
        gameModel.setActiveCard(card);
        assertNotNull(gameModel.getActiveCard());
        assertEquals(card, gameModel.getActiveCard());
    }


    /**
     * Tests the startGame method of the GameModel class.
     * It initializes a game with a specified level, players, and game ID.
     * It checks if the game is started correctly by verifying the game object,
     * level, players' ships, and pile of components.
     */
    @Test
    void startGame() {
        gameModel.startGame(level, players, gameId);
        assertNotNull(gameModel.getGame());
        assertEquals(level, gameModel.getLevel());
        assertEquals(gameId, gameModel.getGame().getID());
        assertNotNull(gameModel.getGame().getBoard());
        for (Player player : gameModel.getGame().getPlayers()) {
            assertNotNull(player.getShip());
            assertEquals(NormalShip.class, player.getShip().getClass());
        }
        assertNotNull(gameModel.getGame().getPile());
        assertFalse(gameModel.getGame().getPile().getUnviewed().isEmpty());
        assertEquals(152, gameModel.getGame().getPile().getUnviewed().size());
    }

    /**
     * Tests all the methods related to assembling the ship in the game model.
     */
    @Test
    void assembling() {
        // init game
        gameModel.startGame(level, players, gameId);

        Player player = gameModel.getGame().getPlayers().getFirst();
        assertNotNull(player);

        Component component = gameModel.getGame().getPile().getUnviewed().getFirst();
        assertNotNull(component);

        try {
            gameModel.componentFromUnviewed(component);
            assertFalse(gameModel.getGame().getPile().getUnviewed().contains(component));

            gameModel.componentToViewed(component);
            assertTrue(gameModel.getGame().getPile().getViewed().contains(component));

            gameModel.RotateClockwise(component);

            gameModel.RotateCounterclockwise(component);


            gameModel.componentFromViewed(component);
            assertFalse(gameModel.getGame().getPile().getViewed().contains(component));

            gameModel.componentToBooked(component, player);
            assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

            gameModel.componentFromBooked(component, player);
            assertFalse(((NormalShip)player.getShip()).getBooked().contains(component));


            gameModel.addToShip(component, player, 1, 1);
            assertEquals(component, player.getShip().getComponentAt(1, 1));

            gameModel.stopAssembling(player, 2);
            assertEquals(3, player.getPosition());
            assertFalse(player.isLeader());

            Player secondPlayer = gameModel.getGame().getPlayers().get(1);
            gameModel.stopAssembling(secondPlayer, 1);
            assertEquals(6, secondPlayer.getPosition());
            assertTrue(secondPlayer.isLeader());

        } catch (Exception e) {
            fail("exception should not be thrown " + e.getMessage());
        }
    }

    /**
     * Tests the methods that calculate the score of players in the game model.
     */
    @Test
    void calculateScore() {
        gameModel.startGame(level, players, gameId);


        gameModel.getGame().getPlayers().forEach(player -> {
            if(player.getUsername().equals("player1")) {
                player.setShip(ship);
                player.setPosition(10);
                int waste = ship.getWaste().size();
                assertEquals(0, waste);
                assertTrue(gameModel.shipValidating(player));
            }
            if(player.getUsername().equals("player2")) {
                player.setShip(ship2);
                player.setPosition(20);
                int waste2 = ship2.getWaste().size();
                assertEquals(0, waste2);
                assertFalse(gameModel.shipValidating(player));
            }
        });

        Map<Player, Integer> score = gameModel.calculateScore();
        assertEquals(13, score.get(gameModel.getGame().getPlayers().get(1)));
        assertEquals(11, score.get(gameModel.getGame().getPlayers().get(0)));

        Battery bat = (Battery) ship2.getComponentAt(1,1);
        try {
            ship2.killComponent(bat);
        } catch (ComponentNotFoundException e){
            fail("Exception should not be thrown");
        }
        Player player1 = gameModel.getGame().getPlayers().get(0);
        Player player2;
        if(player1.getUsername().equals("player1")) {
            player2 = gameModel.getGame().getPlayers().get(1);
        }
        else{
            player2 = gameModel.getGame().getPlayers().get(0);
            player1=gameModel.getGame().getPlayers().get(1);
        }

        score = gameModel.calculateScore();
        assertEquals(13, score.get(player1));
        assertEquals(10, score.get(player2));
    }

    /**
     * Tests the movePlayer method of the GameModel class.
     * This method verifies
     * that the player's position is correctly updated when moved by a specific number of steps.
     * It ensures
     * that the player's new position reflects the correct calculation of the original position plus the steps moved.
     */
    @Test
    void testMovePlayer() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setPosition(5);

        gameModel.movePlayer(player, 3);
        assertEquals(8, player.getPosition());
    }

    /**
     * Tests the addCredits method of the GameModel class.
     * <p>
     * This test verifies that the credit balance of a player is correctly
     * updated when credits are added using the addCredits method.
     * It ensures
     * that the new credit value is accurate based on the initial value and
     * the number of credits added.
     * <p>
     * The test performs the following steps:
     * 1. Starts a new game session by initializing the game model with the required parameters.
     * 2. Retrieves the first player from the list of players in the game.
     * 3. Records the player's initial credit balance.
     * 4. Calls the addCredits method to add a specific number of credits to the player.
     * 5. Asserts that the player's new credit balance is equal to the initial balance plus the added credits.
     */
    @Test
    void testAddCredits() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        int initialCredits = player.getCredits();

        gameModel.addCredits(player, 10);
        assertEquals(initialCredits + 10, player.getCredits());
    }

    /**
     * Tests the getCrew method of the GameModel class.
     * <p>
     * This test validates that the getCrew method correctly retrieves the crew size of a player's ship.
     * <p>
     * Test steps:
     * 1. Starts a new game session with the specified level, players, and game ID.
     * 2. Retrieves the first player from the list of players in the game.
     * 3. Sets a predefined ship for the player.
     * 4. Calls the getCrew method to retrieve the crew size of the player's ship.
     * 5. Asserts that the crew size returned by the method matches the crew size of the predefined ship.
     */
    @Test
    void testGetCrew() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        int crew = gameModel.getCrew(player);
        assertEquals(ship.crew(), crew);
    }

    /**
     * Tests the getAstronauts method of the GameModel class.
     * <p>
     * This test verifies that the getAstronauts method correctly retrieves the number of astronauts
     * in a player's ship.
     * <p>
     * Test steps:
     * 1. Starts a new game session with the specified level, players, and game ID.
     * 2. Retrieves the first player from the list of players in the game.
     * 3. Sets a predefined ship for the player.
     * 4. Calls the getAstronauts method to retrieve the number of astronauts in the player's ship.
     * 5. Asserts that the number of astronauts returned by the method matches the number in the predefined ship.
     */
    @Test
    void testGetAstronauts() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        int astronauts = gameModel.getAstronauts(player);
        assertEquals(ship.getAstronauts(), astronauts);
    }

    /**
     * Tests the firePower method of the GameModel class.
     * <p>
     * This test validates the calculation of the firepower available to a player when specific ship components
     * are used in an attack.
     * It ensures that the returned firepower value matches the expected value calculated
     * based on the components and their energy usage.
     * <p>
     * Test steps:
     * 1. Starts a new game session with predefined parameters (level, players, game ID).
     * 2. Retrieves the first player from the list of players in the game.
     * 3. Assigns a ship to the player.
     * 4. Prepares a list of batteries and a set of cannons from the ship.
     * 5. Calls the firePower method with the player, selected cannons, and batteries.
     * 6. Asserts that the returned firepower equals the value calculated by the ship's firePower method.
     * 7. Verifies that the energy of the used battery is correctly decremented.
     * 8. Ensures no exceptions are thrown during the test execution.
     */
    @Test
    void testFirePower() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        batteries.add((Battery)ship.getComponentAt(2, 2));

        Cannon cannon = (Cannon)ship.getComponentAt(3, 3);
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(cannon);

        try {
            float power = gameModel.firePower(player, cannons, batteries);
            assertEquals(ship.firePower(cannons, batteries.size()), power);
            assertEquals(1, ((Battery)ship.getComponentAt(2, 2)).getAvailableEnergy());
        } catch (Exception e) {
            fail("Exceptions should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the engine power calculation functionality of the GameModel class.
     * <p>
     * This test verifies that the `enginePower` method correctly calculates the power of a player's
     * ship engine based on provided input and ensures that energy consumption is applied as expected.
     * <p>
     * Test steps:
     * 1. Initializes the game with a specified level, players, and game ID.
     * 2. Retrieves the first player from the list of players in the game and assigns a predefined ship to them.
     * 3. Prepares a list of batteries for the test by selecting a specific component from the ship.
     * 4. Calls the `enginePower` method with the player, speed, and list of batteries.
     * 5. Asserts that the returned engine power matches the expected power
     * calculated by the ship's `enginePower` method.
     * 6. Verifies that the energy of the battery used in the calculation is reduced to the expected value.
     * 7. Ensures no exceptions are thrown during the execution of the test.
     */
    @Test
    void testEnginePower() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        batteries.add((Battery)ship.getComponentAt(2, 2));

        try {
            int power = gameModel.enginePower(player, 1, batteries);
            assertEquals(ship.enginePower(1), power);
            assertEquals(1, ((Battery)ship.getComponentAt(2, 2)).getAvailableEnergy());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of the getInGamePlayers method in the GameModel class.
     * <p>
     * This test verifies that the getInGamePlayers method correctly retrieves the list of players
     * who are actively participating in the game.
     * The method excludes players who have their
     * game status set to inactive.
     * <p>
     * Test steps:
     * 1. Starts the game by initializing the game model with the required level, players, and game ID.
     * 2. Retrieves the list of players after the game starts and asserts the total player count.
     * 3. Sets one player's game status to inactive.
     * 4. Calls the getInGamePlayers method again and asserts that the list only includes the remaining
     *    active players.
     * 5. Validates that the order of players in the list matches the expected results based on the game status.
     */
    @Test
    void testGetInGamePlayers() {
        gameModel.startGame(level, players, gameId);
        Player player1 = gameModel.getGame().getPlayers().getFirst();
        Player player2 = gameModel.getGame().getPlayers().get(1);

        List<Player> inGamePlayers = gameModel.getInGamePlayers();
        assertEquals(2, inGamePlayers.size());

        player1.setGameStatus(false);
        inGamePlayers = gameModel.getInGamePlayers();
        assertEquals(1, inGamePlayers.size());
        assertEquals(player2, inGamePlayers.getFirst());
    }

    /**
     * Tests the autoValidation method of the GameModel class.
     * <p>
     * This test ensures that the autoValidation method performs successfully without throwing any exceptions.
     * <p>
     * Test steps:
     * 1. Starts a new game session using the specified level, players, and game ID.
     * 2. Retrieves the first player from the list of players in the game.
     * 3. Calls the autoValidation method for the retrieved player.
     * 4. Verifies that no exceptions are thrown during the execution of this method.
     */
    @Test
    void testAutoValidation() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        assertDoesNotThrow(() -> gameModel.autoValidation(player));
    }

    /**
     * Tests the functionality of the heavy meteor cannon in the game model.
     * <p>
     * The test sets up a game instance and a player with a ship, then simulates
     * the firing of a heavy meteor projectile with the cannon.
     * The method verifies
     * that the list of cannons returned by the {@code heavyMeteorCannon} method is
     * not empty, ensuring that the functionality works as expected.
     */
    @Test
    void testHeavyMeteorCannon() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Projectile meteor = new Projectile();
        meteor.setFireType(FireType.HEAVY_METEOR);
        meteor.setDirection(Direction.UP);

        List<Cannon> cannons = gameModel.heavyMeteorCannon(player, 7, meteor);

        assertFalse(cannons.isEmpty());
    }

    /**
     * Tests the functionality of the `moveCargo` method within the game model.
     * This test verifies that cargo can be successfully moved between different
     * cargo holds within a ship, as well as removed from the ship when necessary.
     * <p>
     * The test initializes a game, sets up a player's ship with an initial cargo hold,
     * and asserts the following scenarios:
     * - Proper setup of cargo in the initial cargo hold.
     * - Movement of cargo between two connected cargo holds.
     * - Removal of cargo from the ship when moved to a null destination.
     * <p>
     * The test also ensures that no exceptions are thrown during the execution of
     * these operations, with appropriate assertions to validate the cargo quantities
     * at each stage of the process.
     */
    @Test
    void testMoveCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        CargoHold cargoHold1 = (CargoHold) ship.getComponentAt(1, 2);
        CargoHold cargoHold2;

        try {
            cargoHold2 = new CargoHold();
            cargoHold2.setSlots(3);
            Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
            connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.UP, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.S);
            cargoHold2.setConnectors(connectorsCargoHold2);
            ship.addComponent(cargoHold2, 1, 4);

            assertNotNull(ship.getCargo().get(CargoColor.GREEN));
            assertEquals(1, ship.getCargo().get(CargoColor.GREEN));

            gameModel.moveCargo(player, CargoColor.GREEN, cargoHold1, cargoHold2);
            assertEquals(1, ship.getCargo().get(CargoColor.GREEN));

            gameModel.moveCargo(player, CargoColor.GREEN, cargoHold2, null);
            assertEquals(0, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

        } catch (Exception e) {
            fail("exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of adding cargo to a ship's cargo hold.
     * <p>
     * This test initializes a game model, starts the game with specified parameters,
     * and verifies the process of adding cargo to a ship's cargo hold.
     * It ensures that:
     * - Cargo is added to the ship with the correct count based on the cargo color.
     * - Attempting to add cargo beyond the cargo hold's capacity results in a CargoFullException.
     * - The test handles exceptions appropriately and fails if any unexpected exceptions are thrown.
     */
    @Test
    void testAddCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);


        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);

        try {

            gameModel.moveCargo(player, CargoColor.GREEN, cargoHold, null);
            gameModel.moveCargo(player, CargoColor.BLUE, cargoHold, null);


            gameModel.addCargo(player, CargoColor.YELLOW, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));

            gameModel.addCargo(player, CargoColor.GREEN, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

            gameModel.addCargo(player, CargoColor.BLUE, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));


            assertThrows(CargoFullException.class, () ->
                    gameModel.addCargo(player, CargoColor.BLUE, cargoHold)
            );
        } catch (Exception e) {
            fail("exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of exceptions when moving cargo in the game model.
     * <p>
     * This method validates the behavior of the {@code moveCargo} functionality to ensure
     * appropriate exceptions are thrown under specific conditions.
     * <p>
     * The test performs the following validations:
     * 1. Confirms that an {@link InvalidCargoException} is thrown when attempting to move
     *    cargo to a {@code null} destination cargo hold.
     * 2. Verifies that adding too much cargo to a cargo hold results in the appropriate
     *    {@link CargoFullException}.
     * <p>
     * The method also ensures that valid configurations, such as setting up cargo holds
     * with specific dimensions and connectors, do not inadvertently throw exceptions during
     * test setup.
     * <p>
     * Assertions made within this test:
     * - Ensures that exceptions are thrown for invalid actions.
     * - Fails if any unexpected exceptions are encountered during the test execution.
     */
    @Test
    void testMoveCargoExceptions() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);


        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);


        assertThrows(InvalidCargoException.class, () -> gameModel.moveCargo(player, CargoColor.RED, cargoHold, null));

        try {

            CargoHold smallCargoHold = new CargoHold();
            smallCargoHold.setSlots(1);
            Map<Direction, ConnectorEnum> connectors = new HashMap<>();
            connectors.put(Direction.RIGHT, ConnectorEnum.S);
            connectors.put(Direction.LEFT, ConnectorEnum.S);
            connectors.put(Direction.UP, ConnectorEnum.S);
            connectors.put(Direction.DOWN, ConnectorEnum.S);
            smallCargoHold.setConnectors(connectors);
            ship.addComponent(smallCargoHold, 3, 1);


            gameModel.addCargo(player, CargoColor.BLUE, smallCargoHold);


            assertThrows(CargoFullException.class, () -> gameModel.moveCargo(player, CargoColor.GREEN, cargoHold, smallCargoHold));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of the {@code viewDeck} method in {@code GameModel}.
     * <p>
     * This method ensures that the deck view returns a valid, non-empty list of cards
     * when a valid index is provided.
     * It also verifies that an {@code InvalidIndexException}
     * is thrown when an invalid index is passed to the {@code viewDeck} method.
     * <p>
     * Assertions:
     * - Checks that the list of adventure cards returned is not null.
     * - Ensures that the list of adventure cards is not empty.
     * - Verifies that an {@code InvalidIndexException} is thrown for an invalid index.
     * - Fails the test if an unexpected {@code InvalidIndexException} is thrown during normal execution.
     */
    @Test
    void testViewDeck() {
        gameModel.startGame(level, players, gameId);
        try {

            List<AdventureCard> cards = gameModel.viewDeck(1);
            assertNotNull(cards);
            assertFalse(cards.isEmpty());

            assertThrows(InvalidIndexException.class, () -> gameModel.viewDeck(4));
        } catch (InvalidIndexException e) {
            fail("InvalidIndexException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the removal of a component from a player's ship at a specific
     * location.
     * It verifies that the specified component is successfully
     * removed and no longer exists at the given coordinates.
     * <p>
     * The method simulates starting a game, sets up a player with a ship,
     * and invokes the {@code removeComponent} method.
     * It then asserts that the component at the specified location has been removed.
     * <p>
     * If the component is not found during the removal operation, the
     * {@code ComponentNotFoundException} is caught, and the test fails.
     * <p>
     * Assertions:
     * - Ensures the component is removed and returns {@code null} for the
     *   specified location on the ship.
     * - Ensures no exceptions are thrown during the process.
     */
    @Test
    void testRemoveComponent() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        try {

            gameModel.removeComponent(1, 3, player);


            assertNull(player.getShip().getComponentAt(1, 3));
        } catch (ComponentNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of setting an alien in a cabin within the game model.
     * <p>
     * This test verifies that an alien of a specified color can be placed into a cabin
     * by calling the game model's `setAlien` method.
     * It also ensures that an exception
     * (`InvalidAlienPlacement`) is thrown when attempting to place an alien of a different
     * color into the cabin that already has an alien.
     * <p>
     * The test performs the following checks:
     * - Ensures the alien's color is successfully set to the cabin's alien color.
     * - Validates that attempting to set an alien of an incompatible color throws
     *   the appropriate exception.
     * <p>
     * If an `InvalidAlienPlacement` exception is thrown incorrectly during the test,
     * it explicitly fails, as this behavior would not be expected.
     */
    @Test
    void testSetAlien() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);

        try {

            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);


            assertEquals(AlienColor.PURPLE, cabin.getAlienColor());


            assertThrows(InvalidAlienPlacement.class, () ->
                    gameModel.setAlien(AlienColor.BROWN, cabin, player)
            );
        } catch (InvalidAlienPlacement e) {
            fail("InvalidAlienPlacement should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the behavior of adding pieces to a player's ship in the game model.
     * <p>
     * This method ensures the following:
     * - A component is successfully booked for a player without throwing an exception.
     * - After invoking the addPieces method, the component is moved from the booked list
     *   to the waste list of the player's ship.
     * - The number of astronauts in the player's ship is updated and is not zero.
     * <p>
     * The test starts with initializing the game with a given level, players, and game ID.
     * A player is retrieved, and their ship is set.
     * A specific component is added to
     * the player's booked components, and the addPieces method is executed to verify
     * the proper transfer and updates within the player's ship.
     */
    @Test
    void testAddPieces() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Component component = new Battery();
        try {
            gameModel.componentToBooked(component, player);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

        gameModel.addPieces(player);

        assertTrue(player.getShip().getWaste().contains(component));

        assertNotEquals(0, player.getShip().getAstronauts());
    }

    /**
     * Tests the functionality of the createDeck method within the game model.
     * <p>
     * Validates that a deck is successfully created and is not empty after the
     * method execution.
     * The test ensures that the game model correctly assigns a
     * deck to the board for the current game.
     */
    @Test
    void testCreateDeck() {
        gameModel.startGame(level, players, gameId);

        gameModel.createDeck();

        assertFalse(gameModel.getGame().getBoard().getDeck().isEmpty());
    }

    /**
     * Tests the functionality of drawing a card in the game and its effects
     * on the game's state and player positions.
     * <p>
     * This method initializes and starts a game using the provided level, players,
     * and game ID. It sets initial positions for two players and verifies the behavior
     * of the drawCard method.
     * <p>
     * The test ensures:
     * - A drawn card is not null.
     * - The drawn card matches the game's active card.
     * - A player's state is updated correctly if their position indicates
     *   they are out of the game based on the board's total spaces.
     * <p>
     * The test also expects no EmptyDeckException to be thrown during execution.
     * An exception results in test failure with an appropriate message.
     */
    @Test
    void testDrawCard() {
        gameModel.startGame(level, players, gameId);
        Player player1 = gameModel.getGame().getPlayers().getFirst();
        Player player2 = gameModel.getGame().getPlayers().get(1);

        player1.setPosition(20);
        player2.setPosition(2);

        try {

            AdventureCard card = gameModel.drawCard();

            assertNotNull(card);
            assertEquals(card, gameModel.getActiveCard());

            if (player1.getPosition() - player2.getPosition() >= gameModel.getGame().getBoard().getSpaces()) {
                assertFalse(player2.isInGame());
            }
        } catch (EmptyDeckException e) {
            fail("EmptyDeckException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of the `loseCrew` method in the `gameModel` class.
     * Verifies that when a crew member is lost from the ship, the alien
     * associated with the cabin is removed and the cabin's alien color
     * is reset to `NONE`.
     * <p>
     * This test covers:
     * - Starting the game and initializing required objects such as the player and ship.
     * - Placing an alien in a specific cabin and ensuring it is successfully set.
     * - Calling the `loseCrew` method and verifying that the alien and its associated
     *   color are properly removed from the specified cabin.
     * <p>
     * Assertions:
     * - Ensures the cabin initially has an alien after being set.
     * - Verifies that after losing the crew, the cabin no longer has an alien
     *   and its color is reset.
     * <p>
     * If any unexpected exceptions are thrown during the execution, the test will fail.
     */
    @Test
    void testLoseCrew() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
        try {
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);

            assertTrue(cabin.getAlien());

            List<Cabin> cabins = new ArrayList<>();
            cabins.add(cabin);

            gameModel.loseCrew(player, cabins);

            assertEquals(AlienColor.NONE, cabin.getAlienColor());

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of the `useShield` method in the game model.
     * <p>
     * The method ensures that the shield usage reduces the available energy of the
     * ship's battery and verifies that the `EnergyException` is thrown when attempting
     * to use the shield after the battery is depleted.
     * <p>
     * The test performs the following validations:
     * - Confirms the battery's energy level decreases by 1 after using the shield.
     * - Repeatedly uses the shield until the battery is depleted, validating energy
     *   reduction at each step.
     * - Ensures an `EnergyException` is thrown when attempting to use the shield
     *   with no available energy in the battery.
     * - Fails the test if an unexpected `EnergyException` is thrown prematurely.
     */
    @Test
    void testUseShield() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Battery battery = (Battery) ship.getComponentAt(2, 2);
        int initialEnergy = battery.getAvailableEnergy();

        try {

            gameModel.useShield(player, battery);

            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            while (battery.getAvailableEnergy() > 0) {
                gameModel.useShield(player, battery);
            }

            assertThrows(EnergyException.class, () -> gameModel.useShield(player, battery));
        } catch (EnergyException e) {
            fail("EnergyException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the firing functionality in the game model, ensuring that when a player fires a
     * projectile at a specified location, the expected outcomes occur, such as changes in
     * the game state or effects on game components like aliens within the cabin.
     * <p>
     * This method sets up a game scenario with a specific ship, cabin, and alien configuration.
     * It then simulates firing a projectile with specific properties (direction and fire type)
     * and verifies if the alien in the targeted cabin is correctly removed as a result of the action.
     * <p>
     * The test ensures no exceptions are thrown during the execution and asserts the expected
     * behavior according to the game rules.
     */
    @Test
    void testFire() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        try {

            Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);


            Projectile heavyProjectile = new Projectile();
            heavyProjectile.setDirection(Direction.UP);
            heavyProjectile.setFireType(FireType.HEAVY_FIRE);


            gameModel.fire(player, 8, heavyProjectile);
            assertFalse(cabin.getAlien());

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the {@code removeEnergy} method of the {@code GameModel} class.
     * <p>
     * This test method initializes a game with a specific level, players, and game ID,
     * and sets up a ship for the first player.
     * It retrieves a specific {@code Battery}
     * component from the ship and tracks its initial available energy.
     * The test verifies
     * that the energy is successfully reduced by the expected amount after calling
     * {@code removeEnergy}.
     * It also ensures that an {@code EnergyException} is thrown
     * when attempting to remove energy from an already depleted battery.
     * <p>
     * Assertions:
     * - The energy of the battery decreases by 1 after calling the method.
     * - An {@code EnergyException} is expected
     *   when attempting to remove energy beyond the battery's available amount.
     * - If an {@code EnergyException} is thrown prematurely, the test fails.
     */
    @Test
    void testRemoveEnergy() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        Battery battery = (Battery) ship.getComponentAt(2, 2);
        batteries.add(battery);

        int initialEnergy = battery.getAvailableEnergy();

        try {

            gameModel.removeEnergy(player, batteries);

            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            while (battery.getAvailableEnergy() > 0) {
                gameModel.removeEnergy(player, batteries);
            }

            assertThrows(EnergyException.class, () ->
                    gameModel.removeEnergy(player, batteries));
        } catch (EnergyException e) {
            fail("EnergyException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the functionality of the hourglass mechanism in the game model.
     * <p>
     * This test verifies that the hourglass period is correctly set, the countdown
     * starts as expected, and the remaining time decreases consistently over the
     * course of the test.
     * Additionally, it checks that turning the hourglass resets
     * the remaining time without throwing any exceptions.
     *
     * @throws InterruptedException if the thread sleep is interrupted
     */
    @Test
    void testHourglass1 () throws InterruptedException {
        gameModel.startGame(level, players, gameId);
        NormalBoard board= (NormalBoard) gameModel.getGame().getBoard();
        board.hourglass.setPeriod(5);
        gameModel.initCountdown();
        assertEquals(5, gameModel.getRemainingTime());
        Thread.sleep(3000);
        assertEquals(2, gameModel.getRemainingTime());
        assertEquals(12, gameModel.getTotalRemainingTime());
        Thread.sleep(2000);
        try {
            gameModel.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(5, gameModel.getRemainingTime());
    }

    /**
     * Tests the hourglass functionality in the game model.
     * <p>
     * This test ensures the correct behavior of the hourglass timer during game play, including
     * - Setting the hourglass period.
     * - Ensuring HourglassException is thrown when attempting to turn the hourglass prematurely.
     * - Verifying smooth hourglass turning after the set period has elapsed.
     * - Ensuring the countdown timer stops at zero after later periods.
     * - Checking the count of turned hourglasses is updated correctly.
     * <p>
     * The method also verifies that no exceptions are thrown when turning the hourglass after
     * the valid period has passed.
     *
     * @throws InterruptedException if the thread is interrupted during the sleep intervals.
     */
    @Test
    void testHourglass2 () throws InterruptedException {
        gameModel.startGame(level, players, gameId);
        NormalBoard board= (NormalBoard) gameModel.getGame().getBoard();
        board.hourglass.setPeriod(5);
        gameModel.initCountdown();
        assertThrows(HourglassException.class, () -> gameModel.turnHourglass());
        Thread.sleep(5100);
        try {
            gameModel.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown"+ e.getMessage());
        }
        Thread.sleep(5100);
        try {
            gameModel.turnHourglass();
        } catch (HourglassException e) {
            fail("Exception should not be thrown");
        }
        Thread.sleep(5100);
        assertEquals(0, gameModel.getRemainingTime());
        assertEquals(2, gameModel.getTurnedHourglass());
        assertThrows(HourglassException.class, () -> gameModel.turnHourglass());
    }

    /**
     * Tests the functionality of initializing a demo game setup.
     * This method performs the following actions:
     * - Adds "player3" and "player4" to the player collection.
     * - Starts a new game using the provided level, players, and gameId.
     * - Creates demo ships in the game environment.
     * <p>
     * This test ensures that the game model correctly sets up a demo scenario
     * with the specified players and game configuration.
     */
    @Test
    void testDemo() {
        players.add("player3");
        players.add("player4");
        gameModel.startGame(level, players, gameId);
        gameModel.createDemoShips();
    }
}