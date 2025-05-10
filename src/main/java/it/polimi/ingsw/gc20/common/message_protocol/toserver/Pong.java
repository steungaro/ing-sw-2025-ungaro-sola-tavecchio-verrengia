package it.polimi.ingsw.gc20.common.message_protocol.toserver;

import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;

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
