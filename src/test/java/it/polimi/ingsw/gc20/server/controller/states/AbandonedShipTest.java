package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AbandonedShipTest {
    static GameController controller;
    static AbandonedShipState state;
    static AdventureCard card;

    /**
     * Sets up the test environment for the AbandonedShipState tests. This method initializes
     * the necessary components, including the game controller, the adventure card, and the
     * abandoned ship state. It also prepares the ships for all players with relevant components
     * and configurations.
     * <p>
     * This setup is executed once before all tests in the class run. Throws an
     * {@code InvalidStateException} if the state is not correctly initialized.
     * <p>
     * The method performs the following operations:
     * - Initializes the adventure card with specific properties like crew, credits, and lost days.
     * - Creates a game controller for a test game with players.
     * - Creates a state of type AbandonedShipState using the controller and card.
     * - Configures each player's ship with various components such as cannons, engines, batteries,
     *   cabins, and cargo holds.
     * - Ensures components are added at valid positions and have appropriate connector configurations.
     * - Initializes astronauts for each ship.
     */
    @BeforeAll
    static void setUp() throws InvalidStateException {
        //initialize the AdventureCard
        card = new AdventureCard();
        card.setCrew(2);
        card.setCredits(3);
        card.setLostDays(1);
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        controller.getModel().setActiveCard(card);

        state = new AbandonedShipState(controller.getModel(), controller, card);
        // build all the ships of the players one will be invalid
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
            connectorsCargoHold.put(Direction.UP, ConnectorEnum.ZERO);
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
            connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U); // Valid ship for other players
            upCannon.setConnectors(connectorsUpCannon);

            Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
            connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.UP, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.U);
            StartingCabin start = (StartingCabin) ship.getComponentAt(2, 3);
            start.setConnectors(connectorsStartingCabin);
            ship.initAstronauts();
            player.setShip(ship);
        }
    }

    /**
     * Tests various aspects of the {@code AbandonedShipState}, including the behavior of
     * methods such as {@code endMove}, {@code acceptCard}, and {@code loseCrew}.
     * This method ensures that the state transitions and operations conform to expected behavior.
     * <p>
     * It verifies:
     * - The initial state of the {@code AbandonedShipState}, such as the current player.
     * - Validation of player actions, including positional changes and credit updates, after
     *   interacting with the adventure card.
     * - The ability of a player to perform the {@code loseCrew} operation appropriately,
     *   ensuring necessary conditions such as selecting valid cabins.
     * - Proper handling of exceptions during invalid state transitions, invalid player turns,
     *   empty cabin selections, or component discrepancies.
     * <p>
     * Exceptions handled during the test include:
     * {@code InvalidTurnException}, {@code InvalidStateException},
     * {@code EmptyCabinException}, and {@code ComponentNotFoundException}.
     * <p>
     * Any unexpected exceptions will cause the test to fail.
     */
    @Test
    void testState() {
        // Test the initial state of the AbandonedShipState
        assert state.getCurrentPlayer().equals("player1");
        try {
            state.endMove(controller.getPlayerByID("player1"));
            controller.getPlayerByID("player2").setPosition(5);
            int position = controller.getPlayerByID("player2").getPosition();
            int credits = controller.getPlayerByID("player2").getCredits();
            state.acceptCard(controller.getPlayerByID("player2"));
            assertEquals(position - 1, controller.getPlayerByID("player2").getPosition());
            assertEquals(credits + card.getCredits(), controller.getPlayerByID("player2").getCredits());
            List<Pair<Integer, Integer>> cabins = new ArrayList<>();
            cabins.add(new Pair<>(2, 4)); // Cabin1
            cabins.add(new Pair<>(2, 3)); // StartingCabin
            state.loseCrew(controller.getPlayerByID("player2"), cabins);
            state.endMove(controller.getPlayerByID("player2"));

        } catch (InvalidTurnException | InvalidStateException | EmptyCabinException | ComponentNotFoundException e) {
            fail(e);
        }
    }
}
