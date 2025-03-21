package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {

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
    void effectLostDays() {
        CombatZone combatZone = new CombatZone();
        combatZone.setLostDays(2);
        assertEquals(2, combatZone.getLostDays());
    }

    @Test
    void effectLostCrew() {
        CombatZone combatZone = new CombatZone();
        combatZone.setLostCrew(3);

        Player player1 = new Player();
        player1.setShip(ship);

        List<Cabin> cabins = new ArrayList<>();

        cabins.add(Cabin1);

        combatZone.EffectLostCrew(player1, cabins);
        assertEquals(0, Cabin1.getAstronauts());

        Cabin1.setAstronauts(2);
        Cabin cabin2 = new Cabin();
        cabin2.setAstronauts(2);

        cabins.add(cabin2);

        combatZone.EffectLostCrew(player1, cabins);
        assertEquals(1, cabin2.getAstronauts());
    }

    @Test
    void effectLostCargo() {
        CombatZone combatZone = new CombatZone();
        combatZone.setLostCargo(2);

        Player player1 = new Player();
        player1.setShip(ship);

        List<CargoHold> cargoHolds = new ArrayList<>();

        CargoHold cargoHold2 = new CargoHold();
        cargoHolds.add(cargoHold);
        cargoHold2.setSlots(3);

        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.GREEN);
        cargo.add(CargoColor.YELLOW);
        cargoHold2.setCargoHeld(cargo);

        cargoHolds.add(cargoHold2);

        combatZone.EffectLostCargo(player1, cargoHolds);

        assertEquals(1, cargoHold2.getCargoHeld().size());
    }

    @Test
    void effectCannonFire() {
    }
}