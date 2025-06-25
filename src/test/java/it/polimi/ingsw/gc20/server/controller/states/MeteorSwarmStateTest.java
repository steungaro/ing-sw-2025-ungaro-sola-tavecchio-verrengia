package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmStateTest {
    static GameController controller;
    static MeteorSwarmState state;
    static AdventureCard card;

    /**
     * Sets up the initial state required for testing {@code MeteorSwarmState}.
     * <p>
     * This method is executed once before all tests in the class are run. It performs the following steps:
     * <p>
     * 1. Initializes an {@code AdventureCard} with specific projectiles and sets it as the active card in the game model.
     * 2. Creates a {@code GameController} with predefined test data such as game name, players, and player count.
     * 3. Constructs valid and invalid ships for each player with various components such as cannons, engines, batteries,
     *    cabin, and cargo holds. The components are configured with appropriate directions, connector settings, and properties.
     * 4. Sets the constructed ships to the players within the game model and initializes their astronauts.
     * 5. Prepares a {@code MeteorSwarmState} instance using the current game controller and active card, which is later
     *    utilized for testing.
     *
     * @throws InvalidStateException if the initial state of the test setup is invalid.
     * @throws EmptyCabinException if the cabin does not contain the required setup.
     */
    @BeforeAll
    static void setUp() throws InvalidStateException, EmptyCabinException {
        //initialize the AdventureCard
        card = new AdventureCard();
        List<Projectile> projectiles = new ArrayList<>();
        Projectile heavyFire = new Projectile();
        heavyFire.setDirection(Direction.DOWN);
        heavyFire.setFireType(FireType.HEAVY_METEOR);
        projectiles.add(heavyFire);
        Projectile lightFire = new Projectile();
        lightFire.setDirection(Direction.DOWN);
        lightFire.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(lightFire);
        card.setProjectiles(projectiles);
        controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
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
            if (player.getUsername().equals("player1")) {
                player.setPosition(5);
            }
            ship.initAstronauts();
            player.setShip(ship);

        }
        state = new MeteorSwarmState(controller.getModel(), controller, card);
    }

    /**
     * Tests the behavior and constraints of the {@code MeteorSwarmState} during a meteor swarm event.
     * <p>
     * This method performs various state transitions and validations for the {@code MeteorSwarmState}.
     * The test sequence includes dice rolls, cannon activations, shield activations, and branch selection.
     * It also ensures that exceptions are thrown when performing invalid actions.
     * <p>
     * The following operations are executed:
     * 1. Rolls dice for a player and validates the current turn.
     * 2. Activates cannons for both players without utilizing specific components.
     * 3. Rolls dice again and activates shields for both players.
     * 4. Attempts an invalid branch selection to ensure {@code InvalidStateException} is thrown.
     * 5. Simulates a player quitting the current turn.
     * <p>
     * This test guarantees that the game transitions and validations in the {@code MeteorSwarmState}
     * function as intended and handle exceptions appropriately.
     *
     * @throws InvalidTurnException         if an operation is invoked by a player
     *                                      when it is not their turn.
     * @throws InvalidStateException        if an operation is invoked in an invalid game state.
     * @throws ComponentNotFoundException   if a required component for an operation
     *                                      is not found.
     * @throws EnergyException              if there is insufficient energy to perform
     *                                      an operation.
     */
    @Test
    void testMeteorSwarmState() throws InvalidTurnException, InvalidStateException, ComponentNotFoundException, EnergyException {
        state.rollDice(controller.getPlayerByID("player1"));
        state.activateCannons(controller.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        state.activateCannons(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        state.rollDice(controller.getPlayerByID("player1"));
        state.activateShield(controller.getPlayerByID("player1"), null, null);
        state.activateShield(controller.getPlayerByID("player2"), null, null);
        assertThrows(InvalidStateException.class, () -> state.chooseBranch(controller.getPlayerByID("player1"), null));
        state.currentQuit(controller.getPlayerByID("player2"));
    }

}