package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;

import java.io.Serializable;

/**
 * Event that is generated when a player lands on a planet
 * @apiNote planet is the index of the planet where the player wants to land
 */
public record LandPlanetEvent(
        String username,
        int planet
) implements Event, Serializable {

    @Override
    public String toString() {
        return "LandPlanetEvent {username = " + username +  "on planet number: " + planet+ "}";
    }
}
