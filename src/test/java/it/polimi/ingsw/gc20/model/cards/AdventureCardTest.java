package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.bank.Astronaut;
import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.bank.Energy;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCardTest {
    private NormalShip ship;
    private Cannon upCannon, downCannon;
    private Engine singleEngine, doubleEngine;
    private Battery battery;
    private Cabin Cabin1;
    private CargoHold cargoHold;

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
        battery.getEnergy().add(new Energy());
        battery.getEnergy().add(new Energy());

        Cabin1 = new Cabin();
        Cabin1.setColor(AlienColor.NONE);
        Cabin1.getAstronauts().add(new Astronaut());

        cargoHold = new CargoHold();
        Cargo c1 = new Cargo(CargoColor.BLUE);
        Cargo c2 = new Cargo(CargoColor.GREEN);
        cargoHold.loadCargo(c1);
        cargoHold.loadCargo(c2);

        // Add components to ship at valid positions
        ship.addComponent(upCannon, 1, 3);
        ship.addComponent(downCannon, 3, 3);
        ship.addComponent(singleEngine, 3, 2);
        ship.addComponent(doubleEngine, 3, 4);
        ship.addComponent(battery, 2, 2);
        ship.addComponent(Cabin1, 2, 4);
        ship.addComponent(cargoHold, 1, 2);
    }

}