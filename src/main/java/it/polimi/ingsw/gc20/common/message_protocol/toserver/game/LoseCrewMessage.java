package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.util.List;

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
