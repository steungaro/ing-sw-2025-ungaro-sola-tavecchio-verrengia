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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ValidatingShipStateTest {
    static GameController controller;
    static ValidatingShipState state;

    @BeforeAll
    static void setUp() throws InvalidStateException, InvalidTileException {
        controller = new GameController("testGame", List.of("player1", "player2", "player3"), 2);
        state = new ValidatingShipState(controller.getModel(), controller);
        // build all the ships of the players
        for (Player player : controller.getModel().getInGamePlayers()) {
            // Create a new NormalShip
            NormalShip ship = new NormalShip();

            // Create components
            Cannon upCannon = new Cannon();
            upCannon.setOrientation(Direction.UP);
            upCannon.setPower(1);

            Cannon downCannon = new Cannon();
            downCannon.setOrientation(Direction.DOWN);
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
            if (player.getUsername().equals("player1")) {
                connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.ZERO); // Invalid ship for player1
            } else {
                connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U); // Valid ship for other players
            }
            upCannon.setConnectors(connectorsUpCannon);

            Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
            connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.U);
            connectorsStartingCabin.put(Direction.UP, ConnectorEnum.U);
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
    }

    @Test
    void isShipValidTest() throws InvalidStateException, InvalidAlienPlacement, ComponentNotFoundException, InvalidTileException {
        for (Player player : controller.getModel().getInGamePlayers()) {
            try {
                // Validate the ship of the player
                boolean isValid = state.isShipValid(player);
                if (!isValid && !player.getUsername().equals("player1")) {
                    fail("Ship should be valid for player: " + player.getUsername());
                }
                if (!player.getUsername().equals("player1")) {
                    state.endMove(player);
                }
            } catch (InvalidStateException e) {
                fail("InvalidStateException thrown when validating ship for player: " + player.getUsername());
            }
        }
        state.removeComp(controller.getPlayerByID("player1"), new Pair<>(1, 3));
        state.addAlien(controller.getPlayerByID("player1"), AlienColor.BROWN, new Pair<>(2, 4));
        state.endMove(controller.getPlayerByID("player1"));
        assertTrue(state.isConcurrent());
        state.toString();
    }
}
