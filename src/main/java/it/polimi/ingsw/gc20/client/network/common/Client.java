package it.polimi.ingsw.gc20.client.network.common;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;

/**
 * The Client interface defines the methods that a client must implement to interact with the game server.
 * It extends GameControllerInterface and MatchControllerInterface to provide game and match functionalities.
 */
public interface Client extends GameControllerInterface, MatchControllerInterface {

    /**
     * Starts the client, establishing a connection to the server.
     */
    void start();

    /**
     * Stops the client, closing the connection to the server.
     */
    void stop();

    /**
     * Checks if the client is currently connected to the server.
     *
     * @return true if connected, false otherwise.
     */
    boolean isConnected();

    /**
     * Sends a login message to the server.
     *
     * @param username the username of the player logging in.
     */
    void login(String username);

    /**
     * Sends a logout message to the server.
     *
     * @param username the username of the player logging out.
     * @deprecated This method is deprecated and should not be used.
     */
    @Deprecated
    void logout(String username);

    /**
     * Returns the address of the server to which the client is connected.
     *
     * @return the server address as a String.
     */
    String getAddress();

    /**
     * Returns the port number of the server to which the client is connected.
     *
     * @return the server port as an integer.
     */
    int getPort();

    /**
     * Sends a ping message to the server to check if the connection is still alive.
     *
     * @param username the username of the player sending the ping.
     */
    void pong(String username);
}
