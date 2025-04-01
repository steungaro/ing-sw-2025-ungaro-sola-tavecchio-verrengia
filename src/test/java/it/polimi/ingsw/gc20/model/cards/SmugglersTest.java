package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {

    @Test
    void setLostCargo() {
        Smugglers smugglers = new Smugglers();
        int lostCargo = 5;
        smugglers.setLostCargo(lostCargo);
        assertEquals(lostCargo, smugglers.getLostCargo());
    }

    @Test
    void getLostCargo() {
        Smugglers smugglers = new Smugglers();
        int lostCargo = 5;
        smugglers.setLostCargo(lostCargo);
        assertEquals(lostCargo, smugglers.getLostCargo());
    }

    @Test
    void setFirePower() {
        Smugglers smugglers = new Smugglers();
        int firePower = 10;
        smugglers.setFirePower(firePower);
        assertEquals(firePower, smugglers.getFirePower());
    }

    @Test
    void getFirePower() {
        Smugglers smugglers = new Smugglers();
        int firePower = 10;
        smugglers.setFirePower(firePower);
        assertEquals(firePower, smugglers.getFirePower());
    }

    @Test
    void setLostDays() {
        Smugglers smugglers = new Smugglers();
        int lostDays = 3;
        smugglers.setLostDays(lostDays);
        assertEquals(lostDays, smugglers.getLostDays());
    }

    @Test
    void getLostDays() {
        Smugglers smugglers = new Smugglers();
        int lostDays = 3;
        smugglers.setLostDays(lostDays);
        assertEquals(lostDays, smugglers.getLostDays());
    }

    @Test
    void setReward() {
        Smugglers smugglers = new Smugglers();
        List<CargoColor> reward = List.of(CargoColor.RED, CargoColor.BLUE);
        smugglers.setReward(reward);
        assertEquals(reward, smugglers.getReward());
    }

    @Test
    void getReward() {
        Smugglers smugglers = new Smugglers();
        List<CargoColor> reward = List.of(CargoColor.RED, CargoColor.BLUE);
        smugglers.setReward(reward);
        assertEquals(reward, smugglers.getReward());
    }

    @Test
    void effectFailure() {
        Player player = new Player();
        Smugglers smugglers = new Smugglers();
        List<CargoHold> cargoHolds = new ArrayList<>();
        NormalShip ship = new NormalShip();
        player.setShip(ship);

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        List<CargoColor> cargoColors = new ArrayList<>();
        ship.loadCargo(CargoColor.BLUE, cargoHold);
        cargoHold.setCargoHeld(cargoColors);

        cargoHolds.add(cargoHold);

        smugglers.EffectFailure(player, cargoHolds);
        assertEquals(0, cargoHold.getCargoHeld().get(CargoColor.BLUE));
    }

    @Test
    void effectSuccess() {
        Player player = new Player();
        Smugglers smugglers = new Smugglers();
        smugglers.setLostDays(3);
        smugglers.setReward(List.of(CargoColor.RED, CargoColor.BLUE));
        List<CargoHold> cargoHolds = new ArrayList<>();
        NormalShip ship = new NormalShip();
        Game game = new Game();
        player.setShip(ship);

        player.setPosition(10);
        smugglers.EffectSuccess(player, game);

        assertEquals(7, player.getPosition());
        assertEquals(List.of(CargoColor.RED, CargoColor.BLUE), smugglers.EffectSuccess(player, game));
    }
}