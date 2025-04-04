package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.model.player.Player;

import java.util.ArrayList;

public class PlanetsState extends State {
    /**
     * Default constructor
     */
    public PlanetsState() {
        super();
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
        Player player = getPlayerByID(username);
        stateEnum = StateEnum.WAITING_CARGO_GAIN;
        cargoGained = new ArrayList<>(model.PlanetLand(player, planetIndex));
    }
}
