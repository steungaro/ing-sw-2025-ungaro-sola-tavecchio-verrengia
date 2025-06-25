package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent to the client by the Heartbeat thread to check if the client is still connected.
 */
public record Ping(
        String username // The username of the receiving client (broadcast to all clients)
) implements Message {
    @Override
    public String toString() {
        return "Ping{}";
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().ping();
    }
}
