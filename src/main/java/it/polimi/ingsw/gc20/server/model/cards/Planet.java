package it.polimi.ingsw.gc20.server.model.cards;

import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.player.Player;

/**
 * The Planet class represents a planet within the game. A planet can have a list of rewards
 * (represented by CargoColor), an availability status, and an associated player.
 * Players can "land" on a planet to claim its rewards, after which the planet becomes unavailable.
 * This class implements the Serializable interface to allow the planet's state to be serialized.
 */
public class Planet implements Serializable {
    private List<CargoColor> reward;
    private boolean available;
    private Player player = null;

    /**
     * Default constructor for the Planet class.
     * Initializes a new instance of the Planet, setting its reward list to an empty list
     * and marking the planet as available.
     */
    public Planet() {
        reward = new ArrayList<>();
        available = true;
    }

    /**
     * Retrieves the list of rewards associated with the planet.
     * The rewards are represented by a list of CargoColor items.
     *
     * @return a List of CargoColor representing the rewards for the planet
     */
    public List<CargoColor> getReward() {
        return reward;
    }

    /**
     * Sets the reward for the planet.
     *
     * @param reward a list of CargoColor objects representing the rewards associated with the planet
     */
    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    /**
     * Retrieves the availability status of the planet.
     *
     * @return a boolean indicating whether the planet is available (true) or not (false)
     */
    public boolean getAvailable() {
        return available;
    }

    /**
     * Sets the availability status of the planet.
     *
     * @param available a boolean indicating whether the planet is available (true) or not (false)
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    
    /**
     * Associates a player with the planet, marks the planet as unavailable,
     * and returns the list of rewards associated with the planet.
     *
     * @param player the Player who is landing on the planet
     * @return a List of CargoColor objects representing the rewards for the player
     */
    public List<CargoColor> land (Player player) {
        this.player = player;
        this.setAvailable(false);
        return reward;
    }


    /**
     * Retrieves the player associated with the planet.
     *
     * @return the Player currently associated with the planet, or null if no player is assigned.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Associates a player with the planet by setting the specified Player instance
     * as the player associated with this planet.
     *
     * @param player the Player to be associated with this planet
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Provides a string representation of the Planet object, including its rewards,
     * availability status, and associated player (if any).
     *
     * @return a string that describes the rewards, availability, and player association of the planet
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reward:");
        for (CargoColor color : reward) {
            sb.append(" ").append(color);
        }
        sb.append(available ? " (\u001B[32mavailable\u001B[0m)" : " (\u001B[31mnot available\u001B[0m)");
        if (player != null) {
            sb.append(" Player: ").append(player.getUsername());
        }
        return sb.toString();
    }

}