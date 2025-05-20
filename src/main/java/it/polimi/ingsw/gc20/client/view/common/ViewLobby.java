package it.polimi.ingsw.gc20.client.view.common;

import java.util.ArrayList;
import java.util.List;

public class ViewLobby {
    private final String name;
    private final int maxPlayers;
    List<String> playersList;
    private final int level;


    public ViewLobby(String name, int maxPlayers, int level, List<String> playersList) {
        this.name = name;
        this.playersList = playersList;
        this.maxPlayers = maxPlayers;
        this.level = level;
    }

    public String getID() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public List<String> getPlayersList() {
        return playersList;
    }
    public int getMaxPlayers(){
        return maxPlayers;
    }

    public String getOwner(){
        return playersList.get(0);
    }

    public String toString(){
        return name + " " + level +" " + playersList;
    }
}
