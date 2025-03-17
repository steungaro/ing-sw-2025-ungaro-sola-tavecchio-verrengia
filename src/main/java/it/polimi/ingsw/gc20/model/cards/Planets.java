package it.polimi.ingsw.gc20.model.cards;

import java.util.*;

import it.polimi.ingsw.gc20.model.bank.Cargo;
import it.polimi.ingsw.gc20.model.player.Player;

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
     * @param player is the player that wants to land on a planet
     * @param planet is the planet where the player wants to land
     * @return the list of cargo colors that the player can take from the planet
     * @apiNote the controller needs to verify that the planet is available and then manage Cargo loading
     */
    public List<Cargo> land (Player player, Planet planet) {
        return planet.land(player).stream().map(Cargo::new).toList();
    }



}