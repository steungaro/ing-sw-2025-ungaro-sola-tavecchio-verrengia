package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import org.javatuples.Pair;

public record ChooseBranchMessage(
        String username, // the username of the player who wants to choose a branch
        Pair<Integer, Integer> coordinates // the coordinates of a component of the chosen branch
) implements Message {
    @Override
    public String toString() {
        return "ChooseBranchMessage {username = " + username + ", coordinates = " + coordinates + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).chooseBranch(username, coordinates);
    }
}
