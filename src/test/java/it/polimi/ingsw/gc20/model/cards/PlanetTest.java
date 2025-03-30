package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetTest {

    @Test
    void getReward() {
        Planet planet = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        CargoColor cargoColor = CargoColor.BLUE;
        reward.add(cargoColor);
        planet.setReward(reward);

        assertEquals(reward, planet.getReward());
    }

    @Test
    void setReward() {
        Planet planet = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        CargoColor cargoColor = CargoColor.BLUE;
        reward.add(cargoColor);
        planet.setReward(reward);

        assertEquals(reward, planet.getReward());
    }

    @Test
    void getAvailable() {
        Planet planet = new Planet();
        Boolean available = true;
        planet.setAvailable(available);

        assertEquals(available, planet.getAvailable());
    }

    @Test
    void setAvailable() {
        Planet planet = new Planet();
        Boolean available = false;
        planet.setAvailable(available);

        assertEquals(available, planet.getAvailable());
    }

    @Test
    void setUnavailable() {
        Planet planet = new Planet();
        planet.setUnavailable();

        assertFalse(planet.getAvailable());
    }

    @Test
    void land() {
        Planet planet = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        CargoColor cargoColor = CargoColor.BLUE;
        reward.add(cargoColor);
        planet.setReward(reward);

        Player player = new Player();
        List<CargoColor> result = planet.land(player);

        assertEquals(reward, result);
        assertFalse(planet.getAvailable());
    }

    @Test
    void getPlayer() {
        Planet planet = new Planet();
        Player player = new Player();
        planet.setPlayer(player);

        assertEquals(player, planet.getPlayer());
    }

    @Test
    void setPlayer() {
        Planet planet = new Planet();
        Player player = new Player();
        planet.setPlayer(player);

        assertEquals(player, planet.getPlayer());
    }
}