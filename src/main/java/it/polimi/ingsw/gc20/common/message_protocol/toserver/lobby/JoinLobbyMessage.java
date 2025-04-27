package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record JoinLobbyMessage(
        String username, // the username of the player who wants to join the lobby
        String lobbyCode // the code of the lobby to join
) implements Message {
    @Override
    public String toString() {
        return "JoinLobbyMessage {username = " + username + ", lobbyCode = " + lobbyCode + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().joinLobby(username, lobbyCode);
    }
}
