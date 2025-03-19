package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

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
