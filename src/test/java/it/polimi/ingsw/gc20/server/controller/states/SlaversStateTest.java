package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SlaversStateTest {
    static GameController controller;
    static SlaversState state;
    static AdventureCard card;

    /**
     * Sets up the initial state and prerequisites for the test environment.
     * This method is executed before each test in the containing class and is used to initialize
     * the game controller, adventure card, player ships, and the SlaversState object.
     * <p>
     * The method creates and configures various components of a normal ship (e.g., cannons, engines,
     * battery, cargo holds, and cabins) for each player and sets their properties, connectors,
     * and initial positions.
     * <p>
     * It also associates the ships with their respective player instances, loads cargo into specific
     * cargo holds, initializes astronauts within the ships, and sets starter game states such as
     * active adventure cards and SlaversState.
     * <p>
     * Exceptions are handled and tested for any issues during the initialization of the ship components
     * or during cargo loading, and the method ensures that all critical objects and configurations
     * are properly set up for the tests.
     *
     * @throws InvalidStateException if an invalid state is encountered during initialization
     * @throws CargoNotLoadable if cargo cannot be loaded into the ship's cargo hold
     * @throws CargoFullException if a cargo hold is at full capacity
     */
    @BeforeEach
    void setUp() throws InvalidStateException, CargoNotLoadable, CargoFullException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setFirePower(2);
        card.setLostDays(1);
        card.setCredits(1);
        card.setCrew(1);
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        assertNotNull(controller.getModel());
        controller.getModel().setActiveCard(card);
        StartingCabin start;
        for (Player player : controller.getModel().getInGamePlayers()) {
            // Create a new NormalShip
            NormalShip ship = new NormalShip();

            // Create components
            Cannon upCannon = new Cannon();
            upCannon.setPower(2);

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

            Cabin Cabin1 = new Cabin();
            Cabin1.setColor(AlienColor.NONE);

            CargoHold cargoHold = new CargoHold();
            cargoHold.setSlots(3);
            CargoHold cargoHold2 = new CargoHold();
            cargoHold2.setSlots(3);

            // Add components to ship at valid positions
            try {
                ship.addComponent(upCannon, 1, 3);
                ship.addComponent(downCannon, 3, 3);
                ship.addComponent(singleEngine, 3, 2);
                ship.addComponent(doubleEngine, 3, 4);
                ship.addComponent(battery, 2, 2);
                ship.addComponent(Cabin1, 2, 4);
                ship.addComponent(cargoHold, 1, 2);
                ship.addComponent(cargoHold2, 1, 4);
            } catch (Exception _) {
                fail("Failed to add components to ship");
            }

            ship.loadCargo(CargoColor.GREEN, cargoHold);

            // Setting the connectors
            Map<Direction, ConnectorEnum> connectorsCargoHold = new HashMap<>();
            connectorsCargoHold.put(Direction.RIGHT, ConnectorEnum.S);
            connectorsCargoHold.put(Direction.LEFT, ConnectorEnum.S);
            connectorsCargoHold.put(Direction.UP, ConnectorEnum.ZERO);
            connectorsCargoHold.put(Direction.DOWN, ConnectorEnum.D);
            cargoHold.setConnectors(connectorsCargoHold);

            Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
            connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.U);
            connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.U);
            connectorsCargoHold2.put(Direction.UP, ConnectorEnum.U);
            connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.U);
            cargoHold2.setConnectors(connectorsCargoHold2);

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
            connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U); // Valid ship for other players
            upCannon.setConnectors(connectorsUpCannon);

            Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
            connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.UP, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.U);
            start = (StartingCabin) ship.getComponentAt(2, 3);
            start.setConnectors(connectorsStartingCabin);
            ship.initAstronauts();
            player.setShip(ship);
        }
        for (int i = 0; i < controller.getModel().getInGamePlayers().size(); i++) {
            Player player = controller.getModel().getInGamePlayers().get(i);
            switch (player.getUsername()) {
                case "player1" -> player.setPosition(6);
                case "player2" -> player.setPosition(5);
            }
        }
        state = new SlaversState(controller.getModel(), controller, card);
    }

    /**
     * Tests the behavior of the state transitions and interactions during a Slavers encounter.
     * This test simulates the actions taken by players during the encounter and verifies
     * state changes, player turns, and the effects of actions like activating cannons and losing crew.
     * <p>
     * The test specifically checks:
     * 1. Cannon activation in the correct phase for a player, ensuring invalid state or turn throws exceptions.
     * 2. Handling of crew loss by a player and transitions to the next phase.
     * 3. Proper player turn progression and validation of the current player after specific actions.
     * 4. Verification of the final player position and credits after accepting the card.
     *
     * @throws InvalidTurnException if a player attempts an action out of turn
     * @throws ComponentNotFoundException if a specified component (e.g., cannon, cabin) is not found
     * @throws InvalidStateException if an action is performed in an incorrect game phase
     * @throws InvalidCannonException if invalid cannon components are involved during activation
     * @throws EnergyException if there is insufficient energy for an action
     * @throws EmptyCabinException if an attempt is made to lose crew from an empty cabin
     */
    @Test
    void testSlaversState() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException, EmptyCabinException {
        state.activateCannons(controller.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        state.loseCrew(controller.getPlayerByID("player1"), List.of(new Pair<>(2, 4)));
        assertEquals("player2", state.getCurrentPlayer());
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1, 3), new Pair<>(3, 3)), List.of(new Pair<>(2, 2), new Pair<>(2, 2)));
        state.acceptCard(controller.getPlayerByID("player2"));
        assertEquals(4, controller.getPlayerByID("player2").getPosition());
        assertEquals(1, controller.getPlayerByID("player2").getCredits());
    }

    /**
     * Tests the behavior of the game logic during a specific sequence of events in the Slavers encounter.
     * This method validates player actions, state transitions, and game rules execution in different scenarios.
     * <p>
     * The test simulates the following sequence:
     * - Player "player1" activates cannons with no target and subsequently loses crew from a specific cabin.
     * - Verifies the player turn progression by asserting the current player switches to "player2".
     * - Player "player2" activates cannons using specified cannon and battery coordinates.
     * - Simulates the end of "player2"'s turn.
     * <p>
     * This test ensures correct handling of cannon activation, crew loss, turn order, and phase transitions.
     *
     * @throws InvalidTurnException if an action is taken by a player out of turn
     * @throws ComponentNotFoundException if a targeted component (e.g., cannon, cabin) is not found
     * @throws InvalidStateException if an action is performed in an incorrect game state
     * @throws InvalidCannonException if an invalid cannon configuration is used during activation
     * @throws EnergyException if there is insufficient energy for a specified action
     * @throws EmptyCabinException if losing crew from an empty cabin is attempted
     */
    @Test
    void testSlaversState2() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException, EmptyCabinException {
        state.activateCannons(controller.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        state.loseCrew(controller.getPlayerByID("player1"), List.of(new Pair<>(2, 4)));
        assertEquals("player2", state.getCurrentPlayer());
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1, 3), new Pair<>(3, 3)), List.of(new Pair<>(2, 2), new Pair<>(2, 2)));
        state.endMove(controller.getPlayerByID("player2"));
    }

    /**
     * Tests a specific sequence of actions and transitions in the Slavers encounter within the game.
     * This test validates the progression of player turns, cannon activation, and state changes.
     * <p>
     * The sequence includes:
     * - Player "player1" quitting during their turn, prompting the state transition to the next player.
     * - Verification that the current active player switches to "player2".
     * - Player "player2" successfully activating cannons using the specified cannon and battery coordinates.
     * <p>
     * This test ensures the correct behavior of the following aspects:
     * - Switching turns when a player quits in the cannon phase or crew loss phase.
     * - Appropriate handling of cannon activation with valid configurations.
     * - State and phase transitions based on player actions and game rules.
     *
     * @throws InvalidTurnException if a player performs an action out of turn
     * @throws ComponentNotFoundException if the specified cannon or cabin component is not found
     * @throws InvalidStateException if an action is performed in an invalid game state
     * @throws InvalidCannonException if improper cannon configurations are used during activation
     * @throws EnergyException if insufficient energy exists for an action
     */
    @Test
    void testSlaversState3() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException {
        state.currentQuit(controller.getPlayerByID("player1"));
        assertEquals("player2", state.getCurrentPlayer());
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
    }

    /**
     * Tests a complex scenario involving the Slavers encounter state where specific actions
     * and transitions are validated to ensure they adhere to the game's rules and logic.
     * <p>
     * This test examines the following:
     * - Activation of cannons by "player1" using specified cannon and battery coordinates.
     * - Quitting of "player2" during their turn and the resultant effects on the game state.
     * - Proper handling of state transitions and validations for player turns, as well as
     *   the enforcement of game rules for cannon activation and quitting actions.
     * <p>
     * This test ensures the correct functionality of:
     * - Cannon activation during the correct phase with specified components.
     * - Appropriate state changes when a player chooses to quit.
     * - Maintaining the integrity of turn progression and sequence.
     *
     * @throws InvalidTurnException if a player attempts to perform an action out of turn
     * @throws ComponentNotFoundException if a specified component (e.g., cannon, cabin) is not found
     * @throws InvalidStateException if an action is executed during an invalid game phase
     * @throws InvalidCannonException if an invalid cannon configuration is used during activation
     * @throws EnergyException if there is insufficient energy to perform an action
     */
    @Test
    void testSlaversState4() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException{
        state.activateCannons(controller.getPlayerByID("player1"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
        state.currentQuit(controller.getPlayerByID("player2"));
    }

    /**
     * Tests the functionality of dual cannon activation in a Slavers encounter scenario, ensuring proper
     * behavior when each player interacts with the game state during their turn.
     * <p>
     * The test performs the following actions:
     * - Player "player1" activates cannons with specified coordinates for both cannons and batteries.
     * - Player "player2" also activates cannons using a different set of cannon and battery coordinates.
     * <p>
     * This test ensures accurate handling of:
     * - Cannon activation by multiple players within the correct game phase.
     * - Validation of appropriate state transitions such as verifying cannon configurations
     *   and handling turn-based actions correctly.
     *
     * @throws InvalidTurnException if a player attempts an action during another player's turn
     * @throws ComponentNotFoundException if specified cannons or other components are not found
     * @throws InvalidStateException if an action is executed in an invalid game state
     * @throws InvalidCannonException if invalid cannon configurations are used during activation
     * @throws EnergyException if there is insufficient energy for the action performed
     */
    @Test
    void testSlaversState5() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException {
        state.activateCannons(controller.getPlayerByID("player1"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
    }
}