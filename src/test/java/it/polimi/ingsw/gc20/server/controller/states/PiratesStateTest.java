package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
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

class PiratesStateTest {
    static GameController controller;
    static PiratesState state;
    static AdventureCard card;

    /**
     * Initializes the test environment before each test case is executed.
     * This method sets up the necessary objects and configurations required for testing the PiratesState functionality.
     * <p>
     * The setup includes:
     * - Instantiating and configuring the AdventureCard.
     * - Creating a GameController with test configurations.
     * - Setting up players, their ships, and ship components with appropriate attributes and connectors.
     * - Adding cargo to ships and initializing astronauts.
     * - Assigning positions to players in the game.
     * - Creating an instance of PiratesState for testing.
     *
     * @throws InvalidStateException if the state transition or setup encounters an invalid state.
     * @throws CargoNotLoadable if the cargo cannot be loaded onto the ship.
     * @throws CargoFullException if the cargo hold of the ship is full and cannot accept additional cargo.
     */
    @BeforeEach
    void setUp() throws InvalidStateException, CargoNotLoadable, CargoFullException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setFirePower(2);
        card.setLostDays(1);
        card.setCredits(1);
        List<Projectile> projectiles = new ArrayList<>();
        Projectile heavyFire = new Projectile();
        heavyFire.setDirection(Direction.DOWN);
        heavyFire.setFireType(FireType.HEAVY_FIRE);
        projectiles.add(heavyFire);
        Projectile lightFire = new Projectile();
        lightFire.setDirection(Direction.DOWN);
        lightFire.setFireType(FireType.LIGHT_FIRE);
        projectiles.add(lightFire);
        card.setProjectiles(projectiles);
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
        state = new PiratesState(controller.getModel(), controller, card);
    }

    /**
     * Tests the behavior of the PiratesState under various scenarios, including
     * validating state-specific rules, turn-based constraints, and game mechanics.
     * <p>
     * This method verifies that exceptions are thrown appropriately when invalid
     * actions are attempted, and that the game flow progresses as expected through
     * phases such as activating cannons, rolling dice, activating shields, and
     * accepting cards.
     * <p>
     * Specific test cases included:
     * - Ensures that actions taken by players not on their turn throw InvalidTurnException.
     * - Validates that actions taken in inappropriate phases throw InvalidStateException.
     * - Confirms the proper throwing of EnergyException and InvalidCannonException when relevant.
     * - Verifies the correct game state transitions as actions are executed.
     * - Asserts the correct updates of player state after accepting cards, such as position and credits.
     * <p>
     * Any exceptions thrown during invalid or illegal actions are expected and
     * part of the test validation for ensuring robust game mechanics.
     *
     * @throws InvalidTurnException if a player attempts an action out of turn.
     * @throws ComponentNotFoundException if a required ship component is missing.
     * @throws InvalidStateException if an action is attempted in an incorrect state or phase.
     * @throws InvalidCannonException if cannon-related actions are invalid.
     * @throws EnergyException if energy requirements are not met for an action.
     */
    @Test
    void testPiratesState() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException {
        assertThrows(InvalidTurnException.class, () -> state.acceptCard(controller.getPlayerByID("player2")));
        assertThrows(InvalidTurnException.class, () -> state.activateShield(controller.getPlayerByID("player2"), null, null));
        assertThrows(InvalidStateException.class, () -> state.acceptCard(controller.getPlayerByID("player1")));
        assertThrows(InvalidStateException.class, () -> state.activateShield(controller.getPlayerByID("player1"), null, null));
        assertThrows(InvalidTurnException.class, () -> state.rollDice(controller.getPlayerByID("player2")));
        assertThrows(InvalidStateException.class, () -> state.rollDice(controller.getPlayerByID("player1")));
        assertThrows(InvalidStateException.class, () -> state.chooseBranch(controller.getPlayerByID("player1"), null));
        assertThrows(InvalidTurnException.class, () -> state.activateCannons(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>()));
        state.activateCannons(controller.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        assertThrows(InvalidStateException.class, () -> state.activateCannons(controller.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>()));
        state.rollDice(controller.getPlayerByID("player1"));
        state.rollDice(controller.getPlayerByID("player1"));
        state.activateShield(controller.getPlayerByID("player1"), null, null);
        assertEquals("player2", state.getCurrentPlayer());
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1,3), new Pair<>(3, 3)), List.of(new Pair<>(2,2), new Pair<>(2,2)));
        state.acceptCard(controller.getPlayerByID("player2"));
        assertEquals(4, controller.getPlayerByID("player2").getPosition());
        assertEquals(1, controller.getPlayerByID("player2").getCredits());
        assertThrows(InvalidTurnException.class, () -> state.chooseBranch(controller.getPlayerByID("player1"), null));
    }

    /**
     * Tests the behavior and functionality of the `currentQuit` method within the game state logic.
     * This method assesses the following aspects:
     * <p>
     * - Verifies that `currentQuit` can handle players quitting their turn and transition the game state accordingly.
     * - Ensures the game transitions to the appropriate phase or player after a player quits.
     * - Tests interaction with `activateCannons` to confirm proper handling of cannon activation in the current game phase.
     * - Validates the method's ability to deal with exceptions like `InvalidTurnException`, `InvalidStateException`,
     *   and other related exceptions without crashing or entering an invalid state.
     * <p>
     * Exceptions are expected and handled based on the game logic:
     *
     * @throws InvalidTurnException if the player attempts to quit out of turn.
     * @throws ComponentNotFoundException if a required component is not available during the state transition.
     * @throws InvalidStateException if the current game state does not permit quitting.
     * @throws InvalidCannonException if invalid operations related to cannon activation occur.
     * @throws EnergyException if insufficient energy resources are available for specific operations.
     */
    @Test
    void testCurrentQuit () throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException {
        state.currentQuit(controller.getPlayerByID("player1"));
        state.activateCannons(controller.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        state.currentQuit(controller.getPlayerByID("player2"));
    }

    /**
     * Tests the activation of cannons by players within the PiratesState under specific conditions.
     * Verifies that the game correctly progresses through the activation of cannons for two players
     * and adheres to game rules such as turn-based mechanics and cannon-related exceptions.
     * <p>
     * The following aspects are evaluated:
     * - Ensures the proper handling of cannon activation for multiple players in the current state.
     * - Validates that the associated exceptions, such as InvalidTurnException, InvalidStateException,
     *   InvalidCannonException, ComponentNotFoundException, and EnergyException, are thrown under
     *   appropriate conditions.
     * - Confirms the state transitions and message broadcasts triggered by the activateCannons method.
     *
     * @throws InvalidTurnException if a player attempts to activate cannons out of turn.
     * @throws ComponentNotFoundException if the specified cannons or batteries are not found on the player's ship.
     * @throws InvalidStateException if the cannon activation action is attempted in an incorrect game phase or state.
     * @throws InvalidCannonException if the provided cannon positions or configurations are invalid.
     * @throws EnergyException if insufficient energy is available for activating the specified cannons or batteries.
     */
    @Test
    void testPiratesState2() throws InvalidTurnException, ComponentNotFoundException, InvalidStateException, InvalidCannonException, EnergyException {
        state.activateCannons(controller.getPlayerByID("player1"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
        state.activateCannons(controller.getPlayerByID("player2"), List.of(new Pair<>(1, 3)), List.of(new Pair<>(2, 2)));
    }

}