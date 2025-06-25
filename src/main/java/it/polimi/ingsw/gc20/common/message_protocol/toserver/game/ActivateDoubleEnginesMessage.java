package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;
import java.util.List;

/**
 * Message that is generated when a player activates the double engine
 */
public record ActivateDoubleEnginesMessage(
        String username, // username of the player
        List<Pair<Integer, Integer>> enginesCoordinates, // coordinates of the engines
        List<Pair<Integer, Integer>> batteriesCoordinates // coordinates of the batteries
) implements Message {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsMessage {username = " + username + "engines = " + enginesCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).activateEngines(username, enginesCoordinates, batteriesCoordinates);
    }
}
