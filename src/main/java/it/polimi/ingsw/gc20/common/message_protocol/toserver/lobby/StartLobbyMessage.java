package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record StartLobbyMessage(
        String username,
        String lobbyId
) implements Message {
    @Override
    public String toString() {
        return "StartLobbyMessage {username = " + username + ", lobbyId = " + lobbyId + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().startLobby(lobbyId);
    }
}
