package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {

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
    }

    @Test
    void effect() {
        Player player1 = new Player();
        player1.setShip(ship);

        // Create game
        Game game = new Game();
        AbandonedStation abandonedStation = new AbandonedStation();
        List<CargoColor> cargo = new ArrayList<>();
        cargo = abandonedStation.Effect(player1, game);
    }

    @Test
    void setCrewNeeded() {
        AbandonedStation abandonedStation = new AbandonedStation();
        abandonedStation.setCrewNeeded(3);
        assertEquals(3, abandonedStation.getCrewNeeded());
    }

    @Test
    void getCrewNeeded() {
        AbandonedStation abandonedStation = new AbandonedStation();
        abandonedStation.setCrewNeeded(3);
        assertEquals(3, abandonedStation.getCrewNeeded());
    }

    @Test
    void setReward() {
        AbandonedStation abandonedStation = new AbandonedStation();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        abandonedStation.setReward(reward);
        assertEquals(reward, abandonedStation.getReward());
    }

    @Test
    void getReward() {
        AbandonedStation abandonedStation = new AbandonedStation();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        abandonedStation.setReward(reward);
        assertEquals(reward, abandonedStation.getReward());
    }

    @Test
    void setLostDays() {
        AbandonedStation abandonedStation = new AbandonedStation();
        abandonedStation.setLostDays(3);
        assertEquals(3, abandonedStation.getLostDays());
    }

    @Test
    void getLostDays() {
        AbandonedStation abandonedStation = new AbandonedStation();
        abandonedStation.setLostDays(3);
        assertEquals(3, abandonedStation.getLostDays());
    }
}