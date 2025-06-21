package it.polimi.ingsw.gc20.server.network.common;


/**
 * The Server interface defines a set of core methods for managing the lifecycle and client interactions
 * of a server. Implementing classes are expected to provide the logic for handling server functionality,
 * such as starting and stopping the server, as well as managing client connections.
 */
public interface Server {
    /**
     * Starts the server and initializes its operational state.
     * Implementations of this method should provide the logic to prepare the server
     * for accepting client connections and handling requests.
     */
    void start();
    /**
     * Stops the server and halts its operational state.
     * Implementations of this method should provide the logic to terminate
     * the server's activity, including stopping client connections and releasing
     * any resources held by the server.
     */
    void stop();
    /**
     * Registers a new client to the server using the specified client handler.
     * This method is intended to add a client to the server's active client list,
     * enabling communication and management of the client connection.
     *
     * @param client the {@code ClientHandler} instance representing the client to be registered.
     *               It provides the logic and communication interface for handling the client.
     *               Must not be {@code null}.
     */
    void registerClient(ClientHandler client);
    /**
     * Updates the information of an existing client on the server using the specified username and client handler.
     * This method is used to associate a client handler with a username, potentially replacing the existing
     * mapping or updating the state of the client associated with the given username.
     *
     * @param username the username of the client to be updated. Must not be null and should uniquely identify the client.
     * @param client the ClientHandler instance representing the client to be updated. Must not be null.
     */
    void updateClient(String username, ClientHandler client);
    /**
     * Removes a client from the server using the specified client handler.
     * This method is intended to remove the client represented by the provided
     * {@code ClientHandler} from the server's active client list, stopping any
     * interaction with the client and cleaning up associated resources.
     *
     * @param client the {@code ClientHandler} instance representing the client
     *               to be removed. It provides the logic and communication interface
     *               for handling the client. Must not be {@code null}.
     */
    void removeClient(ClientHandler client);
}
