package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the methods for authentication in the game.
 * It allows players to log in and log a pong response to check connectivity.
 */
public interface AuthInterface extends Remote {
    /**
     * Logs in a player with the given username.
     *
     * @param username the username of the player
     * @param view the view interface for communication
     * @return true if login is successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean login (String username, ViewInterface view) throws RemoteException;

    /**
     * Logs out a player with the given username.
     *
     * @param username the username of the player
     * @return true if logout is successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     * @deprecated This method is deprecated and should not be used in new implementations.
     */
    @Deprecated
    boolean logout (String username) throws RemoteException;

    /**
     * Logs a pong response from the player to check connectivity.
     *
     * @param username the username of the player
     * @throws RemoteException if a remote communication error occurs
     */
    void pong(String username) throws RemoteException;
}