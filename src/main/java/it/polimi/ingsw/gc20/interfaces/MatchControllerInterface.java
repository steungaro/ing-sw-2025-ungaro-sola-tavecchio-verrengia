package it.polimi.ingsw.gc20.interfaces;
import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.util.List;

public interface MatchControllerInterface {
    /**
     * Gets the game controller for the given id
     *
     * @param id the game identifier
     * @return the game controller for the given id, null if not found
     */
    GameController getGameController(String id);

    /**
     * Gets the list of players currently in lobbies
     *
     * @return the list of players in lobbies
     */
    List<String> getPlayersInLobbies();

    /**
     * Sets the maximum number of lobbies allowed
     *
     * @param maxLobbies the max number of lobbies
     */
    void setMaxLobbies(int maxLobbies);

    /**
     * Sets the maximum number of concurrent matches allowed
     *
     * @param maxMatches the max number of matches
     */
    void setMaxMatches(int maxMatches);

    /**
     * Gets all available lobbies
     *
     * @return the list of lobbies
     */
    List<Lobby> getLobbies();

    /**
     * Gets a specific lobby by its ID
     *
     * @param id the id of the lobby
     * @return the lobby with the given id, null if not found
     */
    Lobby getLobby(String id);

    /**
     * Adds a user to an existing lobby
     *
     * @param id the id of the lobby
     * @param user the user that wants to join
     * @return the lobby with the given id, null if not found
     */
    Lobby joinLobby(String id, String user);

    /**
     * Creates a new lobby
     *
     * @param name the name of the lobby
     * @param user the owner of the lobby that joins automatically
     * @param maxPlayers the max number of players
     * @param level the difficulty level
     * @return the created lobby
     * @throws IllegalArgumentException if the max number of lobbies is reached
     */
    Lobby createLobby(String name, String user, int maxPlayers, int level);

    /**
     * Removes a user from their current lobby
     *
     * @param userid the id of the user to leave, if the lobby is empty it is removed
     * @throws IllegalArgumentException if the user is not in any lobby
     */
    void leaveLobby(String userid);

    /**
     * Terminates a game session
     *
     * @param id the id of the game to end
     */
    void endGame(String id);

    /**
     * Starts a game from a lobby
     *
     * @param id the id of the lobby to start
     * @throws IllegalArgumentException if the lobby is not found or if the max number of matches is reached
     */
    void startLobby(String id);
}