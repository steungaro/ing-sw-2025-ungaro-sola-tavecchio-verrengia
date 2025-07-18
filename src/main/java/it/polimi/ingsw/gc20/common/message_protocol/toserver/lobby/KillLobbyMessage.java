package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.controller.MatchController;

/**
 * This message is sent to the server to request the termination of a lobby.
 */
public record KillLobbyMessage(
        String username
) implements Message {
    @Override
    public String toString() {
        return "KillLobbyMessage{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().killLobby(username);
    }
}
