package it.polimi.ingsw.gc20.client.network.common;

import it.polimi.ingsw.gc20.client.network.RMI.RMIClient;
import it.polimi.ingsw.gc20.client.network.socket.SocketClient;

/**
 * Factory class for creating client instances based on the specified type.
 * This class implements the Singleton design pattern to ensure only one instance exists.
 */
public class ClientFactory {
    private static ClientFactory instance;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private ClientFactory() {}

    /**
     * Returns the singleton instance of ClientFactory.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of ClientFactory
     */
    public static ClientFactory getInstance() {
        if (instance == null) {
            instance = new ClientFactory();
        }
        return instance;
    }

/**
     * Creates a client based on the specified type.
     *
     * @param clientType the type of client to create (e.g., "SOCKET", "RMI")
     * @param serverAddress the address of the server to connect to
     * @param port the port number to connect to
     * @return an instance of Client based on the specified type
     * @throws IllegalArgumentException if the client type is unknown
     */
    public Client createClient(String clientType, String serverAddress, int port) {
        return switch (clientType.toUpperCase()) {
            case "SOCKET" -> new SocketClient(serverAddress, port);
            case "RMI" -> new RMIClient(serverAddress, port);
            default -> throw new IllegalArgumentException("Unknown client type: " + clientType);
        };
    }
}
