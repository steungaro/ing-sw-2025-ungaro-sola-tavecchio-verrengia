package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent from the client to the server to start a lobby.
 */
public record StartLobbyMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return "StartLobbyMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().startLobby(username);
    }
}
