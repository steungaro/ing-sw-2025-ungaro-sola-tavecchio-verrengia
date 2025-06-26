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

class SmugglersStateTest {
    static GameController controller;
    static SmugglersState state;
    static AdventureCard card;

    /**
     * Sets up the test environment by initializing class fields and preparing the necessary test data.
     * <p>
     * This method is executed before each test in the SmugglersStateTest class, utilizing the
     * {@code @BeforeEach} annotation. It creates and configures an AdventureCard and a
     * GameController instance, populating the game state with players and their associated ships.
     * It ensures that each in-game player has a valid ship configuration with appropriate components
     * and connectors. The smugglers state is instantiated with the prepared game model, controller,
     * and card.
     *
     * @throws InvalidStateException if the state transition or initialization encounters an error
     * @throws CargoNotLoadable if cargo cannot be loaded into the specified cargo hold during setup
     * @throws CargoFullException if an attempt is made to load cargo into a full cargo hold during setup
     */
    @BeforeEach
    void setUp() throws InvalidStateException, CargoNotLoadable, CargoFullException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setFirePower(2);
        card.setLostDays(1);
        card.setLostCargo(2);
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.YELLOW);
        reward.add(CargoColor.BLUE);
        card.setReward(reward);
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
                case "player3" -> player.setPosition(4);
            }
        }
        state = new SmugglersState(controller.getModel(), controller, card);
    }

    /**
     * Tests the sequence of game state transitions and player interactions during various phases
     * of the game's smugglers' state. This test validates that the game properly handles player
     * turn changes, state phase transitions, and associated actions such as activating cannons,
     * unloading cargo, losing energy, accepting cards, loading cargo, moving cargo, and ending moves.
     * <p>
     * The method ensures that:
     * - Player turns transition correctly.
     * - State phases move in the expected order based on the actions performed.
     * - Specific methods like `activateCannons`, `unloadCargo`, `loseEnergy`, `endMove`,
     *   `acceptCard`, `loadCargo`, and `moveCargo` perform their intended operations without errors.
     * <p>
     * The test further verifies the following state transitions:
     * - From the cannons phase to remove cargo phase after both front players activate their cannons.
     * - From the accept phase to adding cargo phase after accepting a card.
     * - Proper adjustments to the game state such as player cargo changes, energy loss, and drawing a card.
     * <p>
     * Exceptions are thrown if invalid actions or state changes occur, ensuring proper error handling
     * for invalid turns, state mismanagement, energy misallocation, and invalid cargo or cannon operations.
     *
     * @throws InvalidTurnException if a player attempts to act outside their assigned turn
     * @throws InvalidStateException if an invalid state transition is attempted
     * @throws EnergyException if an operation leads to invalid energy usage or configuration
     * @throws InvalidCannonException if an invalid cannon activation occurs
     * @throws InvalidCargoException if cargo operations are performed incorrectly
     * @throws CargoException if a general cargo-related exception occurs
     * @throws CargoNotLoadable if cargo cannot be loaded into the designated position
     * @throws CargoFullException if cargo loading is attempted on a full cargo hold
     * @throws ComponentNotFoundException if a required component is missing during an operation
     */
    @Test
    void testSmugglersState() throws InvalidTurnException, InvalidStateException, EnergyException, InvalidCannonException, InvalidCargoException, CargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        assertEquals("player1", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player1"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
        assertEquals("player2", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        assertEquals(StatePhase.REMOVE_CARGO, state.phase);
        assertEquals("player2", state.getCurrentPlayer());
        state.unloadCargo(controller.getPlayerByID("player2"), CargoColor.GREEN, new Pair<>(1, 2));
        state.loseEnergy(controller.getPlayerByID("player2"), new Pair<>(2, 2));
        assertEquals("player3", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player3"), List.of(new Pair<>(1, 3), new Pair<>(3, 3)), List.of(new Pair<>(2, 2), new Pair<>(2, 2)));
        assertEquals("player3", state.getCurrentPlayer());
        assertEquals(StatePhase.ACCEPT_PHASE, state.phase);
        state.acceptCard(controller.getPlayerByID("player3"));
        assertEquals(StatePhase.ADD_CARGO, state.phase);
        state.loadCargo(controller.getPlayerByID("player3"), CargoColor.YELLOW, new Pair<>(1, 2));
        state.moveCargo(controller.getPlayerByID("player3"), CargoColor.YELLOW, new Pair<>(1, 2), new Pair<>(1, 4));
        state.loadCargo(controller.getPlayerByID("player3"), CargoColor.BLUE, new Pair<>(1, 4));
        assertEquals(StatePhase.DRAW_CARD_PHASE, state.phase);
    }

    /**
     * Tests a comprehensive sequence of actions and state transitions in the smugglers' game state,
     * ensuring correct behavior across various game phases and player interactions. This method
     * verifies the proper functionality of critical game mechanics such as loading/unloading cargo,
     * activating cannons, transitioning between game phases, and player turn management.
     * <p>
     * The method includes assertions to validate:
     * - Accurate turn progression for multiple players.
     * - Correct transitions between game phases (e.g., cannons phase to remove cargo phase, remove cargo
     *   phase to accept phase, etc.).
     * - Successful execution of operations, such as cannon activation, cargo loading, movement, unloading,
     *   and end-of-turn actions.
     * <p>
     * Additionally, the test ensures that operations involving cargo and cannons are handled gracefully,
     * with appropriate exceptions being thrown for invalid interactions or configurations.
     * <p>
     * The following critical checkpoints are verified during the test:
     * - Accurate state updates after player actions.
     * - Valid energy adjustments during cannon activation.
     * - Proper handling of cargo operations, including loading, movement, unloading, and capacity checks.
     * - Error handling for incorrect actions, such as invalid turn attempts or misconfigured components.
     * <p>
     * The test guarantees that both valid and invalid scenarios are covered through exception handling,
     * preventing unintended behavior from occurring in the game logic.
     *
     * @throws InvalidTurnException if a player performs an action outside their turn
     * @throws ComponentNotFoundException if a necessary component cannot be found for a specific operation
     * @throws InvalidStateException if an illegal state transition is attempted
     * @throws InvalidCannonException if an invalid cannon operation is performed
     * @throws EnergyException if an operation results in an invalid energy state
     * @throws InvalidCargoException if cargo operations are misconfigured or invalid
     * @throws CargoException if a general cargo-related exception occurs
     * @throws CargoNotLoadable if cargo cannot be loaded into the specified position
     * @throws CargoFullException if an attempt is made to load cargo in a full cargo hold
     */
    @Test
    void testSmugglersState2() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException, InvalidCargoException, CargoException, CargoNotLoadable, CargoFullException {
        CargoHold cargoHold = (CargoHold) controller.getPlayerByID("player2").getShip().getComponentAt(1, 2);
        controller.getPlayerByID("player2").getShip().loadCargo(CargoColor.GREEN, cargoHold);
        assertEquals("player1", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player1"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
        assertEquals("player2", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        assertEquals(StatePhase.REMOVE_CARGO, state.phase);
        assertEquals("player2", state.getCurrentPlayer());
        state.unloadCargo(controller.getPlayerByID("player2"), CargoColor.GREEN, new Pair<>(1, 2));
        state.unloadCargo(controller.getPlayerByID("player2"), CargoColor.GREEN, new Pair<>(1, 2));
        assertEquals("player3", state.getCurrentPlayer());
        assertEquals(StatePhase.CANNONS_PHASE, state.phase);
        state.activateCannons(controller.getPlayerByID("player3"), List.of(new Pair<>(1, 3), new Pair<>(3, 3)), List.of(new Pair<>(2, 2), new Pair<>(2, 2)));
        assertEquals("player3", state.getCurrentPlayer());
        assertEquals(StatePhase.ACCEPT_PHASE, state.phase);
        state.acceptCard(controller.getPlayerByID("player3"));
        assertEquals(StatePhase.ADD_CARGO, state.phase);
        state.loadCargo(controller.getPlayerByID("player3"), CargoColor.YELLOW, new Pair<>(1, 2));
        state.moveCargo(controller.getPlayerByID("player3"), CargoColor.YELLOW, new Pair<>(1, 2), new Pair<>(1, 4));
        state.loadCargo(controller.getPlayerByID("player3"), CargoColor.BLUE, new Pair<>(1, 4));
        state.endMove(controller.getPlayerByID("player3"));
        assertEquals(StatePhase.DRAW_CARD_PHASE, state.phase);
    }

    /**
     * Tests the functionality and behavior of the game state when a player quits during their turn,
     * ensuring that the game properly transitions to the next player's turn.
     * <p>
     * The method simulates a scenario where "player1" exits the game during their turn, and verifies
     * that the game correctly updates the current player to "player2".
     * <p>
     * Assertions:
     * - Verifies that the game transitions to the appropriate next player after the current player quits.
     * <p>
     * This test validates the correctness of player turn management during the smugglers' state phase.
     */
    @Test
    void testSmugglersState3() {
        state.currentQuit(controller.getPlayerByID("player1"));
        assertEquals("player2", state.getCurrentPlayer());
    }

    /**
     * Tests the behavior of the game state during a sequence of state transitions and player actions
     * in the smugglers' phase, specifically handling the scenario of multiple players ending their turn
     * and a player quitting during their turn.
     * <p>
     * The method performs the following validations:
     * - Ensures that the game correctly progresses to the next player after each player ends their turn.
     * - Verifies the proper update of the current player after a player quits the game.
     * <p>
     * Assertions:
     * - Checks that the current player is updated to "player3" after "player1" and "player2" end their turns.
     * - Confirms that the game handles a player quitting mid-turn without disrupting the game flow.
     *
     * @throws InvalidTurnException if an action is attempted by a player out of turn.
     * @throws InvalidStateException if an invalid state transition is attempted during the test.
     */
    @Test
    void testSmugglersState4() throws InvalidTurnException, InvalidStateException {
        state.endMove(controller.getPlayerByID("player1"));
        state.endMove(controller.getPlayerByID("player2"));
        assertEquals("player3", state.getCurrentPlayer());
        state.currentQuit(controller.getPlayerByID("player3"));
    }

    /**
     * Tests the behavior of the smugglers' game state when multiple players end their turns consecutively.
     * <p>
     * This method verifies that the state transitions appropriately and the game correctly progresses
     * through the player turns, ensuring accurate game flow management. Specifically, this test validates:
     * <p>
     * - The ability of players to end their turns without causing invalid state transitions or errors.
     * - Proper handling of player turn progression and state updates in the smugglers' phase.
     * - Correct exception handling for invalid operations when ending moves.
     * <p>
     * The test ensures that the game transitions to the next phase or player as required, verifying
     * the robustness of the `endMove` method when multiple consecutive player actions are performed.
     *
     * @throws InvalidTurnException if a player attempts to end a move when it is not their turn
     * @throws InvalidStateException if the game state is invalid for ending a move
     */
    @Test
    void testSmugglersState5() throws InvalidTurnException, InvalidStateException {
        state.endMove(controller.getPlayerByID("player1"));
        state.endMove(controller.getPlayerByID("player2"));
        state.endMove(controller.getPlayerByID("player3"));
    }
}