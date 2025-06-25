package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
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

class PlanetsStateTest {
    static GameController controller;
    static PlanetsState state;
    static AdventureCard card;

    /**
     * Initializes the test environment for the PlanetsStateTest class by setting up
     * the necessary game components and initializing an AdventureCard object, GameController,
     * and game state. This method ensures the test setup accurately represents a valid game state
     * with predefined configuration, player setup, ship components, and their connections.
     *
     * @throws InvalidStateException if the state is invalid during setup.
     * @throws CargoNotLoadable if the cargo cannot be loaded into the ship.
     * @throws CargoFullException if the cargo hold is full while attempting to load cargo.
     */
    @BeforeEach
    void setUp() throws InvalidStateException, CargoNotLoadable, CargoFullException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setLostDays(1);
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.YELLOW);
        reward.add(CargoColor.BLUE);
        List<CargoColor> reward2 = new ArrayList<>();
        reward2.add(CargoColor.GREEN);
        reward2.add(CargoColor.GREEN);
        List<Planet> planets = new ArrayList<>();
        Planet planet1 = new Planet();
        planet1.setReward(reward);
        Planet planet2 = new Planet();
        planet2.setReward(reward2);
        planets.add(planet1);
        planets.add(planet2);
        card.setPlanets(planets);
        controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
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
        state = new PlanetsState(controller.getModel(), controller, card);
    }

    /**
     * Executes a series of game actions and validations to test the game's handling
     * of cargo loading, moving, unloading, and player turn transitions in a controlled scenario.
     * <p>
     * The method simulates a player landing on a planet, interacting with cargo, and ending moves,
     * ensuring correctness in game behavior and transitions between game states.
     * Various operations like cargo loading, movement, unloading, and validation of player turns
     * are performed, while assertions validate the expected outcomes.
     *
     * @throws InvalidTurnException if an action is performed out of turn.
     * @throws InvalidStateException if an action is invoked in an invalid game state.
     * @throws ComponentNotFoundException if referenced game components are not found.
     * @throws CargoException if there's an issue with cargo operations.
     * @throws CargoNotLoadable if the cargo cannot be loaded into the player's ship.
     * @throws CargoFullException if the player's cargo hold is full.
     * @throws InvalidCargoException if the cargo being moved or unloaded is invalid.
     */
    @Test
     void Test() throws InvalidTurnException, InvalidStateException, ComponentNotFoundException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        state.landOnPlanet(controller.getPlayerByID("player1"), 0);
        state.loadCargo(controller.getPlayerByID("player1"), CargoColor.YELLOW, new Pair<>(1, 2));
        state.moveCargo(controller.getPlayerByID("player1"), CargoColor.YELLOW, new Pair<>(1, 2), new Pair<>(1, 4));
        state.unloadCargo(controller.getPlayerByID("player1"), CargoColor.YELLOW, new Pair<>(1, 4));
        state.loadCargo(controller.getPlayerByID("player1"), CargoColor.BLUE, new Pair<>(1, 2));
        state.endMove(controller.getPlayerByID("player1"));
        assertEquals("player2", state.getCurrentPlayer());
        state.endMove(controller.getPlayerByID("player2"));
        assertEquals(4, controller.getPlayerByID("player1").getPosition());
    }

    /**
     * Tests the functionality of the {@code currentQuit} method in the {@code State} class.
     * <p>
     * This method verifies the behavior when the current player quits the game. Specifically, it ensures:
     * - The {@code currentQuit} method is invoked with the correct player retrieved using their ID.
     * - The current player is correctly updated to the next player after the quitting action.
     * <p>
     * Assertions:
     * - Confirms that the current player is updated to "player2" after "player1" quits.
     */
    @Test
    void testCurrentQuit(){
        state.currentQuit(controller.getPlayerByID("player1"));
        assertEquals("player2", state.getCurrentPlayer());
    }

}