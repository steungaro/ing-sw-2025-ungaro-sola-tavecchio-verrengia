package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.gamesets.*;

/**
 * @author GC20
 */
public class Planets extends AdventureCard {
    private List<Planet> planets;

    /**
     * Default constructor
     */
    public Planets() {
        super();
        planets = new ArrayList<>();
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public Planet getPlanet(int index) {
        return planets.get(index);
    }

    /*
     * @param g is the game where the player is playing
     * @param player is the player that wants to land on a planet
     * @param planet is the planet where the player wants to land
     * @return the list of cargo that the player can take from the planet, cargo is removed from bank, if not enough cargo is available in bank, it will not be created
     * @apiNote the controller needs to verify that the planet is available and then manage Cargo loading
     */
    public List<Cargo> land (Player player, Planet planet) {
        return planet.land(player).stream().map(Cargo::new).toList();
    }



}