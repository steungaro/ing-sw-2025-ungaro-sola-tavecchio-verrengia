package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.List;

public record LobbyMessage(
        List<String> players,
        String lobbyName,
        int level
) implements Message {
    @Override
    public String toString() {
        return "lobby data:" +
                "players=" + players +
                ", lobbyName='" + lobbyName + '\'' +
                ", Level=" + level;
    }

    @Override
    public void handleMessage() {
        // Handle the lobby message (client side)
    }
}
