package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record Ping(
        String username // The username of the receiving client (broadcast to all clients)
) implements Message {
    @Override
    public String toString() {
        return "Ping{}";
    }

    @Override
    public void handleMessage() {
        // Handle the ping message (client side)
    }
}
