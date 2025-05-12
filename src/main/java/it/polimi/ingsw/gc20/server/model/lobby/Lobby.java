package it.polimi.ingsw.gc20.server.model.lobby;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.FullLobbyException;
import it.polimi.ingsw.gc20.server.exceptions.LobbyException;

import java.util.*;

/**
 * 
 */
public class Lobby {
    private final List<String> users;
    private String id;
    private int maxPlayers;
    private String ownerUsername;
    private String name;
    private final int level;

    /**
     * Default constructor
     */
    public Lobby(String id, String name, String ownerUsername, int maxPlayers, int level) {
        this.users = new ArrayList<>();
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.ownerUsername = ownerUsername;
        this.name = name;
        this.users.add(ownerUsername);
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public List<String> getUsers() {
        return users;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean containsUser(String user) {
        return users.contains(user);
    }

    /**
     * @param user is the username of the player that wants to leave the lobby
     *             the owner of the lobby can't leave the lobby
     */
    public void removePlayer(String user) throws LobbyException{
        if (user.equals(ownerUsername)) {
            throw new LobbyException("The owner of the lobby can't leave the lobby");
        } else {
            this.users.remove(user);
        }
    }

    /**
     * @param user is the username of the player that wants to join the lobby
     */
    public void addPlayer(String user) throws FullLobbyException{
        if (!users.contains(user) && users.size() < maxPlayers) {
            this.users.add(user);
        } else {
            throw new FullLobbyException("The lobby is full");
        }
    }

    /**
     * @return the game controller created from the lobby
     * @see GameController
     */
    public GameController createGameController() {
        return new GameController(id, users, level);
    }

    public void kill() {
        this.users.clear();
        this.id = null;
        this.maxPlayers = 0;
        this.ownerUsername = null;
        this.name = null;
    }
}