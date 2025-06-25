package it.polimi.ingsw.gc20.client.view.common;

import java.util.List;

/**
 * Represents a lobby in the game, containing information about its name, maximum players,
 * level, and a list of players currently in the lobby.
 */
public class ViewLobby {
    private final String name;
    private final int maxPlayers;
    private final List<String> playersList;
    private final int level;


    /**
     * Constructs a ViewLobby instance with the specified parameters.
     *
     * @param name         the name of the lobby
     * @param maxPlayers   the maximum number of players allowed in the lobby
     * @param level        the level of the lobby (0 for learner, 1 for normal)
     * @param playersList  the list of players currently in the lobby
     */
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

    @Override
    public String toString(){
        return "Name: " + name + " |" + " Level: " + (level == 0 ? "Learner" : "Normal") +" | Owner: " + playersList.getFirst() + " | Players: " + playersList.size() + "/" + maxPlayers;
    }
}
