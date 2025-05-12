package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.List;

public record ConnectionStatusMessage(
        List<String> connectedPlayers,
        List<String> disconnectedPlayers
) implements Message {
    @Override
    public String toString() {
        return "connectedPlayers=" + connectedPlayers +
                ", disconnectedPlayers=" + disconnectedPlayers;
    }

    @Override
    public void handleMessage() {
        //TODO
    }
}
