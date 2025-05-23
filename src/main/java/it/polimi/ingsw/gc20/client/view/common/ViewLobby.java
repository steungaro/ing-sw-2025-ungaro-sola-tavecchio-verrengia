package it.polimi.ingsw.gc20.client.view.common;

import java.util.List;

public class ViewLobby {
    private final String name;
    private final int maxPlayers;
    private final List<String> playersList;
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
        return playersList.getFirst();
    }

    public String toString(){
        return "Name: " + name + " |" + " Level: " + (level == 0 ? "Learner" : "Normal") +" | Owner: " + playersList.getFirst() + " | Players: " + playersList.size() + "/" + maxPlayers;
    }
}
