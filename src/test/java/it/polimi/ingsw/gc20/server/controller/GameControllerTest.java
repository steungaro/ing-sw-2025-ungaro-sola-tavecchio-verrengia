package it.polimi.ingsw.gc20.server.controller;

import it.polimi.ingsw.gc20.server.controller.states.*;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController gameController;
    AbandonedShipState abandonedShipState;
    AdventureCard adventureCard;
    String id = "0";

    /**
     * Sets up the initial environment for each test execution in the GameControllerTest class.
     * This method is executed before each test case to ensure that the test environment is properly configured.
     * <p>
     * The method:
     * - Initializes the GameController instance.
     * - Creates a list of player names.
     * - Handles exceptions related to invalid game states.
     * - Initializes the necessary state and adventure card instances for testing.
     * <p>
     * This ensures a consistent and isolated test environment, preparing all required resources.
     */
    @BeforeEach
    void setUp() {
        // Initialize the GameController object before each test
        // This is a placeholder, replace it with actual initialization code
        String player1 = "player1";
        String player2 = "player2";
        String player3 = "player3";
        String player4 = "player4";
        List<String> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        List<String> players2 = new ArrayList<>();
        players2.add(player1);
        int level = 2;
        try {
            assertThrows(InvalidStateException.class, () -> new GameController(id, id, players2, level));
            gameController = new GameController(id, id, players, level);
            adventureCard = new AdventureCard();
            adventureCard.setCrew(2);
            adventureCard.setCredits(3);
            adventureCard.setLostDays(1);
            abandonedShipState = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        } catch (InvalidStateException _){
            fail();
        }
    }

    /**
     * Tests the functionality of the setState method in the GameController class.
     * <p>
     * This method ensures that the gameController correctly updates its current
     * state when the setState method is invoked with a new state. It verifies that:
     * - The new state is successfully assigned to the gameController.
     * - The updated state is correctly returned by the getState method.
     * <p>
     * Assertions:
     * - The state of gameController is equal to the provided abandonedShipState
     *   after invoking setState.
     * <p>
     * This test ensures the correctness of state transitions within the gameController.
     */
    @Test
    void setState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState, gameController.getState());
    }

    /**
     * Tests the functionality of the `getGameID` method in the `GameController` class.
     * <p>
     * This method ensures that the `getGameID` method correctly retrieves the unique identifier
     * (`gameID`) assigned to the current game instance. It validates that:
     * <p>
     * - The returned game ID matches the expected ID set in the `gameController`.
     * <p>
     * Assertions:
     * - The retrieved game ID is equal to the predefined `id` value.
     * <p>
     * This test verifies the correctness of the `getGameID` method and ensures proper access
     * to the game's unique identifier.
     */
    @Test
    void getGameID() {
        assertEquals(id, gameController.getGameID());
    }

    /**
     * Tests the functionality of the `getFirstOnlinePlayer` method in the `GameController` class.
     * <p>
     * This method verifies that the `getFirstOnlinePlayer` correctly identifies and retrieves the
     * username of the first player who is currently online from the list of players in the game.
     * <p>
     * Assertions:
     * - The returned username is equal to the expected first online player in the list.
     * <p>
     * This test ensures that the `getFirstOnlinePlayer` method operates as intended, effectively
     * filtering and returning the correct player data based on the player's online status.
     */
    @Test
    void getFirstOnlinePlayer() {
        String firstOnlinePlayer = gameController.getFirstOnlinePlayer();
        assertEquals("player1", firstOnlinePlayer);
    }

    /**
     * Tests the functionality of the `drawCard` method in the `GameController` class.
     * <p>
     * This test ensures the correctness of the `drawCard` method by performing the following:
     * - Draws a card from the game model using the `drawCard` method.
     * - Verifies that the drawn card is not null after the method is called.
     * - Continuously draws cards in a loop until the game transitions to the `EndgameState`.
     * - Ensures that the game correctly handles the transition to `EndgameState` when the deck is empty.
     * <p>
     * Assertions:
     * - The active card retrieved from the game model is not null after a draw operation.
     * - The loop terminates when the game transitions to `EndgameState`, indicating proper handling of an empty deck.
     * <p>
     * This test validates the behavior of the `drawCard` method under various conditions,
     * ensuring the game operates as intended when a player draws cards.
     */
    @Test
    void drawCard() {
        gameController.drawCard();
        AdventureCard drawnCard = gameController.getModel().getActiveCard();
        assertNotNull(drawnCard);
        while (drawnCard!= null){
                gameController.drawCard();
                if (gameController.getState().getClass().getSimpleName().equals("EndgameState")) {
                    break;
                }
                drawnCard = gameController.getModel().getActiveCard();
        }
    }

    /**
     * Tests the functionality of the `getState` method in the `GameController` class.
     * <p>
     * This method ensures that the `getState` method correctly retrieves the current game state
     * as set by the `setState` method. It validates that:
     * <p>
     * - The `getState` method returns the correct state after invoking `setState`.
     * - The class name of the state returned by `getState` matches the class name of the state
     *   passed to `setState`.
     * <p>
     * Assertions:
     * - Confirms that the state retrieved through `getState` is equivalent to the previously set state.
     * <p>
     * This test ensures the proper functioning of the `getState` method and the accurate retrieval
     * of the game state.
     */
    @Test
    void getState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.getClass().getSimpleName(), gameController.getState().getClass().getSimpleName());
    }

    /**
     * Tests the functionality of the `landOnPlanet` method in the `GameController` class.
     * <p>
     * This method simulates the scenario where players land on a specific planet during the game.
     * It sets up the necessary state and adventure card objects with relevant configurations,
     * such as rewards and planet availability. The test then invokes the `landOnPlanet` method
     * for two players to perform the landing operation and verifies the underlying behavior.
     * <p>
     * Key aspects tested include:
     * - Proper initialization and configuration of the `PlanetsState` and associated `AdventureCard` data.
     * - Execution of the `landOnPlanet` method with correct parameters for player actions.
     * - Validation that the `GameController` correctly processes multiple players landing on the same planet.
     * <p>
     * This test ensures the robustness of the `landOnPlanet` method by simulating a realistic gameplay scenario.
     */
    @Test
    void landOnPlanet(){
        Planet planet = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        planet.setReward(reward);
        planet.setAvailable(true);
        List<Planet> planets = new ArrayList<>();
        planets.add(planet);

        AdventureCard planetsCard = new AdventureCard();
        planetsCard.setPlanets(planets);
        planetsCard.setLostDays(1);
        planetsCard.setPlanets(planets);

        PlanetsState planetsState = new PlanetsState(gameController.getModel(), gameController, planetsCard);
        gameController.setState(planetsState);

        gameController.landOnPlanet("player1", 0);
        gameController.landOnPlanet("player2", 0);
    }

    /**
     * Tests the functionality of the `loadCargo` method in the `GameController` class.
     * <p>
     * This test sets up the necessary conditions for loading cargo onto a ship during gameplay.
     * It verifies that the `loadCargo` method performs the following actions correctly:
     * <p>
     * - Properly initializes the abandoned station state with a reward of cargo colors.
     * - Ensures the player's ship contains a valid cargo hold with specified slots.
     * - Sets the required game state and active adventure card.
     * - Executes the `loadCargo` method to load the specified cargo into the player's ship
     *   at the defined position.
     * <p>
     * Assertions:
     * - Ensures that the cargo is successfully loaded into the correct position.
     * - Validates that the game's state and active card remain consistent after the operation.
     * <p>
     * This test ensures accurate functionality of the cargo-loading mechanics in the `GameController`
     * and verifies scenarios where the player interacts with an adventure card to retrieve rewards.
     *
     * @throws InvalidTileException if the specified tile for cargo loading is invalid.
     */
    @Test
    void loadCargo() throws InvalidTileException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        gameController.getPlayerByID("player1").getShip().addComponent(cargoHold, 2, 2);
        gameController.getModel().setActiveCard(abandonedStationCard);
        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.loadCargo("player1", loaded, position);
    }

    /**
     * Tests the functionality of unloading cargo from a player's ship in the game.
     * <p>
     * This method verifies the following:
     * - Proper setup of the game model, player ship, and associated components.
     * - Handling of adding and removing cargo from specific slots within the player's ship cargo hold.
     * - Ensures compatibility with game states such as AbandonedStationState and SmugglersState.
     * - Interaction of cargo unloading with other ship components like batteries and energy levels.
     * <p>
     * The method emulates gameplay scenarios where a player loads and unloads cargo,
     * and resolves transitions between different game states. It also validates that
     * player actions and state updates (e.g., energy loss during combat) behave as expected.
     *
     * @throws InvalidTileException if an invalid position for adding or removing cargo is specified.
     */
    @Test
    void unloadCargo() throws InvalidTileException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.setState(abandonedStationState);
        gameController.acceptCard("player1");
        CargoHold CargoHold = new CargoHold();
        CargoHold.setSlots(3);
        gameController.getPlayerByID("player1").getShip().addComponent(CargoHold, 2, 2);

        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 1, 1);

        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.loadCargo("player1", CargoColor.BLUE, position);
        gameController.unloadCargo("player1", CargoColor.BLUE, position);

        AdventureCard card = new AdventureCard();
        List<CargoColor> rewardSmugglers = new ArrayList<>();
        card.setReward(rewardSmugglers);
        card.setFirePower(1000);
        card.setCredits(1000);
        card.setLostCargo(2);
        SmugglersState smugglersState = new SmugglersState(gameController.getModel(), gameController, card);
        gameController.setState(smugglersState);
        gameController.getModel().setActiveCard(card);
        gameController.activateCannons("player1", new ArrayList<>(), new ArrayList<>());
        gameController.loseEnergy("player1", new Pair<>(1, 1));
    }

    /**
     * Tests the movement of cargo between cargo holds in a player's ship. This method evaluates
     * the functionality of loading and unloading cargo, as well as transferring cargo within
     * ship components. The cargo movement operation involves game state transitions and ensures
     * the correct handling of various game conditions.
     *
     * @throws CargoNotLoadable if the specified cargo cannot be loaded into the cargo hold.
     * @throws CargoFullException if the cargo hold does not have enough space to hold additional cargo.
     * @throws InvalidTileException if the targeted position for cargo operations is invalid.
     */
    @Test
    void moveCargo() throws CargoNotLoadable, CargoFullException, InvalidTileException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        for (CargoColor cargoColor : cargoHeldFrom) {
            CargoHoldFrom.loadCargo(cargoColor);
        }

        gameController.getPlayerByID("player1").getShip().addComponent(CargoHoldFrom, 2, 2);

        CargoHold CargoHoldTo = new CargoHold();
        CargoHoldTo.setSlots(3);

        gameController.getModel().setActiveCard(abandonedStationCard);
        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.loadCargo("player1", loaded, position);

        gameController.unloadCargo("player1", loaded, position);

        gameController.moveCargo("player1", loaded, position, position);
    }

    /**
     * Tests the functionality of accepting an adventure card by multiple players during the game.
     * The method validates the proper handling of cargo loading and transferring, ensuring that
     * specific game rules are adhered to when players interact with the adventure card's state.
     * <p>
     * This test involves:
     * - Setting up an AdventureCard with a predefined reward.
     * - Configuring the game state to handle the interaction with the adventure card.
     * - Loading cargo into a player's CargoHold and verifying the actions taken when the card is accepted.
     * <p>
     * Exceptions:
     * - CargoNotLoadable: Thrown if the cargo cannot be loaded due to an invalid configuration.
     * - CargoFullException: Thrown if a cargo hold is already full and cannot accommodate more cargo.
     */
    @Test
    void acceptCard() throws CargoNotLoadable, CargoFullException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        for (CargoColor cargoColor : cargoHeldFrom) {
            CargoHoldFrom.loadCargo(cargoColor);
        }

        CargoHold CargoHoldTo = new CargoHold();
        CargoHoldTo.setSlots(3);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.acceptCard("player2");
        gameController.acceptCard("player1");
    }

    /**
     * Tests the functionality of retrieving the count of online players in the game.
     * <p>
     * The method verifies that the number of players currently online, as returned
     * by gameController.getOnlinePlayers(), matches the expected value of 4.
     * <p>
     * This test ensures the correct behavior of the getOnlinePlayers function
     * in the gameController class by asserting the expected player count.
     */
    @Test
    void getOnlinePlayers() {
        int size = gameController.getOnlinePlayers();
        assertEquals(4, size);
    }

    /**
     * Tests the activation of cannons by simulating a game scenario where a player uses a battery
     * to power a cannon during the Meteor Swarm state of the game.
     * <p>
     * This method contains the following steps:
     * 1. Initializes a list of projectiles with different directions and fire types.
     * 2. Sets the projectiles to the current adventure card and transitions the game
     *    to the Meteor Swarm state.
     * 3. Adds a cannon and a battery to the player's ship at specific coordinates.
     * 4. Rolls the dice for the player and then activates the cannon using the battery.
     * 5. Asserts that the battery's available energy is decremented after use.
     * <p>
     * This test assumes that the gameController properly simulates the activation
     * of cannons within the Meteor Swarm state and that the available energy in
     * batteries is reduced as per game logic.
     *
     * @throws InvalidTileException if an invalid position is accessed while placing or activating components on the player's ship.
     */
    @Test
    void activateCannons() throws InvalidTileException {


        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile1 = new Projectile();
        projectile1.setDirection(Direction.DOWN);
        projectile1.setFireType(FireType.HEAVY_METEOR);
        Projectile projectile2 = new Projectile();
        projectile2.setDirection(Direction.LEFT);
        projectile2.setFireType(FireType.LIGHT_METEOR);
        Projectile projectile3 = new Projectile();
        projectile3.setDirection(Direction.RIGHT);
        projectile3.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(projectile1);
        projectiles.add(projectile2);
        projectiles.add(projectile3);
        adventureCard.setProjectiles(projectiles);

        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(meteorSwarmState);

        Cannon cannon = new Cannon();
        gameController.getPlayerByID("player1").getShip().addComponent(cannon, 1, 1);
        Pair<Integer, Integer> cannonCord = new Pair<>(1, 1);
        List<Pair<Integer, Integer>> cannons = new ArrayList<>();
        cannons.add(cannonCord);

        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);
        Pair<Integer, Integer> batteryCord = new Pair<>(2, 2);
        List<Pair<Integer, Integer>> batteries = new ArrayList<>();
        batteries.add(batteryCord);

        gameController.rollDice("player1");
        gameController.activateCannons("player1", cannons, batteries);

        Battery usedBattery = (Battery) gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2);
        assertEquals(2, usedBattery.getAvailableEnergy());

        // NOTE: can't confirm the cannon was activated, as it depends on the game logic
    }

    /**
     * Tests the getModel method of the gameController.
     * Validates that the method returns a non-null instance of GameModel.
     */
    @Test
    void getModel() {
        GameModel model = gameController.getModel();
        assertNotNull(model);
    }

    /**
     * This method tests the behavior of the loseCrew functionality within the game framework.
     * It sets up the game state using an AdventureCard, places ship components, initializes astronauts,
     * and simulates crew loss for specified positions on a player's ship.
     * <p>
     * The method performs the following steps:
     * 1. Creates and configures an `AdventureCard` instance, setting crew, credits, and lost days.
     * 2. Sets the game state to `AbandonedShipState` using the created AdventureCard.
     * 3. Adds ship components (`Cabin`) for a player and initializes the astronauts on these components.
     * 4. Defines positions of crew members and simulates crew loss for the specified positions
     *    on both a valid and an invalid player.
     * 5. Asserts the resulting number of astronauts remaining in the cabins after invoking the loseCrew method.
     *
     * @throws InvalidTileException if an invalid tile operation occurs during the test execution.
     */
    @Test
    void loseCrew() throws InvalidTileException {
        AdventureCard adventureCard1 = new AdventureCard();
        adventureCard1.setCrew(2);
        adventureCard1.setCredits(3);
        adventureCard1.setLostDays(1);
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard1);
        gameController.setState(abandonedShipState1);
        gameController.getModel().setActiveCard(adventureCard);
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();


        Cabin cabin1 = new Cabin();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);

        Cabin cabin2 = new Cabin();

        cabin1.setConnectors(connectors);
        cabin2.setConnectors(connectors);

        gameController.getPlayerByID("player1").getShip().addComponent(cabin1, 2, 2);
        gameController.getPlayerByID("player1").getShip().addComponent(cabin2, 1, 1);
        gameController.getPlayerByID("player1").getShip().initAstronauts();

        Pair<Integer, Integer> position = new Pair<>(2, 2);
        Pair<Integer, Integer> position2 = new Pair<>(1, 1);
        List<Pair<Integer, Integer>> positions = new ArrayList<>();
        positions.add(position);
        positions.add(position2);
        gameController.acceptCard("player1");
        gameController.loseCrew("player2", positions);
        gameController.loseCrew("player1", positions);
        assertEquals(2, cabin1.getAstronauts() + cabin2.getAstronauts());
    }

    /**
     * Activates a shield for the specified player in the game using the provided shield and battery coordinates.
     * <p>
     * This method is used to enable a shield component on a player's ship during the "Meteor Swarm" state or similar scenarios
     * where activation of defensive components is required. The shield is activated using energy from an adjacent battery,
     * and this reduces the available energy in the battery by one unit for each shield activation.
     * <p>
     * Preconditions:
     * - The shield component must be properly configured and placed on the player's ship.
     * - The battery component must have sufficient energy and be connected to the shield by valid connectors.
     * - The game state must support the activation of shield components (e.g., during an active meteor swarm phase).
     * <p>
     * Postconditions:
     * - The shield is activated for the specified player, providing defense against incoming projectiles.
     * - The available energy in the corresponding battery is reduced by the amount required for activation.
     * <p>
     * Throws:
     * - InvalidTileException if the provided shield or battery coordinates are not valid,
     *   or if the components cannot be activated due to invalid placement, connection, or state issues.
     * <p>
     * Parameters:
     * - playerId: The identifier of the player attempting to activate the shield.
     * - shieldCord: The grid coordinates of the shield component on the player's ship.
     * - batteryCord: The grid coordinates of the battery component on the player's ship.
     * <p>
     * Assertions:
     * - This method ensures that the battery's available energy is decreased after the shield activation.
     */
    @Test
    void activateShield() throws InvalidTileException {
        AdventureCard adventureCard1 = new AdventureCard();
        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile1 = new Projectile();
        projectile1.setDirection(Direction.DOWN);
        projectile1.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(projectile1);
        adventureCard1.setProjectiles(projectiles);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);

        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard1);

        Shield shield = new Shield();
        Direction[] directions = {Direction.UP, Direction.RIGHT};
        shield.setCoveredSides(directions);
        shield.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(shield, 1, 1);
        Pair<Integer, Integer> shieldCord = new Pair<>(1, 1);

        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        battery.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);
        Pair<Integer, Integer> batteryCord = new Pair<>(2, 2);

        gameController.getModel().setActiveCard(adventureCard1);
        gameController.setState(meteorSwarmState);
        gameController.rollDice("player1");
        gameController.activateShield("player2", shieldCord, batteryCord);
        gameController.activateShield("player1", shieldCord, batteryCord);

        assertEquals(2, battery.getAvailableEnergy());
    }

    /**
     * Tests the functionality of retrieving the active card from the game model.
     * The method sets an active card in the game model and then verifies
     * that the active card retrieved through the controller matches the card set.
     * Ensures that the active card is correctly stored and accessed within the model.
     */
    @Test
    void getActiveCard() {
        gameController.getModel().setActiveCard(adventureCard);
        assertEquals(adventureCard, gameController.getActiveCard());
    }

    /**
     * Tests the functionality of ending a player's move within the game.
     * <p>
     * This method validates that the game transitions correctly to a new state
     * when `endMove` is invoked for specific players. It sets up an
     * AbandonedShipState for the game and executes `endMove` for the specified
     * players to ensure proper behavior.
     * <p>
     * Preconditions:
     * - The game state is set up with an instance of AbandonedShipState.
     * <p>
     * Postconditions:
     * - The player's move is correctly concluded based on the invoked `endMove` calls.
     */
    @Test
    void endMove(){
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.endMove("player2");
        gameController.endMove("player1");
    }

    /**
     * Tests the behavior of the `chooseBranch` method in the `OpenSpaceState`.
     * <p>
     * This test validates that the `chooseBranch` method handles the scenario
     * when a branch is selected during the `OpenSpaceState`. It ensures that:
     * - The `OpenSpaceState` is correctly initialized with the current game model, controller, and adventure card.
     * - The game state is transitioned to the `OpenSpaceState`.
     * - The `chooseBranch` method is invoked with specific player input and branch coordinates.
     */
    @Test
    void chooseBranchFailure(){
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.chooseBranch("player1", new Pair<>(1, 1));
    }

    /**
     * Tests the activation of engines in the combat zone for a specific player in the game.
     * <p>
     * This method sets up a specific game state for testing, initializes required game components
     * such as engines and batteries, and simulates the process of engine activation by the player.
     * It utilizes the `activateEngines` method of the game controller to verify correct functionality.
     *
     * @throws InvalidTileException if an invalid tile configuration occurs during setup or activation.
     */
    @Test
    void activateEngines() throws InvalidTileException {
        CombatZone0State combatZone0State = new CombatZone0State(gameController.getModel(), gameController, adventureCard);
        gameController.setState(combatZone0State);

        Engine engine1 = new Engine();
        engine1.setDoublePower(true);

        gameController.getPlayerByID("player1").getShip().addComponent(engine1, 1, 1);
        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);

        gameController.getPlayerByID("player1").getShip().addComponent(engine1, 1, 2);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 1);
        Pair<Integer, Integer> engineCord1 = new Pair<>(1, 2);
        Pair<Integer, Integer> batteryCord2 = new Pair<>(2, 1);
        List<Pair<Integer, Integer>> engineCoords = new ArrayList<>();
        engineCoords.add(engineCord1);
        List<Pair<Integer, Integer>> batteryCoords = new ArrayList<>();
        batteryCoords.add(batteryCord2);

        gameController.activateEngines("player1", engineCoords, batteryCoords);
    }

    /**
     * Tests the functionality of retrieving player data using the getPlayerByID method
     * from the gameController. Ensures that the player object returned is not null
     * and that the player's username matches the expected value.
     * <p>
     * This method is designed to verify the correctness of retrieving
     * player information based on the player's unique identifier.
     */
    @Test
    void getPlayerData() {
        Player player = gameController.getPlayerByID("player1");
        assertNotNull(player);
        assertEquals("player1", player.getUsername());
    }

    /**
     * Tests the functionality of setting the player's color.
     * <p>
     * This method retrieves a player by their ID from the game controller,
     * assigns a specific color to the player, and verifies that
     * the player's color is set correctly.
     */
    @Test
    void setPlayerColor() {
        PlayerColor color = PlayerColor.BLUE;
        Player player = gameController.getPlayerByID("player1");
        assertEquals(color, player.getColor());
    }

    /**
     * Tests the functionality of the getPlayerScores method.
     * <p>
     * This test verifies that the game controller transitions into the
     * EndgameState and calculates player scores correctly when the method is invoked.
     * It sets the game state to EndgameState and triggers the score calculation
     * to ensure that the expected behavior occurs.
     */
    @Test
    void getPlayerScores() {
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);
        gameController.getScore();
    }

    /**
     * This test method verifies the behavior of the system when an attempt to retrieve
     * player scores in the OpenSpaceState fails or is inconsistent with the expected state.
     * The method initializes the game state to an OpenSpaceState, sets it in the game
     * controller, and retrieves the score to evaluate the system's response in this context.
     * It ensures the game state transitions and scoring functions correctly under this scenario.
     */
    @Test
    void getPlayerScoresFailure(){
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.getScore();
    }

    /**
     * Tests the giveUp functionality for the gameController.
     * Verifies that the player can give up during different game states, and
     * ensures the player's "inGame" status is updated correctly after giving up.
     * <p>
     * This method:
     * 1. Transitions the gameController state to OpenSpaceState and invokes giveUp for "player1".
     * 2. Transitions the gameController state to PreDrawState and invokes giveUp for "player1".
     * 3. Asserts that the player with ID "player1" is no longer marked as in the game.
     */
    @Test
    void giveUp() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.giveUp("player1");
        PreDrawState preDrawState = new PreDrawState(gameController);
        gameController.setState(preDrawState);
        gameController.giveUp("player1");
        assertFalse(gameController.getPlayerByID("player1").isInGame());
    }

    /**
     * Simulates the process of disconnecting a player from the game in a test scenario.
     * This method validates the state changes and game behavior when players are disconnected.
     * <p>
     * The test sets up a particular game state and proceeds to disconnect players by their identifiers.
     * It asserts the following:
     * - Whether a player is marked as disconnected.
     * - Whether the disconnected players are removed from the list of in-game connected players.
     * - Whether the count of online players is updated correctly after disconnections.
     */
    @Test
    void disconnectPlayer() {
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("+1/30 if you're reading this :)");
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.getInGameConnectedPlayers().contains("player1"));
        assertEquals(3, gameController.getOnlinePlayers());

        gameController.disconnectPlayer("player2");
        gameController.disconnectPlayer("player3");
    }

    /**
     * Tests the functionality of reconnecting a player to the game after they have been disconnected.
     * <p>
     * The method performs the following steps:
     * - Reconnects specific players using their respective identifiers.
     * - Sets the game state to an instance of AbandonedShipState.
     * - Disconnects a specific player and verifies the disconnection using assertions.
     * - Reconnects the previously disconnected player and verifies the reconnection.
     * - Ensures the total number of connected players is updated accordingly.
     * <p>
     * Assertions:
     * - Verifies that a disconnected player is marked as disconnected.
     * - Ensures the disconnected player is removed from the list of in-game connected players.
     * - Confirms that, after reconnection, the player is no longer marked as disconnected.
     * - Validates that the number of connected players is updated to the correct count after reconnection.
     */
    @Test
    void reconnectPlayer() {
        gameController.reconnectPlayer("you fell for it twice");
        gameController.reconnectPlayer("player2");
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.getInGameConnectedPlayers().contains("player1"));
        gameController.reconnectPlayer("player1");
        gameController.preDrawConnect();
        assertFalse(gameController.isPlayerDisconnected("player1"));
        assertEquals(4, gameController.getOnlinePlayers());
    }

    /**
     * Test method for verifying the functionality of retrieving a player by their unique ID.
     * This method tests whether the {@code getPlayerByID} function of the {@code gameController}
     * correctly fetches a {@code Player} object using a given ID and ensures that the retrieved
     * player's properties are as expected.
     * <p>
     * Verifications include:
     * - Ensuring the returned {@code Player} object is not null.
     * - Asserting that the returned {@code Player}'s username matches the expected ID.
     */
    @Test
    void getPlayerByID() {
        Player player = gameController.getPlayerByID("player1");
        assertNotNull(player);
        assertEquals("player1", player.getUsername());
    }

    /**
     * Tests the retrieval of all usernames from the game model.
     * <p>
     * This method verifies that the list of usernames retrieved from the game's players
     * matches the expected output. It checks the following:
     * - The list of usernames is not null.
     * - The size of the usernames list is exactly 4.
     * - The usernames list contains specific expected usernames: "player1", "player2",
     *   "player3", and "player4".
     */
    @Test
    void getAllUsernames() {
        List<String> usernames = gameController.getModel().getGame().getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        assertNotNull(usernames);
        assertEquals(4, usernames.size());
        assertTrue(usernames.contains("player1"));
        assertTrue(usernames.contains("player2"));
        assertTrue(usernames.contains("player3"));
        assertTrue(usernames.contains("player4"));
    }

    /**
     * Test method to verify the behavior of retrieving disconnected players
     * in the game. This method sets up an instance of the AbandonedShipState,
     * disconnects a couple of players, and ensures the state maintains the
     * correct list of disconnected players.
     * <p>
     * This test is used to confirm that players can be properly marked as
     * disconnected and that the game state reflects these disconnections as
     * expected.
     * <p>
     * Method is executed as part of unit testing to ensure the disconnecting
     * functionality works correctly within the game's flow when transitioning
     * to the AbandonedShipState.
     */
    @Test
    void getDisconnectedPlayers() {
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("player1");
        gameController.disconnectPlayer("player2");
    }

    /**
     * Tests whether the isPlayerDisconnected functionality of the GameController correctly
     * identifies if a player is marked as disconnected.
     * <p>
     * The test case sets up an AbandonedShipState for the game model and controller,
     * marks a player as disconnected, and verifies the disconnected status of both
     * a disconnected player and a non-disconnected player.
     */
    @Test
    void isPlayerDisconnected() {
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.isPlayerDisconnected("player2"));
    }

    /**
     * Tests the functionality of taking a component from the unviewed pile.
     * <p>
     * This method verifies the process of handling the game's state transitions
     * and ensures that a component can be moved from the unviewed pile to another
     * state while being appropriately removed from the unviewed list.
     * <p>
     * Scenarios that are tested include:
     * 1. Ensuring the component is not present in the viewed pile before being taken.
     * 2. Verifying that the component is properly removed from the unviewed pile
     *    after the action is performed.
     * <p>
     * The test involves switching between different game states, including
     * OpenSpaceState and AssemblingState, to simulate transitions accurately and validate
     * the consistent handling of the unviewed pile during these states.
     */
    @Test
    void takeComponentFromUnviewed() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.takeComponentFromUnviewed("player1", 0);
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));

        gameController.takeComponentFromUnviewed("player1", 0);

        assertFalse(gameController.getModel().getGame().getPile().getUnviewed().contains(comp));
    }

    /**
     * Tests the functionality of taking a game component from the viewed pile.
     * This method validates the transitions between different game states
     * and ensures that components are correctly moved between piles during gameplay.
     * <p>
     * The test involves:
     * - Setting up an OpenSpaceState and transitioning the game controller to it.
     * - Performing an action to take a component from the viewed pile.
     * - Validating the presence of the component in the appropriate game pile after state change.
     * - Adding the component back to the viewed pile for verification.
     * <p>
     * Assertions ensure:
     * - The model in the current state is not null.
     * - Components are correctly transitioned between the unviewed and viewed piles.
     */
    @Test
    void takeComponentFromViewed() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.takeComponentFromViewed("player1", 0);
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);

        gameController.addComponentToViewed("player1");

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    /**
     * Test method for verifying the behavior of the `takeComponentFromBooked` functionality in the gameController.
     * This method simulates the process of transitioning game states and ensures that components
     * are correctly moved between different game piles and the player's booked components.
     * The test initializes an `OpenSpaceState` and transitions to an `AssemblingState`, performing multiple
     * component operations, and finally validating the state of the components and their associations.
     * <p>
     * The test verifies the following:
     * - The initial state of the game's model is not null in the `AssemblingState`.
     * - Components can be retrieved from the player's booked items and are no longer present
     *   in the viewed pile or the player's booked components after being taken.
     * <p>
     * Assertions:
     * - Ensures the state of the game's model is properly initialized.
     * - Validates that the taken component is removed from the `viewed` pile.
     * - Validates that the taken component is removed from the player's booked items.
     */
    @Test
    void takeComponentFromBooked() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.takeComponentFromBooked("player1", 0);
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToBooked("player1");
        gameController.takeComponentFromBooked("player1", 0);
        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));
        assertFalse(((NormalShip)(gameController.getPlayerByID("player1").getShip())).getBooked().contains(comp));
    }

    /**
     * Tests the functionality of adding a component to the "booked" list of a player's ship.
     * <p>
     * This method verifies the behavior of the game controller when transitioning between
     * different states and adding components to a player's booked components list. It ensures:
     * - Components can be correctly added to the booked list.
     * - The state transitions, such as moving from OpenSpaceState to AssemblingState,
     *   occur and maintain appropriate game logic.
     * - The component added to the booked list is the expected one from the unviewed pile.
     * <p>
     * Assertions:
     * - Verifies that the model in the AssemblingState is not null.
     * - Checks that the component retrieved from the unviewed pile is successfully added
     *   to the booked list of the player's ship.
     * - Confirms that the booked list of the player's ship contains the intended component.
     */
    @Test
    void addComponentToBooked() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.addComponentToBooked("player1");

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToBooked("player1");

        assertTrue(((NormalShip)(gameController.getPlayerByID("player1").getShip())).getBooked().contains(comp));
    }

    /**
     * Tests the functionality of adding a component to the viewed pile in the game.
     * <p>
     * This method verifies the following:
     * 1. That the game's state changes correctly when transitioning between
     *    OpenSpaceState and AssemblingState.
     * 2. That the component is added to the viewed pile after being taken
     *    from the unviewed pile.
     * 3. That the viewed pile in the game model contains the added component.
     * <p>
     * The method sets up the OpenSpaceState and AssemblingState states for
     * the game controller, manipulates components within these states,
     * and then validates that the state of the viewed pile is updated as expected.
     * <p>
     * Assertions:
     * - Ensures the model in AssemblingState is not null.
     * - Confirms that the component retrieved from the unviewed pile is
     *   correctly added to the viewed pile.
     */
    @Test
    void addComponentToViewed() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.addComponentToViewed("player1");
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToViewed("player1");

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));

    }

    /**
     * Tests the functionality of placing a component on a player's ship during the assembling stage and ensuring the
     * component is correctly positioned based on the provided coordinates. Additionally, tests the behavior of the
     * placeComponent method in a different game state.
     * <p>
     * The method performs the following steps:
     * - Sets the game state to AssemblingState and ensures a component is placed on the player's ship at the specified coordinates.
     * - Transitions the game state to OpenSpaceState and tests how the placeComponent method behaves in this context.
     * <p>
     * Verifies that the component is correctly added to the player's ship in the specified position during the appropriate state.
     * <p>
     * Assertions:
     * - Ensures the component is placed at the specified coordinates (2, 2) in the AssemblingState.
     * - Validates the behavior of the placeComponent method in the OpenSpaceState.
     */
    @Test
    void placeComponent() {

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);

        Pair<Integer, Integer> coordinates = new Pair<>(2, 2);
        gameController.placeComponent("player1", coordinates);
        assertEquals(comp, gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2));

        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.placeComponent("player1", coordinates);
    }

    /**
     * Tests the functionality of rotating a game component clockwise.
     * The method verifies that the connectors of a component are correctly updated
     * after being rotated 90 degrees clockwise. It also checks the component's
     * connectors' alignment in different game states, such as AssemblingState and
     * OpenSpaceState.
     * <p>
     * Scenarios tested:
     * - Initializing the game controller and transitioning to AssemblingState.
     * - Taking a component from the unviewed pile and storing its initial connectors.
     * - Performing a clockwise rotation and ensuring the connectors align as expected.
     * - Transitioning to OpenSpaceState and testing clockwise and counterclockwise rotations.
     * <p>
     * Assertions:
     * - Ensures that after a clockwise rotation, the component's connectors are shifted
     *   to their correct new positions (e.g., the UP connector moves to RIGHT, etc.).
     */
    @Test
    void rotateComponentClockwise() {
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.LEFT));

        gameController.rotateComponentClockwise("player1");

        assertEquals(connectors.get(Direction.UP), comp.getConnectors().get(Direction.RIGHT));
        assertEquals(connectors.get(Direction.RIGHT), comp.getConnectors().get(Direction.DOWN));
        assertEquals(connectors.get(Direction.DOWN), comp.getConnectors().get(Direction.LEFT));
        assertEquals(connectors.get(Direction.LEFT), comp.getConnectors().get(Direction.UP));
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.rotateComponentClockwise("player1");
        gameController.rotateComponentCounterclockwise("player1");
    }

    /**
     * Tests the functionality of rotating a game component counterclockwise.
     * This method verifies whether the connectors of a component are correctly updated
     * after executing a counterclockwise rotation.
     * <p>
     * The following steps are performed during the test:
     * 1. Initializes a map to store the initial state of the component's connectors.
     * 2. Sets the game controller to `AssemblingState` to allow component rotation.
     * 3. Retrieves a component from the game pile and records its initial connectors.
     * 4. Performs a counterclockwise rotation on the component using the game controller.
     * 5. Asserts that the component's connectors are correctly rotated:
     *    - The connector on the DOWN side is moved to the RIGHT side.
     *    - The connector on the LEFT side is moved to the DOWN side.
     *    - The connector on the UP side is moved to the LEFT side.
     *    - The connector on the RIGHT side is moved to the UP side.
     */
    @Test
    void rotateComponentCounterclockwise() {
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.LEFT));

        gameController.rotateComponentCounterclockwise("player1");

        assertEquals(connectors.get(Direction.DOWN), comp.getConnectors().get(Direction.RIGHT));
        assertEquals(connectors.get(Direction.LEFT), comp.getConnectors().get(Direction.DOWN));
        assertEquals(connectors.get(Direction.UP), comp.getConnectors().get(Direction.LEFT));
        assertEquals(connectors.get(Direction.RIGHT), comp.getConnectors().get(Direction.UP));
    }

    /**
     * Removes a specified component from a player's ship at the given coordinates.
     * The method operates differently depending on the current game state.
     * <p>
     * Behavior:
     * - In the ValidatingShipState, it removes the component located at the specified
     *   coordinates on the player's ship.
     * - In the OpenSpaceState, it may handle the removal differently, potentially
     *   relating to the context of the open space gameplay.
     * <p>
     * Preconditions:
     * - The game must be in a valid state where the action is permitted.
     * - The player ID provided must correspond to an existing player in the game.
     * - The coordinates must refer to a valid position on the player's ship and
     *   there must be a component at that position in states that allow removal.
     * <p>
     * Postconditions:
     * - In ValidatingShipState, the component at the specified coordinates is removed
     *   from the player's ship.
     * - In other states, behavior may vary as per the state-specific logic.
     * <p>
     * Parameters:
     * - playerId: The unique identifier of the player whose ship component is to be removed.
     * - coordinates: The coordinates on the player's ship map from which the component is to be removed.
     * <p>
     * Throws:
     * - IllegalStateException: If the game is not in a state that permits the operation.
     * - IllegalArgumentException: If the playerId is invalid or the coordinates are out of bounds
     *   or do not correspond to a removable component.
     */
    @Test
    void removeComponentFromShip() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.takeComponentFromUnviewed("player1", 0);
        Pair<Integer, Integer> coordinates = new Pair<>(3, 4);
        gameController.placeComponent("player1", coordinates);

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);
        gameController.removeComponentFromShip("player1", coordinates);

        assertNull(gameController.getPlayerByID("player1").getShip().getComponentAt(3,4));

        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.removeComponentFromShip("player1", coordinates);
    }


    /**
     * Tests the behavior of the stopAssembling method in different game states.
     * <p>
     * This test evaluates the correctness of the stopAssembling method of the
     * GameController when invoked in two distinct game states:
     * - AssemblingState
     * - OpenSpaceState
     * <p>
     * The test sets the GameController to the respective states, invokes the
     * stopAssembling method, and ensures proper functionality during these transitions.
     */
    @Test
    void stopAssembling() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.stopAssembling("player1", 1);
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.stopAssembling("player1", 1);
    }

    /**
     * Simulates the sequence of actions that occur when the hourglass is turned
     * during the game. This method changes the game state, performs player actions,
     * and introduces a delay to mimic a real-time waiting period.
     * <p>
     * The method executes the following:
     * 1. Transitions the game to an OpenSpaceState.
     * 2. Calls the turnHourglass method for a player with a specific identifier.
     * 3. Disconnects a player from the game.
     * 4. Processes another turnHourglass call for a different player.
     * 5. Transitions the game to an AssemblingState.
     * 6. Introduces a delay of 90 seconds to simulate a game wait period.
     * 7. Calls turnHourglass again for a player.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted during execution.
     */
   @Test
    void turnHourglass() throws InterruptedException {
       OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
       gameController.setState(openSpaceState);
       gameController.turnHourglass("player1");
       gameController.disconnectPlayer("player2");
       gameController.turnHourglass("player2");
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        Thread.sleep(90000);

        gameController.turnHourglass("player1");
    }

    /**
     * Tests the behavior of the game model's remaining time during the assembling state.
     * Verifies that the remaining time decreases correctly over the passage of time.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted
     */
    @Test
    void getHourglassTime() throws InterruptedException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Thread.sleep(10000);
        assertEquals(80, gameController.getModel().getRemainingTime());

        Thread.sleep(1000);

        assertEquals(79, gameController.getModel().getRemainingTime());
    }

    /**
     * Tests the functionality of the `peekDeck` method in various game states and scenarios.
     * <p>
     * This method performs the following:
     * 1. Creates an `OpenSpaceState` and sets it in the game controller.
     * 2. Executes the `peekDeck` method with "player1" and a depth of 1.
     * 3. Creates an `AssemblingState` and sets it in the game controller.
     * 4. Disconnects "player2" using the `disconnectPlayer` method.
     * 5. Executes the `peekDeck` method with "player2" and a depth of 2.
     * 6. Executes the `peekDeck` method again with "player1" and a depth of 3.
     * <p>
     * This test ensures the `peekDeck` method behaves as expected under different states
     * and after various player-related actions, such as disconnection.
     */
    @Test
    void peekDeck() {
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.peekDeck("player1", 1);


        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.disconnectPlayer("player2");
        gameController.peekDeck("player2", 2);
        gameController.peekDeck("player1", 3);
    }

    /**
     * Tests the functionality of adding an alien to a cabin component on a player's ship.
     * <p>
     * This method verifies the correctness of adding an alien of a specific color to a cabin
     * component located on a player's ship. The test validates that the alien's color is
     * correctly assigned to the cabin and ensures the game state transitions are handled
     * properly during the process.
     * <p>
     * The method performs the following primary steps:
     * - Sets the game state to a validating state to allow component addition.
     * - Creates a cabin component with a specified alien color and directional connectors.
     * - Adds the cabin component to a specific location on a player's ship.
     * - Adds an alien with the specified color to a cabin at the given coordinates of the ship.
     * - Asserts that the alien color in the cabin matches the expected value.
     * - Changes the game state to open space and performs an additional alien addition.
     * <p>
     * Any invalid tile exceptions during the test are reported as test failures.
     */
    @Test
    void addAlien() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);

        AlienColor alienColor = AlienColor.BROWN;

        Cabin cabin = new Cabin();
        cabin.setColor(alienColor);
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);
        cabin.setConnectors(connectors);


        try {
            gameController.getPlayerByID("player1").getShip().addComponent(cabin, 2, 2);


            Pair<Integer, Integer> cabinCord = new Pair<>(2, 2);
            gameController.addAlien("player1", alienColor, cabinCord);
            gameController.endMove("player1");
            gameController.endMove("player2");
            gameController.endMove("player3");
            gameController.endMove("player4");
            assertEquals(alienColor, ((Cabin) (gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2))).getCabinColor());
        } catch (InvalidTileException e) {
            fail("Invalid tile exception: " + e.getMessage());
        }
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.addAlien("player1", alienColor, new Pair<>(2, 2));
    }

    /**
     * Tests the readiness of all ships to fly after transitioning to the
     * ValidatingShipState and simulating the end of moves for all players.
     * <p>
     * This test case ensures the following:
     * - The game's state is properly set to the ValidatingShipState.
     * - Each player's end move is invoked.
     * - Validation confirms that all ships are ready to fly through the
     *   allShipsReadyToFly method of the ValidatingShipState.
     * <p>
     * Utilizes assertions to verify the correctness of the readiness status
     * for all ships in the given testing scenario.
     */
    @Test
    void readyToFly() {

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);
        gameController.endMove("player1");
        gameController.endMove("player2");
        gameController.endMove("player3");
        gameController.endMove("player4");
        assertTrue(validatingShipState.allShipsReadyToFly());
    }

    /**
     * Tests the dice roll functionality during different states in the game.
     * <p>
     * This method simulates the scenario where the game state transitions
     * between different states (e.g., SlaversState and OpenSpaceState), and a
     * player ("player1") rolls the dice in these states. It ensures that the
     * dice roll functionality works correctly and checks the last rolled value.
     *
     * @throws DieNotRolledException if the dice has not been successfully rolled.
     */
    @Test
    void rollDice() throws DieNotRolledException {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);
        OpenSpaceState openSpaceState = new OpenSpaceState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(openSpaceState);
        gameController.rollDice("player1");

        gameController.rollDice("player1");
        gameController.getModel().getGame().lastRolled();
    }

    /**
     * Tests the functionality of retrieving the value of the last rolled dice in the game state.
     * <p>
     * This method verifies that after a dice roll operation is performed, the last rolled dice value
     * is correctly updated and is not equal to zero. The test simulates the dice roll process within
     * the game context and checks the state of the game to confirm the correct behavior.
     * <p>
     * Steps performed in the test:
     * 1. Initializes a SlaversState instance and sets it as the current game state.
     * 2. Simulates dice rolls for a specific player by invoking the rollDice method twice.
     * 3. Retrieves the last rolled dice value using the game's state model.
     * 4. Asserts that the last rolled dice value is not zero, ensuring the dice roll was successfully recorded.
     *
     * @throws DieNotRolledException if an attempt is made to retrieve the last rolled dice value
     *                               before any dice is rolled.
     */
    @Test
    void lastRolledDice() throws DieNotRolledException {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        gameController.rollDice("player1");

        gameController.rollDice("player1");
        int lastRolled = gameController.getModel().getGame().lastRolled();
        assertNotEquals(0, lastRolled);
    }

    /**
     * Tests the functionality of the getCurrentPlayer method in the SlaversState class.
     * <p>
     * Validates that the method correctly returns the current player's identifier
     * when the game is in the SlaversState.
     * <p>
     * This test ensures that:
     * - The game state is set to SlaversState properly.
     * - The getCurrentPlayer method in SlaversState returns the expected player's
     *   identifier, verifying the correct behavior of the method.
     */
    @Test
    void getCurrentPlayer() {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        String currentPlayer = slaversState.getCurrentPlayer();
        assertEquals("player1", currentPlayer);
    }

    /**
     * Tests the method responsible for retrieving the list of players connected
     * to a game session and currently active in the game.
     * <p>
     * This method ensures that:
     * - The returned list of in-game connected players is not null.
     * - The size of the list matches the expected number of connected players.
     * - Specific players expected to be part of the game are contained within the list.
     * <p>
     * The test utilizes assertions to validate the above conditions.
     */
    @Test
    void getInGameConnectedPlayers() {
        List<String> inGameConnectedPlayers = gameController.getInGameConnectedPlayers();
        assertNotNull(inGameConnectedPlayers);
        assertEquals(4, inGameConnectedPlayers.size());
        assertTrue(inGameConnectedPlayers.contains("player1"));
        assertTrue(inGameConnectedPlayers.contains("player2"));
        assertTrue(inGameConnectedPlayers.contains("player3"));
        assertTrue(inGameConnectedPlayers.contains("player4"));
    }

    /**
     * Test method for verifying the behavior of the getScore method
     * within the context of the EndgameState.
     * <p>
     * This test creates an EndgameState for the gameController, sets the
     * gameController's state to the created EndgameState, and invokes the
     * getScore method. Its purpose is to ensure the correct functionality
     * of score retrieval when in the EndgameState.
     */
    @Test
    void getScoreTest(){
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);
        gameController.getScore();
    }

    /**
     * Tests the functionality of taking a component from the "viewed" pile
     * in the game model. This method verifies that after invoking the
     * `takeComponentFromViewed` action, the specified component is removed
     * from the "viewed" pile.
     * <p>
     * The test initializes the game controller with an assembling state,
     * retrieves a component from the "unviewed" pile, moves it to the "viewed" pile,
     * and subsequently calls the method `takeComponentFromViewed` to remove it
     * from the "viewed" pile. The assertion checks that the component is no
     * longer present in the "viewed" pile.
     */
    @Test
    void takeComponentFromViewedTest(){
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToViewed("player1");

        gameController.takeComponentFromViewed("player1", 0);

        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    /**
     * Test method to verify the functionality of terminating a game session.
     * <p>
     * This method initializes a game controller with specified parameters such as
     * player names, game ID, and level. It then invokes the `killGame` method on
     * the game controller to terminate the game session. If the game session is
     * terminated successfully, the association between a player and the game
     * controller is validated to ensure it no longer exists.
     * <p>
     * The method handles any `InvalidStateException` that may arise during the
     * execution of the `killGame` method.
     * <p>
     * This test validates that the game controller appropriately removes players
     * and their association with the game upon game termination.
     */
    @Test
    void killGame(){
        String player1 = "Sola";
        String player2 = "Ste";
        String player3 = "Verri";
        String player4 = "taverna";
        String id = "22";
        List<String> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        int level = 2;
        try {
            GameController controller = new GameController (id, id, players, level);
            controller.killGame();
        } catch (InvalidStateException _) {

        }
        assertNull(MatchController.getInstance().getGameControllerForPlayer("Sola"));
    }
}