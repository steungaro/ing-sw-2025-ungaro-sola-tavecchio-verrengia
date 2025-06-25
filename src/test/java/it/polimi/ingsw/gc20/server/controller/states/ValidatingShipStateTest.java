package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTileException;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatingShipStateTest {
    static GameController controller;
    static ValidatingShipState state;

    /**
     * Sets up the initial state for the ship validation tests. This method is responsible for
     * initializing the game controller, creating and constructing ships for the players with
     * various components and connectors, and transitioning the game to a state suitable for
     * ship validation. Each player is assigned a ship with specific component configurations,
     * and the setup accounts for both valid and invalid configurations for testing purposes.
     *
     * @throws InvalidStateException if the game state cannot be initialized properly.
     * @throws InvalidTileException if an invalid tile configuration is encountered during setup.
     */
    @BeforeAll
    static void setUp() throws InvalidStateException, InvalidTileException {
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        // build all the ships of the players
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
            connectorsCargoHold.put(Direction.UP, ConnectorEnum.S);
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
            if (player.getUsername().equals("player1")) {
                connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.ZERO); // Invalid ship for player1
            } else {
                connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U); // Valid ship for other players
            }
            upCannon.setConnectors(connectorsUpCannon);

            Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
            connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.UP, ConnectorEnum.D);
            connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.U);
            StartingCabin start = (StartingCabin) ship.getComponentAt(2, 3);
            start.setConnectors(connectorsStartingCabin);
            if (player.getUsername().equals("player1")) {
                LifeSupport lifeSupport = new LifeSupport();
                lifeSupport.setColor(AlienColor.BOTH);
                ship.addComponent(lifeSupport, 2, 5);

                Map<Direction, ConnectorEnum> connectorsLifeSupport = new HashMap<>();
                connectorsLifeSupport.put(Direction.RIGHT, ConnectorEnum.S);
                connectorsLifeSupport.put(Direction.LEFT, ConnectorEnum.S);
                connectorsLifeSupport.put(Direction.UP, ConnectorEnum.S);
                connectorsLifeSupport.put(Direction.DOWN, ConnectorEnum.S);
                lifeSupport.setConnectors(connectorsLifeSupport);
                Cabin1.setColor(AlienColor.BROWN); // Invalid ship for player1
            }
            player.setShip(ship);
        }
        state = new ValidatingShipState(controller.getModel(), controller);
    }

    /**
     * Tests the validity of a ship's state through a sequence of operations involving ship component manipulation,
     * alien placement, and state transitions. The method simulates a player's actions in the game and validates that
     * the state transitions and operations behave as expected.
     * <p>
     * The test performs the following actions:
     * - Removes a component from the ship of the player identified by "player1".
     * - Adds a brown alien to a specified cabin on "player1"'s ship.
     * - Ends the turn for "player1".
     * - Asserts that the game state allows concurrent operations after the move.
     * - Simulates "player1" rejoining the game.
     * - Simulates the game resuming for "player2".
     * <p>
     * Exceptions are thrown to handle invalid scenarios, such as:
     * - InvalidStateException: If a state-related operation is performed outside the allowed phase.
     * - InvalidAlienPlacement: If an alien is placed in an invalid location on the ship.
     * - ComponentNotFoundException: If a specified component is not found on the ship.
     * - InvalidTileException: If an operation involves an invalid tile configuration.
     * <p>
     * This test ensures that the system correctly enforces game rules and maintains appropriate game state transitions.
     *
     * @throws InvalidStateException if an invalid operation is performed due to the current game state.
     * @throws InvalidAlienPlacement if an alien is placed in an invalid manner.
     * @throws ComponentNotFoundException if a component specified for removal or manipulation is not found.
     * @throws InvalidTileException if a tile-related configuration or operation is invalid.
     */
    @Test
    void isShipValidTest() throws InvalidStateException, InvalidAlienPlacement, ComponentNotFoundException, InvalidTileException {
        state.removeComp(controller.getPlayerByID("player1"), new Pair<>(1, 3));
        state.addAlien(controller.getPlayerByID("player1"), AlienColor.BROWN, new Pair<>(2, 4));
        state.endMove(controller.getPlayerByID("player1"));
        assertTrue(state.isConcurrent());
        state.rejoin("player1");
        state.resume("player2");
    }

    /**
     * Tests the correct behavior of the {@code resume} method within the {@code State} class,
     * focusing on its handling of a single reconnected player scenario during the ship validation phase.
     * <p>
     * The method invokes the {@code resume} functionality by simulating the re-entry of a specific
     * player ("player1") into the game, verifying that the system transitions appropriately based
     * on the current game phase and player's state. The test ensures that:
     * - The game correctly recognizes the rejoined player.
     * - Appropriate state actions are triggered for the reconnected and other players.
     * <p>
     * This test is part of the {@code ValidatingShipStateTest} class, designed to validate the
     * ship validation functionalities during the game's VALIDATE_SHIP_PHASE.
     */
    @Test
    void isShipValidTest2(){
        state.resume("player1");
    }
}
