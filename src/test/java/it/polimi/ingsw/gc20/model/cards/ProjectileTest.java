package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    private NormalShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin Cabin1;
    private CargoHold cargoHold;
    private Player player1;
    private Game game;

    @BeforeEach
    void setUp(){
        // Create a new NormalShip
        ship = new NormalShip();

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
        cargoHold.setSlots(2);
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
        StartingCabin start = (StartingCabin) ship.getComponentAt(2,3);
        start.setConnectors(connectorsStartingCabin);

        ship.initAstronauts();
    }

    @Test
    void fire() {
        // Create a new Projectile
        Projectile projectile = new Projectile();

        // Set the direction and fire type
        projectile.setDirection(Direction.UP);
        projectile.setFireType(FireType.HEAVY_FIRE);

        // Fire the projectile at the ship
        // projectile.Fire(ship, 6);

    }

    @Test
    void setDirection() {
        // Create a new Projectile
        Projectile projectile = new Projectile();

        // Set the direction
        projectile.setDirection(Direction.UP);

        // Assert that the direction is set correctly
        assertEquals(Direction.UP, projectile.getDirection());
    }

    @Test
    void setFireType() {
        // Create a new Projectile
        Projectile projectile = new Projectile();

        // Set the fire type
        projectile.setFireType(FireType.HEAVY_FIRE);

        // Assert that the fire type is set correctly
        assertEquals(FireType.HEAVY_FIRE, projectile.getFireType());
    }

    @Test
    void getDirection() {
        // Create a new Projectile
        Projectile projectile = new Projectile();

        // Set the direction
        projectile.setDirection(Direction.UP);

        // Assert that the direction is set correctly
        assertEquals(Direction.UP, projectile.getDirection());
    }

    @Test
    void getFireType() {
        // Create a new Projectile
        Projectile projectile = new Projectile();

        // Set the fire type
        projectile.setFireType(FireType.HEAVY_FIRE);

        // Assert that the fire type is set correctly
        assertEquals(FireType.HEAVY_FIRE, projectile.getFireType());
    }
}