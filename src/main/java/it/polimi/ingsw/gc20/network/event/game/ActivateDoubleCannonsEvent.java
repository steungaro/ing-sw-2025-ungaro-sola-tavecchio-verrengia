package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Message;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Message that is generated when a player activates the double cannons
 */
public record ActivateDoubleCannonsEvent(
        String username,
        List<Pair<Integer, Integer>> cannonsCoordinates,
        List<Pair<Integer, Integer>> batteriesCoordinates
) implements Message, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsEvent {username = " + username + "cannons = " + cannonsCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }
}
