package it.polimi.ingsw.gc20.server.model.cards;

import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetTest {
    private Planet planet;

    /**
     * Sets up the Planet instance before each test.
     */
    @BeforeEach
    void setup(){
        planet = new Planet();
    }


    /**
     * Tests the default constructor of the Planet class.
     * It checks that the reward is initialized as an empty list,
     * available is set to true, and player is null.
     */
    @Test
    void defaultConstructorTest(){
        assertNotNull(planet.getReward());
        assertTrue(planet.getReward().isEmpty());
        assertTrue(planet.getAvailable());
        assertNull(planet.getPlayer());
    }

    /**
     * Tests the set and get methods for the reward attribute of Planet.
     */
    @Test
    void setGetRewardTest(){
        planet.setReward(List.of(CargoColor.BLUE, CargoColor.GREEN));
        assertEquals(List.of(CargoColor.BLUE, CargoColor.GREEN), planet.getReward());
    }

    /**
     * Tests the set and get methods for the available attribute of Planet.
     */
    @Test
    void setGetAvailableTest(){
        planet.setAvailable(false);
        assertFalse(planet.getAvailable());
    }

    /**
     * Tests the land on planet functionality.
     * It checks that the player receives the correct reward,
     * the planet becomes unavailable,
     * and the player on the planet is set correctly.
     */
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

    /**
     * Tests the set and get methods for the player attribute of Planet.
     */
    @Test
    void setPlayerTest(){
        Player player = new Player();
        player.setUsername("TestPlayer");
        planet.setPlayer(player);
        assertEquals(player, planet.getPlayer());
    }

    /**
     * Tests the toString method of Planet.
     * It checks that the string representation includes the reward,
     * availability status, and player information.
     */
    @Test
    void toStringWithPlayerDisplaysCorrectFormat() {
        Player player = new Player();
        player.setUsername("TestPlayer");
        planet.setPlayer(player);
        planet.setReward(List.of(CargoColor.RED));
        planet.setAvailable(false);

        String expected = "Reward: RED (\u001B[31mnot available\u001B[0m) Player: TestPlayer";
        assertEquals(expected, planet.toString());

        planet.setReward(List.of(CargoColor.RED));
        planet.setAvailable(true);
        planet.setPlayer(null);

        String expected2 = "Reward: RED (\u001B[32mavailable\u001B[0m)";
        assertEquals(expected2, planet.toString());
    }
}
