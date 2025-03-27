package it.polimi.ingsw.gc20.model.cards;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest{

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
        Cabin1.setAstronauts(1);

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
    }

    @Test
    void effect() {
        Player player1 = new Player();
        player1.setPosition(4);

        // Create game
        Game game = new Game();

        player1.setShip(ship);

        AbandonedShip abandonedShip = new AbandonedShip();
        abandonedShip.setCredits(3);
        abandonedShip.setLostCrew(3);
        abandonedShip.setLostDays(1);
        List<Cabin> cabins = new ArrayList<>();
        cabins.add(Cabin1);
        StartingCabin startingCabin = (StartingCabin) ship.getComponentAt(2, 3);
        startingCabin.setAstronauts(2);
        cabins.add(startingCabin);

        abandonedShip.Effect(player1, game, cabins);
        assertEquals(3, player1.getCredits());
        assertEquals(3, player1.getPosition());
        assertEquals(0, startingCabin.getAstronauts());
    }
}