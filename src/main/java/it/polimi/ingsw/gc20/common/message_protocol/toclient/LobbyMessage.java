package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.util.List;

/**
 * This message is sent to the client to provide information about the lobby they're in.
 */
public record LobbyMessage(
        List<String> players,
        String lobbyName,
        int level,
        int maxPlayers
) implements Message {
    @Override
    public String toString() {
        return "lobby data:" +
                "players=" + players +
                ", lobbyName='" + lobbyName + '\'' +
                ", Level=" + level;
    }

    @Override
    public void handleMessage() {
        ViewLobby viewLobby = new ViewLobby(lobbyName, maxPlayers, level, players);
        ClientGameModel.getInstance().updateLobby(viewLobby);
        ClientGameModel.getInstance().inLobbyMenu();
    }
}
