package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsTest {

    @Test
    void getPlanets() {
        Planets planets = new Planets();
        assertNotNull(planets.getPlanets());
    }

    @Test
    void setPlanets() {
        Planets planets = new Planets();
        planets.setPlanets(new ArrayList<>());
        assertEquals(0, planets.getPlanets().size());
    }

    @Test
    void getPlanet() {
        Planets planets = new Planets();
        Planet planet = new Planet();
        planets.setPlanets(new ArrayList<>());
        planets.getPlanets().add(planet);
        assertEquals(planet, planets.getPlanet(0));
    }

    @Test
    void getLostDays() {
        Planets planets = new Planets();
        planets.setLostDays(5);
        assertEquals(5, planets.getLostDays());
    }

    @Test
    void setLostDays() {
        Planets planets = new Planets();
        planets.setLostDays(3);
        assertEquals(3, planets.getLostDays());
    }

    @Test
    void land() {
        Planets planets = new Planets();
        Player player = new Player();
        Planet planet = new Planet();
        planet.setAvailable(true);
        planets.getPlanets().add(planet);
        assertNotNull(planets.land(player, planet));
    }

    @Test
    void effectLostDays() {
        Planets planets = new Planets();
        Player player = new Player();
        player.setPosition(10);
        Game game = new Game();
        planets.setLostDays(3);
        planets.effectLostDays(player, game);
        assertEquals(7, player.getPosition());
    }
}