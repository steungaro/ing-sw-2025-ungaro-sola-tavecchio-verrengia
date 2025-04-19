package it.polimi.ingsw.gc20.network.event.game;

import it.polimi.ingsw.gc20.network.event.Message;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Message that is generated when a player activates the double engine
 */
public record ActivateDoubleEnginesEvent(
        String username,
        List<Pair<Integer, Integer>> enginesCoordinates,
        List<Pair<Integer, Integer>> batteriesCoordinates
) implements Message, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsEvent {username = " + username + "engines = " + enginesCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }
}
