package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.io.Serializable;
import java.util.List;

public record LobbyListMessage(
    List<LobbyInfo> lobbies
) implements Message {

    public record LobbyInfo(String lobbyName, int maxNumberOfPlayers, int level) implements Serializable {}
    @Override
    public String toString() {
        return "lobbies=" + lobbies;
    }

    @Override
    public void handleMessage() {
        //TODO capire come gestire lobby
    }
}
