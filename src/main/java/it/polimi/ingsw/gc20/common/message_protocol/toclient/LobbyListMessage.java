package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record LobbyListMessage(
    List<LobbyInfo> lobbies
) implements Message {

    public record LobbyInfo(String lobbyName, int maxNumberOfPlayers, int level, List<String>players) implements Serializable {}
    @Override
    public String toString() {
        return "lobbies=" + lobbies;
    }

    @Override
    public void handleMessage() {
        List<ViewLobby> viewLobbies = new ArrayList<>();
        for (LobbyInfo lobby : lobbies) {
            viewLobbies.add(new ViewLobby(lobby.lobbyName, lobby.maxNumberOfPlayers, lobby.level, lobby.players));
        }
        ClientGameModel.getInstance().setLobbyList(viewLobbies);
        ClientGameModel.getInstance().mainMenuState();
    }
}
