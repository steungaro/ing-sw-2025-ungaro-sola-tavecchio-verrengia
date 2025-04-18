package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;

import java.io.Serializable;

/**
 * Event that is generated when a player activates the double cannons
 * @apiNote username of the player,  tuple with the component where to remove the cargo and the component where to add the cargo, if the second component is NULL we are removing the cargo from the ship
 */
public record MoveCargoEvent(
        String username,
        Integer sourceID,
        Integer destinationID,
        CargoColor cargoColor
) implements Event, Serializable {

    @Override
    public String toString() {
        return "MoveCargoEvent {username = " + username +  "source component = " + sourceID + "destination component = " + destinationID + "cargo = " + cargoColor + "}";
    }
}
