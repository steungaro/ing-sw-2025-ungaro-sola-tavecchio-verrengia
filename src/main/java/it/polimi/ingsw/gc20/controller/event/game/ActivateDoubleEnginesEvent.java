package it.polimi.ingsw.gc20.controller.event.game;

import it.polimi.ingsw.gc20.controller.event.Event;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Event that is generated when a player activates the double engine
 * @apiNote username of the player, list of tuples of the engine and battery components the engines are in the first position while the batteries are in the second
 */
public record ActivateDoubleEnginesEvent(
        String username,
        List<Pair<Integer, Integer>> componentIDs
) implements Event, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleEnginesEvent {username = " + username +  componentIDs.toString() + "}";
    }
}
