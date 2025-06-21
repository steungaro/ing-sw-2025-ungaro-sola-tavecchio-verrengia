package it.polimi.ingsw.gc20.server.model.lobby;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.FullLobbyException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.LobbyException;

import java.util.*;

/**
 * The Lobby class represents a game lobby that manages players and their interactions
 * before starting a game. It holds information such as the lobby's ID, name, owner,
 * maximum number of players, and the players currently in the lobby.
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

    /**
     * Retrieves the unique identifier of the lobby.
     *
     * @return the ID of the lobby
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the level of the lobby.
     *
     * @return the level of the lobby
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the name of the lobby.
     *
     * @return the name of the lobby
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the username of the owner of the lobby.
     *
     * @return the username of the lobby owner
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * Retrieves the list of usernames of the users currently in the lobby.
     *
     * @return a list of usernames representing the users in the lobby
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * Retrieves the maximum number of players allowed in the lobby.
     *
     * @return the maximum number of players that can join the lobby
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Updates the maximum number of players allowed in the lobby.
     *
     * @param maxPlayers the new maximum number of players that can join the lobby
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Updates the username of the owner of the lobby.
     *
     * @param ownerUsername the new username of the owner
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * Updates the name of the lobby.
     *
     * @param name the new name of the lobby
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the unique identifier of the lobby.
     *
     * @param id the new unique identifier of the lobby
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Checks if the specified user is present in the lobby's list of users.
     *
     * @param user the username of the user to check for in the lobby
     * @return true if the user is present in the lobby, false otherwise
     */
    public boolean containsUser(String user) {
        return users.contains(user);
    }

    /**
     * Removes a player from the lobby. If the user being removed is the owner of the lobby,
     * and the lobby still contains other users, an exception is thrown since the owner cannot leave
     * unless they are the last user in the lobby.
     *
     * @param user the username of the player to be removed from the lobby
     * @throws LobbyException if the user is the lobby owner and the lobby is not empty
     */
    public void removePlayer(String user) throws LobbyException{
        if (user.equals(ownerUsername) && users.size() != 1) {
            throw new LobbyException("The owner of the lobby can't leave the lobby");
        } else {
            this.users.remove(user);
        }
    }

    /**
     * Adds a player to the lobby if the maximum number of players has not been reached
     * and the user is not yet present in the lobby.
     *
     * @param user the username of the player to add to the lobby
     * @throws FullLobbyException if the lobby has reached its maximum capacity
     */
    public void addPlayer(String user) throws FullLobbyException{
        if (!users.contains(user) && users.size() < maxPlayers) {
            this.users.add(user);
        } else {
            throw new FullLobbyException("The lobby is full");
        }
    }

    /**
     * Creates and initializes a new instance of the GameController tied to the current lobby settings.
     * The GameController is responsible for managing the game's lifecycle and operations.
     *
     * @return a newly created GameController instance configured with the lobby's state
     * @throws InvalidStateException if the lobby state is invalid for initializing a game
     */
    public GameController createGameController() throws InvalidStateException {
        return new GameController(name, id, users, level);
    }

    /**
     * This method is generally used to completely invalidate and clean up a lobby
     * instance, ensuring it is no longer operable or associated with its previous state.
     */
    public void kill() {
        this.users.clear();
        this.id = null;
        this.maxPlayers = 0;
        this.ownerUsername = null;
        this.name = null;
    }
}