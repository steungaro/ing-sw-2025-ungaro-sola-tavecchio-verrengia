package it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent from the client to the server to create a new lobby.
 * It contains the name of the lobby, the username of the player who wants to create it,
 * the maximum number of players allowed in the lobby, and the level of the lobby.
 */
public record CreateLobbyMessage(
        String name, // the name of the lobby
        String username, // the username of the player who wants to create the lobby
        int maxPlayers, // the maximum number of players in the lobby
        int level // the level of the lobby
) implements Message {
    @Override
    public String toString() {
        return "CreateLobbyMessage {name = " + name + ", username = " + username + ", maxPlayers = " + maxPlayers + ", level = " + level + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().createLobby(name, username, maxPlayers, level);
    }
}
