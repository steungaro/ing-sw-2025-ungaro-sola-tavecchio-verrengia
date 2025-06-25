package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.*;
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

class OpenSpaceStateTest {
    static GameController controller;
    static OpenSpaceState state;
    static AdventureCard card;

    /**
     * Sets up the initial state required for testing the {@code OpenSpaceState}.
     * <p>
     * This method initializes the game controller, adventure card, and creates ships for the in-game players.
     * It configures ship components with valid states and sets up their connectors. Additionally, it assigns ships
     * to players and prepares the game state by initializing astronauts and setting player positions.
     * <p>
     * This method is executed before each test in the class to ensure a consistent testing environment. Any exceptions
     * related to an invalid state or cabin initialization are handled by the respective exception definitions.
     *
     * @throws InvalidStateException if the state setup fails during initialization
     * @throws EmptyCabinException if there is an issue with initializing a cabin component
     */
    @BeforeEach
    void setUp() throws InvalidStateException, EmptyCabinException {
        //initialize the AdventureCard
        card = new AdventureCard();
        controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
        assertNotNull(controller.getModel());
        controller.getModel().setActiveCard(card);
        // build all the ships of the players one will be invalid
        StartingCabin start;
        for (Player player : controller.getModel().getInGamePlayers()) {
            // Create a new NormalShip
            NormalShip ship = new NormalShip();

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
            player.setPosition(0);
            if (player.getUsername().equals("player1")) {
                player.setPosition(5);
            }
            ship.initAstronauts();
            player.setShip(ship);

        }
        state = new OpenSpaceState(controller.getModel(), controller, card);
    }

    /**
     * Tests the behavior of the {@code OpenSpaceState} during the engine activation and movement phase.
     * <p>
     * This test verifies:
     * - The initial state of the current player.
     * - The activation of engines for multiple players using specific engine and battery configurations.
     * - The resulting board positions of the players after engine activations.
     * <p>
     * The method first asserts that the initial current player is correct. Then, it simulates engine activation using the
     * {@code activateEngines} method for two players, with different configurations for engines and batteries. Finally,
     * the test ensures the players' positions are updated as expected based on their respective activations.
     *
     * @throws InvalidTurnException if an attempt is made to activate engines outside the player's turn
     * @throws ComponentNotFoundException if a specified component is not found during engine activation
     * @throws InvalidStateException if the state is invalid for the given operation
     * @throws InvalidEngineException if engine activation fails due to invalid parameters
     * @throws EnergyException if insufficient energy is provided during engine activation
     */
    @Test
    void testOpenSpaceState() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidEngineException, EnergyException {
        // Test the initial state of the OpenSpaceState
        assertEquals("player1", state.getCurrentPlayer());
        state.activateEngines(controller.getPlayerByID("player1"),
                List.of(new Pair<>(3, 4)),
                List.of(new Pair<>(2, 2)));
        state.activateEngines(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        assertEquals(8, controller.getPlayerByID("player1").getPosition());
        assertEquals(1, controller.getPlayerByID("player2").getPosition());
    }


    /**
     * Tests the behavior of the {@code currentQuit} method in the {@code OpenSpaceState} class.
     * <p>
     * This method performs the following verifications:
     * - Asserts the initial current player is "player1".
     * - Simulates a quit action from the player identified by "player1".
     * - Simulates a quit action from the player identified by "player2".
     * <p>
     * The {@code currentQuit} method internally attempts to activate player engines with empty configurations
     * while gracefully handling potential exceptions such as {@code InvalidTurnException},
     * {@code InvalidStateException}, {@code EnergyException}, {@code InvalidEngineException},
     * and {@code ComponentNotFoundException}, which are caught and ignored.
     */
    @Test
    void currentQuitTest(){
        assertEquals("player1", state.getCurrentPlayer());
        state.currentQuit(controller.getPlayerByID("player1"));
        state.currentQuit(controller.getPlayerByID("player2"));
    }
}