package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.io.Serializable;

// Message generated when a player places the component in their hand on the ship
public record PlaceComponentMessage(
        String username, // username of the player
        String gameId, // id of the game
        Pair<Integer, Integer> coordinates // coordinates of the component
) implements Message, Serializable {
    @Override
    public String toString() {
        return "PlaceComponentMessage {username = " + username + ", gameId = " + gameId + ", coordinates = " + coordinates + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).placeComponent(username, coordinates);
    }
}
