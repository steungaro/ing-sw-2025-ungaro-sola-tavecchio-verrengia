package it.polimi.ingsw.gc20.server.network;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * The NetworkService class is a singleton that provides services for managing
 * network-connected clients and performing message-related operations.
 * It maintains a registry of clients, facilitates communication between them,
 * and supports broadcasting messages to all connected clients.
 */
public class NetworkService {
    private static NetworkService instance;
    private final Map<String, ClientHandler> clients = new HashMap<>();

    /**
     * Constructs a new instance of the NetworkService class.
     * <p>
     * This constructor is marked private to enforce the singleton pattern, ensuring
     * that only one instance of the NetworkService class can be created and managed
     * throughout the application lifecycle. The instance can be obtained using the
     * {@code NetworkService.getInstance()} method.
     */
    private NetworkService() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the singleton instance of the {@code NetworkService} class.
     * If no instance exists, a new one is created and returned. This ensures that
     * only one instance of {@code NetworkService} is used throughout the application.
     *
     * @return the singleton instance of {@code NetworkService}
     */
    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    /**
     * Registers a new client in the NetworkService by adding the client to the internal
     * registry. The client's username, retrieved using {@code getClientUsername()},
     * is used as the key to uniquely identify the client within the registry.
     *
     * @param client the {@code ClientHandler} instance representing the client to be
     *               registered. It must provide a unique username via the
     *               {@code getClientUsername()} method.
     */
    public void registerClient(ClientHandler client) {
        clients.put(client.getClientUsername(), client);
    }

    /**
     * Removes a client from the internal registry managed by the NetworkService.
     * The client is identified by their unique username, and upon removal,
     * they will no longer be managed or accessible within the network service.
     *
     * @param username the unique identifier of the client to be removed from
     *                 the internal registry
     */
    public void removeClient(String username) {
        clients.remove(username);
    }

    /**
     * Retrieves a client from the internal registry by their unique username.
     * The method looks up the client in the registry and returns the corresponding
     * {@code ClientHandler} instance if it exists.
     *
     * @param username the unique identifier of the client to be retrieved
     * @return the {@code ClientHandler} associated with the given username,
     *         or {@code null} if no client exists with the specified username
     */
    public ClientHandler getClient(String username) {
        return clients.get(username);
    }

    /**
     * Sends a message to a specific client identified by their username.
     * If the client is registered in the service, the message will be
     * delivered to the corresponding {@code ClientHandler}.
     *
     * @param username the unique identifier of the client to whom the message will be sent
     * @param message  the message to be sent to the specified client
     */
    public void sendToClient(String username, Message message) {
        ClientHandler client = clients.get(username);
        System.out.println("\n Sending message to client: " + username + " - Message: " + message + "\n");
        if (client != null) {
            client.sendToClient(message);
        }
    }

    /**
     * Broadcasts a message to all connected clients in the network.
     * This method iterates through the collection of registered clients
     * and sends the specified message to each client using their respective {@code sendToClient} method.
     *
     * @param message the {@code Message} instance to be sent to all connected clients.
     */
    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients.values()) {
            client.sendToClient(message);
        }
    }
}
