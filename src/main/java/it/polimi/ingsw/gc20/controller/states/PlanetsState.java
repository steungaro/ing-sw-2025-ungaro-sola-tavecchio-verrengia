package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.model.cards.Planet;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlanetsState extends CargoState {
    private List<Planet> planets;
    private int lostDays;
    private String landedPlayer;
    private int landedPlanetIndex;
    /**
     * Default constructor
     */
    public PlanetsState(List<Planet> planets, int lostDays) {
        super();
        this.planets = planets;
        this.lostDays = lostDays;
        this.landedPlayer = null;
        this.landedPlanetIndex = -1;
    }

    @Override
    public String toString() {
        return "PlanetsState";
    }



    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     * @throws IllegalStateException if the game is not in the planet phase
     * @throws IllegalArgumentException if it is not the player's turn
     */
    @Override
    public void landOnPlanet(String username, int planetIndex) {
        if (!currentPlayer.equals(username)) {
            throw new IllegalArgumentException("It's not your turn");
        }
        if (planets.get(planetIndex).getAvailable()) {
            planets.get(planetIndex).setAvailable(false);
            landedPlayer = username;
            landedPlanetIndex = planetIndex;
        } else {
            throw new IllegalStateException("The planet is not available");
        }
    }

    @Override
    public void loadCargo(Player player, CargoColor loaded, CargoHold chTo) {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't load cargo unless you are on the planet");
        }
        if (planets.get(landedPlanetIndex).getReward().contains(loaded)) {
            planets.get(landedPlanetIndex).getReward().remove(loaded);
            super.loadCargo(player, loaded, chTo);
        } else {
            throw new IllegalStateException("You can't load this cargo, it's not in the reward");
        }
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, CargoHold ch) {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't unload cargo unless you are on the planet");
        }
        super.unloadCargo(player, unloaded, ch);
    }

    @Override
    public void moveCargo(Player player, CargoColor cargo, CargoHold from, CargoHold to) {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't move cargo unless you are on the planet");
        }
        super.moveCargo(player, cargo, from, to);
    }

    @Override
    public void endMove(Player player){
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("It's not your turn");
        }
        landedPlayer = null;
        landedPlanetIndex = -1;
    }
}
