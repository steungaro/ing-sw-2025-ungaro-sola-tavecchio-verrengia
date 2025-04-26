package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.Serializable;

// Message that is generated when a player finishes the assembly of the ship
public record AssemblyEndedMessage(
        String username, // the username of the player who finished the assembly
        String gameId, // the id of the game
        int position // the position where the player wants to start
) implements Message, Serializable {
    @Override
    public String toString() {
        return "AssemblyEndedMessage {username = " + username + "gameId = " + gameId + "position = " + position + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).stopAssembling(username, position);
    }
}
