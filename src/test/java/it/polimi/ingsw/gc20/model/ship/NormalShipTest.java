package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalShipTest {

    private NormalShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin brownCabin;
    private CargoHold cargoHold;

    @BeforeEach
    void setUp() {
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
        battery.getEnergy().add(new Energy());
        battery.getEnergy().add(new Energy());

        brownCabin = new Cabin();
        brownCabin.setColor(AlienColor.BROWN);
        brownCabin.getAstronauts().add(new Astronaut());

        cargoHold = new CargoHold();
        Cargo c1 = new Cargo(CargoColor.BLUE);
        Cargo c2 = new Cargo(CargoColor.GREEN);
        cargoHold.loadCargo(c1);
        cargoHold.loadCargo(c2);

        // Add components to ship at valid positions
        ship.addComponent(upCannon, 1, 2);
        ship.addComponent(downCannon, 3, 2);
        ship.addComponent(singleEngine, 4, 1);
        ship.addComponent(doubleEngine, 5, 2);
        ship.addComponent(battery, 2, 2);
        ship.addComponent(brownCabin, 3, 4);
        ship.addComponent(cargoHold, 4, 2);
    }

    @Test
    void getFirstComponent() {
    }

    @Test
    void getShield() {
    }

    @Test
    void getCannons() {
    }

    @Test
    void getAllExposed() {
    }

    @Test
    void isValid() {
    }

    @Test
    void epidemic() {
    }

    @Test
    void firePower() {
    }
}