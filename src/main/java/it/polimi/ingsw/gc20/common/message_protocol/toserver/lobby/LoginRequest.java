package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import java.io.Serializable;

public record LoginRequest(
        String username
) implements Serializable {
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
