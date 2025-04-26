package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.Serializable;

// Event that is generated when a player rotates the hourglass
public record TurnHourglassMessage(
    String username, // the username of the player who wants to rotate the hourglass
    String gameId // the id of the game
) implements Message, Serializable {
    @Override
    public String toString() {
        return "TurnHourglassMessage {username = " + username + ", gameId = " + gameId + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).turnHourglass(username);
    }
}
