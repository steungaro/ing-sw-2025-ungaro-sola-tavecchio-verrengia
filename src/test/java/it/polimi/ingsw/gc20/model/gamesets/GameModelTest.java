package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    int level = 2;
    List<String> players = new ArrayList<>();
    String gameId = "1";
    GameModel gameModel = new GameModel();

    @BeforeEach
    void setUp() {
        players.add("player1");
        players.add("player2");
    }

    private NormalShip ship, ship2;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin Cabin1;
    private CargoHold cargoHold;

    @BeforeEach
    void setUp2() {
        // Create a new NormalShip
        ship = new NormalShip();
        ship2 = new NormalShip();

        // Create components
        upCannon = new Cannon();
        upCannon.setOrientation(Direction.UP);
        upCannon.setPower(1);

        downCannon = new Cannon();
        downCannon.setOrientation(Direction.DOWN);
        downCannon.setPower(2);

        singleEngine = new Engine();
        singleEngine.setDoublePower(false);

        doubleEngine = new Engine();
        doubleEngine.setDoublePower(true);

        battery = new Battery();
        battery.setSlots(2);
        battery.fillBattery();

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);
        Cabin1.setAstronauts(2);

        cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        cargoHold.loadCargo(CargoColor.BLUE);
        cargoHold.loadCargo(CargoColor.GREEN);

        // Add components to ship at valid positions
        ship.addComponent(upCannon, 1, 3);
        ship.addComponent(downCannon, 3, 3);
        ship.addComponent(singleEngine, 3, 2);
        ship.addComponent(doubleEngine, 3, 4);
        ship.addComponent(battery, 2, 2);
        ship.addComponent(Cabin1, 2, 4);
        ship.addComponent(cargoHold, 1, 2);

        ship2.addComponent(upCannon, 1, 3);
        ship2.addComponent(downCannon, 3, 3);
        ship2.addComponent(singleEngine, 3, 2);
        ship2.addComponent(doubleEngine, 3, 4);
        ship2.addComponent(battery, 2, 2);
        ship2.addComponent(Cabin1, 2, 4);
        ship2.addComponent(cargoHold, 1, 2);


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
        connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U);
        upCannon.setConnectors(connectorsUpCannon);

        Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
        connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start = (StartingCabin) ship.getComponentAt(2, 3);
        start.setConnectors(connectorsStartingCabin);

        Map<Direction, ConnectorEnum> connectorBattery2 = new HashMap<>();
        connectorBattery2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorBattery2.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorBattery2.put(Direction.UP, ConnectorEnum.ZERO);
        connectorBattery2.put(Direction.DOWN, ConnectorEnum.ZERO);
        Battery battery2 = new Battery();
        battery2.setConnectors(connectorBattery2);
        ship2.addComponent(battery2, 1,1);
        StartingCabin start2 = (StartingCabin) ship2.getComponentAt(2, 3);
        start2.setConnectors(connectorsStartingCabin);
    }

    @Test
    void startGame() {
        gameModel.startGame(level, players, gameId);

        assertTrue(gameModel.getGame().getPile().getUnviewed().getFirst() instanceof Battery);
    }

    @Test
    void calculateScore() {
        gameModel.startGame(level, players, gameId);


        gameModel.getGame().getPlayers().forEach(player -> {
            if(player.getUsername().equals("player1")) {
                player.setShip(ship);
                player.setPosition(10);
                int expConn = ship.getAllExposed();
                int waste = ship.getWaste().size();
            }
            if(player.getUsername().equals("player2")) {
                player.setShip(ship2);
                player.setPosition(20);
                int expConn2 = ship2.getAllExposed();
                int waste2 = ship2.getWaste().size();
            }
        });

        Map<Player, Integer> score = gameModel.calculateScore();
        assertEquals(6, score.get(gameModel.getGame().getPlayers().get(1)));
        assertEquals(8+4, score.get(gameModel.getGame().getPlayers().get(0)));

        Battery bat = (Battery) ship2.getComponentAt(1,1);
        ship2.killComponent(bat);

        Player player1 = gameModel.getGame().getPlayers().get(0);
        Player player2;
        if(player1.getUsername().equals("player1")) {
            player2 = gameModel.getGame().getPlayers().get(1);
        }
        else{
            player2 = gameModel.getGame().getPlayers().get(0);
            player1=gameModel.getGame().getPlayers().get(1);
        }

        score = gameModel.calculateScore();
        assertEquals(6+4, score.get(player1));
        assertEquals(8+4-1, score.get(player2));
    }
}