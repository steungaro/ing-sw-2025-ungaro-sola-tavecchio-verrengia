package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Message that is generated when a player activates the double engine
 */
public record ActivateDoubleEnginesMessage(
        String username, // username of the player
        String gameId, // id of the game
        List<Pair<Integer, Integer>> enginesCoordinates, // coordinates of the engines
        List<Pair<Integer, Integer>> batteriesCoordinates // coordinates of the batteries
) implements Message, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsMessage {username = " + username + "game = " + gameId + "engines = " + enginesCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).activateEngines(username, enginesCoordinates, batteriesCoordinates);
    }
}
