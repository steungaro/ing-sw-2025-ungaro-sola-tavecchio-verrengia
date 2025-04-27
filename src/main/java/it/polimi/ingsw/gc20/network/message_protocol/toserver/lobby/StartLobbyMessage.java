package it.polimi.ingsw.gc20.network.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

public record StartLobbyMessage(
        String lobbyId
) implements Message {
    @Override
    public String toString() {
        return "StartLobbyMessage {username = " + lobbyId + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().startLobby(lobbyId);
    }
}
