package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;

public record RemoveComponentMessage(
        String username, // the username of the player who wants to remove a component
        Pair<Integer, Integer> coordinates // the coordinates of the component to be removed
) implements Message {
    @Override
    public String toString() {
        return "RemoveComponentMessage {username = " + username + ", coordinates = " + coordinates + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).removeComponentFromShip(username, coordinates);
    }
}
