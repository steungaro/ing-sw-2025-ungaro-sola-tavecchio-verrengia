package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Event that is generated when a player activates a shield
 * @apiNote username of the player, tuple of the shield and battery components
 */
public record ActivateShieldsEvent(
        String username,
        Integer shieldID,
        Integer batteryID
) implements Event, Serializable {

    @Override
    public String toString() {
        return "ActivateShieldsEvent {username = " + username + "shield = " + shieldID+ "battery = " + batteryID + "}";
    }
}
