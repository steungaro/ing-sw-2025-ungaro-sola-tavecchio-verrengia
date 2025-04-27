package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import org.javatuples.Pair;
import java.util.List;

/**
 * Message that is generated when a player activates the double cannons
 */
public record ActivateDoubleCannonsMessage(
        String username, // username of the player
        List<Pair<Integer, Integer>> cannonsCoordinates, // coordinates of the cannons
        List<Pair<Integer, Integer>> batteriesCoordinates  // coordinates of the batteries
) implements Message {

    @Override
    public String toString() {
        return "ActivateDoubleCannonsMessage {username = " + username + "cannons = " + cannonsCoordinates.toString() + " batteries = " + batteriesCoordinates.toString() + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).activateCannons(username, cannonsCoordinates, batteriesCoordinates);
    }
}
