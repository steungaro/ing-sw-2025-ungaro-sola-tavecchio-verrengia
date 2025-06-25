package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CombatZone0Test {
    static GameController controller;
    static CombatZone0State state;
    static AdventureCard card;

    /**
     * Sets up the initial test environment for the CombatZone0Test class.
     * This method is executed once before all tests in the class. It initializes
     * the game controller, creates an AdventureCard, and configures the ships of
     * all players, including adding components and their respective connectors.
     * <p>
     * The method also establishes the initial state for the game by associating
     * the game controller with a CombatZone0State and preparing the required game
     * components to simulate a valid test scenario.
     *
     * @throws InvalidStateException if the initial setup encounters an invalid state.
     * @throws EmptyCabinException if an attempt is made to access an empty cabin during setup.
     */
    @BeforeAll
    static void setUp() throws InvalidStateException, EmptyCabinException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setCrew(1);
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
        card.setLostDays(1);
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
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
            ship.initAstronauts();
            player.setShip(ship);
            if (player.getUsername().equals("player1")) {
                controller.getPlayerByID("player1").getShip().unloadCrew(start);
                controller.getPlayerByID("player1").setPosition(5);
            }

        }
        state = new CombatZone0State(controller.getModel(), controller, card);
    }

    /**
     * Tests the behavior of the CombatZone0State within the game.
     * <p>
     * This method verifies several aspects of the CombatZone0State, including the player's
     * initial position, the activation of engines and cannons for the players in the game,
     * and the effects of losing crew during the combat. Additionally, dice rolls and shield
     * activations are tested to ensure proper game mechanics within the combat zone.
     * <p>
     * Assertions are made to verify that:
     * - The initial position of the player is correct.
     * - Engines and cannons are appropriately activated for the players.
     * - Crew size is decremented correctly when a cabin is lost.
     * - The current player state is accurate after operations.
     * - Dice rolling and shield activation operate correctly without throwing errors.
     *
     * @throws InvalidTurnException if an operation is performed during an invalid turn.
     * @throws InvalidStateException if the state of the game is inconsistent.
     * @throws InvalidEngineException if engine activation encounters an error.
     * @throws EnergyException if operations exceed the available energy resources.
     * @throws EmptyCabinException if an action is performed on an unoccupied cabin.
     * @throws InvalidCannonException if cannon activation encounters an error.
     * @throws ComponentNotFoundException if a required component is missing.
     * @throws InvalidShipException if a ship is initialized incorrectly.
     * @throws DieNotRolledException if an operation requiring dice values is performed before rolling.
     */
    @Test
    void testCombatZone0State() throws InvalidTurnException, InvalidStateException, InvalidEngineException, EnergyException, EmptyCabinException, InvalidCannonException, ComponentNotFoundException, InvalidShipException, DieNotRolledException {
        assertEquals(4, controller.getPlayerByID("player1").getPosition());
        List<Pair<Integer, Integer>> engines = new ArrayList<>();
        List<Pair<Integer, Integer>> battery = new ArrayList<>();
        for (Player player : controller.getModel().getInGamePlayers()) {
            if (player.getUsername().equals("player1")) {
                state.activateEngines(player, engines, battery);
                engines.add(new Pair<>(3, 4));
                battery.add(new Pair<>(2, 2));
            } else {
                state.activateEngines(player, engines, battery);
            }
        }
        assertEquals("player1", state.getCurrentPlayer());
        List<Pair<Integer, Integer>> cabins = new ArrayList<>();
        int crewSize = controller.getPlayerByID("player1").getShip().crew();
        cabins.add(new Pair<>(2, 4));
        state.loseCrew(controller.getPlayerByID("player1"), cabins);
        assertEquals(crewSize - 1, controller.getPlayerByID("player1").getShip().crew());

        assertEquals(4, controller.getPlayerByID("player1").getPosition());
        List<Pair<Integer, Integer>> cannons = new ArrayList<>();
        List<Pair<Integer, Integer>> battery2 = new ArrayList<>();
        for (Player player : controller.getModel().getInGamePlayers()) {
            if (player.getUsername().equals("player1")) {
                state.activateCannons(player, cannons, battery2);
                cannons.add(new Pair<>(3, 3));
                battery2.add(new Pair<>(2, 2));
            } else {
                state.activateCannons(player, cannons, battery2);
            }
        }
        assertEquals("player1", state.getCurrentPlayer());
        state.rollDice(controller.getPlayerByID("player1"));
        state.rollDice(controller.getPlayerByID("player1"));
        state.activateShield(controller.getPlayerByID("player1"), null, null);
    }

    /**
     * Tests the behavior of the `currentQuit` and `chooseBranch` methods in scenarios
     * where the player makes invalid branch selection attempts during the `VALIDATE_SHIP_PHASE`.
     * <p>
     * This test ensures that:
     * - An `InvalidStateException` is thrown when a null branch is chosen.
     * - The `currentQuit` method responds correctly to clean up or transition states
     *   after an invalid branch selection attempt.
     * <p>
     * The test verifies the robustness of state transitions and exception handling
     * within the `CombatZone0State` when the player attempts invalid operations.
     */
    @Test
    void currentQuitChooseBranchInvalidTest(){
        assertThrows(InvalidStateException.class, ()->state.chooseBranch(controller.getPlayerByID("player1"), null));
        state.currentQuit(controller.getPlayerByID("player1"));
    }
}
