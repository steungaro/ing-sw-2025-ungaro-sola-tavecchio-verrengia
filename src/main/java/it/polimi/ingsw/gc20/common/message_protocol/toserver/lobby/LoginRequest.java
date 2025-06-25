package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.io.Serializable;

/**
 * This message is sent from the client to the server to request a login with a given username.
 * The server will respond with a LobbyMessage if the login is successful.
 * This message does not implement the {@link Message} interface because it's transient and does not require handling on the client side.
 */
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
