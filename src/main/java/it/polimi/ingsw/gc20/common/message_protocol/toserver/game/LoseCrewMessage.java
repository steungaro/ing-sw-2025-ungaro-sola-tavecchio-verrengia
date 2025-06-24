package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;

import java.util.List;

/**
 * Message that is generated when a player loses a crew member.
 * This message contains the username of the player and the coordinates of the crew member that was lost.
 */
public record LoseCrewMessage(
        String username, // username of the player that loses a crew member
        List<Pair<Integer, Integer>> coordinates
) implements Message {
    @Override
    public String toString() {
        return "LoseCrewMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).loseCrew(username, coordinates);
    }
}
