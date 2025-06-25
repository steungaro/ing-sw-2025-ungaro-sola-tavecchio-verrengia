package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the methods for managing lobbies in the game and allows multiple matches to be played simultaneously.
 */
public interface MatchControllerInterface extends Remote {
    /**
     * Allows a user to join a lobby by its ID.
     *
     * @param id the id of the lobby
     * @param user the user that wants to join the lobby
     */
    void joinLobby(String id, String user) throws RemoteException;

    /**
     * Creates a new lobby with the specified parameters.
     *
     * @param name the name of the lobby
     * @param maxPlayers the max number of players
     * @param user the owner of the lobby that joins automatically
     * @throws IllegalArgumentException if the max number of lobbies is reached
     */
    void createLobby(String name, String user, int maxPlayers, int level) throws RemoteException;

    /**
     * Allows a user to leave a lobby.
     *
     * @param userid the id of the user that wants to leave the lobby
     * @throws RemoteException if a remote communication error occurs
     */
    void leaveLobby(String userid) throws RemoteException;

    /**
     * @param username the user that wants to start the lobby they're into
     */
    void startLobby(String username) throws RemoteException;

    /**
     * Kills the lobby that the user is in.
     *
     * @param username the user that wants to kill the lobby
     * @throws RemoteException if a remote communication error occurs
     */
    void killLobby(String username) throws RemoteException;

    /**
     * Retrieves the list of lobbies available for a given user.
     * @param username the username of the user requesting the lobbies
     * @throws RemoteException if a remote communication error occurs
     */
    void getLobbies(String username) throws RemoteException;
}