package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

// Message that is generated when a player finishes the assembly of the ship
public record AssemblyEndedMessage(
        String username, // the username of the player who finished the assembly
        int position // the position where the player wants to start
) implements Message {
    @Override
    public String toString() {
        return "AssemblyEndedMessage {username = " + username + "position = " + position + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).stopAssembling(username, position);
    }
}
