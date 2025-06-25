package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.controller.MatchController;

/**
 * This message is sent to the server to request an updated list of lobbies.
 */
public record LobbyListRequest(
        String username //username of the player requesting the list
) implements Message {
    @Override
    public String toString() {
        return "LobbyListRequest{}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getLobbies(username);
    }
}
