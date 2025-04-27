package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetTest {
    private Planet planet;

    @BeforeEach
    void setup(){
        planet = new Planet();
    }

    @Test
    void defaultConstructorTest(){
        assertNotNull(planet.getReward());
        assertTrue(planet.getReward().isEmpty());
        assertTrue(planet.getAvailable());
        assertNull(planet.getPlayer());
    }

    @Test
    void setGetRewardTest(){
        planet.setReward(List.of(CargoColor.BLUE, CargoColor.GREEN));
        assertEquals(List.of(CargoColor.BLUE, CargoColor.GREEN), planet.getReward());
    }

    @Test
    void setGetAvailableTest(){
        planet.setAvailable(false);
        assertFalse(planet.getAvailable());
    }

    @Test
    void landTest(){
        Player player = new Player();
        player.setUsername("TestPlayer");
        planet.setReward(List.of(CargoColor.BLUE, CargoColor.GREEN));
        List<CargoColor> reward = planet.land(player);

        assertEquals(reward, List.of(CargoColor.BLUE, CargoColor.GREEN));
        assertFalse(planet.getAvailable());
        assertEquals(player, planet.getPlayer());
    }

    @Test
    void setPlayerTest(){
        Player player = new Player();
        player.setUsername("TestPlayer");
        planet.setPlayer(player);
        assertEquals(player, planet.getPlayer());
    }
}
