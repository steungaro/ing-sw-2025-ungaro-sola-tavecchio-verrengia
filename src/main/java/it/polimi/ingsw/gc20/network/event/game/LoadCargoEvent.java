package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.io.Serializable;

/**
 * Event that is generated when a player loads a cargo onto their ship
 * @apiNote username of the player, the component where to add the cargo and the cargo color
 */
public record LoadCargoEvent(
        String username,
        Integer componentID,
        CargoColor cargoColor
) implements Event, Serializable {

    @Override
    public String toString() {
        return "LoadCargoEvent {username = " + username +  "component = " + componentID + "cargo = " + cargoColor + "}";
    }
}
