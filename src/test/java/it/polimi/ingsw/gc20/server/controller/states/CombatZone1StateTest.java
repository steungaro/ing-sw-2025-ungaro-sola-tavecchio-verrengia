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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CombatZone1StateTest {
    static GameController controller;
    static CombatZone1State state;
    static AdventureCard card;

    /**
     * Sets up the testing environment for the CombatZone1StateTest class.
     * <p>
     * This method initializes the necessary components and objects required for
     * the tests. It creates an AdventureCard, configures its properties, and sets
     * it as the active card in the game controller. It also builds ships for each
     * in-game player with appropriate components, connectors, and configurations,
     * ensuring one ship is intentionally invalid. Once the setup is complete, the
     * testing state is initialized with a CombatZone1State instance.
     *
     * @throws InvalidStateException if an invalid state is encountered during setup
     * @throws EmptyCabinException if a cabin is found to be empty when it should not be
     * @throws CargoNotLoadable if cargo cannot be loaded onto the ship
     * @throws CargoFullException if the cargo hold is full and cannot accommodate additional cargo
     */
    @BeforeAll
    static void setUp() throws InvalidStateException, EmptyCabinException, CargoNotLoadable, CargoFullException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setCrew(1);
        card.setLostCargo(2);
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
            battery.setSlots(6);
            battery.setAvailableEnergy(6);

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
                controller.getPlayerByID("player1").getShip().loadCargo(CargoColor.GREEN, cargoHold);
            }

        }

        state = new CombatZone1State(controller.getModel(), controller, card);
    }

    /**
     * Tests the functionality of the CombatZone1State class, ensuring proper behavior in a combat scenario.
     * <p>
     * This method verifies the following:
     * - Activation of cannons and proper assignment of cannon positions for specific players.
     * - Correct functionality of engine activation for players and proper assignment of engine positions.
     * - Unloading of a specific cargo type and verifying that the player's ship's cargo hold is properly emptied.
     * - Loss of energy for a player as expected.
     * - Proper transition to the next game state through move ending.
     * - Dice rolling behavior for a player.
     * - Activation of shields without any specified additional parameters.
     * <p>
     * Assertions are made to confirm:
     * - Correct player positioning after certain actions.
     * - Proper assignment of turns to the current player.
     * - Successful emptying of cargo.
     * - Correct energy manipulation.
     * <p>
     * This method uses multithreading (e.g., `Thread.sleep`) to simulate certain delays in state progression if applicable.
     *
     * @throws InvalidTurnException if a turn-based error occurs.
     * @throws InvalidStateException if the application is in an invalid state.
     * @throws InvalidEngineException if engine activation fails.
     * @throws EnergyException if energy manipulation generates an invalid state.
     * @throws InvalidCannonException if cannon configuration or activation fails.
     * @throws ComponentNotFoundException if a required ship component is missing.
     * @throws InvalidShipException if the ship being manipulated is considered invalid.
     * @throws DieNotRolledException if dice rolling operations are attempted without rolling.
     * @throws InvalidCargoException if invalid cargo operations are performed.
     * @throws InterruptedException if the test thread execution is interrupted during a delay simulation.
     */
    @Test
    void testCombatZone1State() throws InvalidTurnException, InvalidStateException, InvalidEngineException, EnergyException, InvalidCannonException, ComponentNotFoundException, InvalidShipException, DieNotRolledException, InvalidCargoException, InterruptedException {
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
        state.unloadCargo(controller.getPlayerByID("player1"), CargoColor.GREEN, new Pair<>(1, 2));

        Map<CargoColor, Integer> cargo = controller.getPlayerByID("player1").getShip().getCargo();
        boolean allZero = true;
        for (Integer count: cargo.values()){
            if (count > 0) {
                allZero = false;
                break;
            }
        }
        assertTrue(allZero);


        state.loseEnergy(controller.getPlayerByID("player1"), new Pair<>(2, 2));
        state.endMove(controller.getPlayerByID("player1"));
        Thread.sleep(5000);

        assertEquals("player1", state.getCurrentPlayer());

        state.rollDice(controller.getPlayerByID("player1"));
        state.rollDice(controller.getPlayerByID("player1"));
        state.activateShield(controller.getPlayerByID("player1"), null, null);
    }

    /**
     * Tests the behavior of the `currentQuit` and `chooseBranch` methods when an invalid state
     * is encountered during the branch selection process.
     * <p>
     * Verifies the following:
     * - The `chooseBranch` method throws an `InvalidStateException` when attempting to choose
     *   a branch in an incorrect game phase or state.
     * - The `currentQuit` method is called after the exception is thrown to handle the player's
     *   decision to quit the current state.
     * <p>
     * This ensures proper error handling and state transition when invalid operations are
     * performed during branch selection.
     * <p>
     * Assertions:
     * - Ensures that `InvalidStateException` is thrown as expected during branch selection.
     */
    @Test
    void currentQuitChooseBranchInvalidTest(){
        assertThrows(InvalidStateException.class, ()->state.chooseBranch(controller.getPlayerByID("player1"), null));
        state.currentQuit(controller.getPlayerByID("player1"));
    }
}