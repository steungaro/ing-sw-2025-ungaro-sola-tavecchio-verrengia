package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Event;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Event that is generated when a player activates the double cannons
 * @apiNote username of the player, list of tuples of the cannon and battery components the cannons are in the first position while the batteries are in the second
 */
public record ActivateDoubleCannonsEvent(
        String username,
        List<Pair<Integer, Integer>> coordinates
) implements Event, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsEvent {username = " + username +  componentIDs.toString() + "}";
    }
}
