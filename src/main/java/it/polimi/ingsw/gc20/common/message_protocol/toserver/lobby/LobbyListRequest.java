package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.controller.MatchController;

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
