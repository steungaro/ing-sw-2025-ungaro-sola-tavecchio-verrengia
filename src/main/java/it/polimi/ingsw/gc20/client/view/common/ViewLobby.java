package it.polimi.ingsw.gc20.client.view.common;

public class ViewLobby {
    private final String name;
    private final String owner;
    private final int maxPlayers;
    private int players;
    private final int level;
    private String currentPlayer;


    public ViewLobby(String name, String owner, int maxPlayers, int level) {
        this.name = name;
        this.owner = owner;
        this.maxPlayers = maxPlayers;
        this.level = level;
    }

    public Object getOwner() {
        return owner;
    }

    public String getID() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String toString(){
        return name + " (owner: " + owner + ", players: " + players + "/" + maxPlayers + ", level: " + level + ")";
    }
}
