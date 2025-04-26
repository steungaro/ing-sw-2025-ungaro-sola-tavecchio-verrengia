package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * Message that is generated when a player activates the double cannons
 */
public record ActivateDoubleCannonsMessage(
        String username, // username of the player
        String gameId, // id of the game
        List<Pair<Integer, Integer>> cannonsCoordinates, // coordinates of the cannons
        List<Pair<Integer, Integer>> batteriesCoordinates  // coordinates of the batteries
) implements Message, Serializable {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsMessage {username = " + username + "game = " + gameId + "cannons = " + cannonsCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).activateCannons(username, cannonsCoordinates, batteriesCoordinates);
    }
}
