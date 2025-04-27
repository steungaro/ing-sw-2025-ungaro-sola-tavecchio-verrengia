package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import org.javatuples.Pair;

// Message generated when a player places the component in their hand on the ship
public record PlaceComponentMessage(
        String username, // username of the player
        Pair<Integer, Integer> coordinates // coordinates of the component
) implements Message {
    @Override
    public String toString() {
        return "PlaceComponentMessage {username = " + username + ", coordinates = " + coordinates + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).placeComponent(username, coordinates);
    }
}
