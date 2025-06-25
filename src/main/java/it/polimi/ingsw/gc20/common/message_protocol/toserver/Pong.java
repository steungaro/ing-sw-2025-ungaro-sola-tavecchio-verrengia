package it.polimi.ingsw.gc20.common.message_protocol.toserver;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;

/**
 * This message is sent by the client to the server in response to a ping.
 * It indicates that the client is still connected and responsive.
 */
public record Pong(
        String username
) implements Message {
    @Override
    public String toString() {
        return "Pong{" +
                "username =" + username + "}";
    }

    @Override
    public void handleMessage() {
        HeartbeatService.getInstance().handlePingResponse(username);
    }
}