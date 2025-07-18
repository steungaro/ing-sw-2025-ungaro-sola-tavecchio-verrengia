package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent from the client to the server to leave a lobby.
 */
public record LeaveLobbyMessage(
        String username // the username of the player who wants to leave the lobby
) implements Message {
    @Override
    public String toString() {
        return "LeaveLobbyMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().leaveLobby(username);
    }
}
