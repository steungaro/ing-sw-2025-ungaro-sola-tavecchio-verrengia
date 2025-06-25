package it.polimi.ingsw.gc20.client.network;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.network.common.ClientFactory;

import java.util.logging.Logger;

/**
 * NetworkManager is responsible for initializing the client connection to the server.
 * It supports both RMI and Socket connections.
 */
public class NetworkManager {
    private static final int RMI_PORT = 1099;
    private static final int SOCKET_PORT = 8080;
    private static final String DEFAULT_SERVER_ADDRESS = "localhost"; // Default server address
    private static final Logger LOGGER = Logger.getLogger(NetworkManager.class.getName());

    /**
     * Initializes a client connection based on the specified client type.
     * If no client type is provided, it defaults to "RMI".
     *
     * @param clientType the type of client to initialize (e.g., "RMI" or "Socket")
     * @return an instance of Client if successful, null otherwise
     */
    public static Client initConnection (String clientType) {
        return initConnection(clientType, DEFAULT_SERVER_ADDRESS);
    }

/**
     * Initializes a client connection based on the specified client type and server address.
     * If no port is provided, it defaults to the appropriate port for the client type.
     *
     * @param clientType    the type of client to initialize (e.g., "RMI" or "Socket")
     * @param serverAddress the address of the server to connect to
     * @return an instance of Client if successful, null otherwise
     */
    public static Client initConnection (String clientType, String serverAddress) {
        return initConnection(clientType, serverAddress, clientType.equals("RMI") ? RMI_PORT : SOCKET_PORT);
    }

    /**
     * Initializes a client connection based on the specified client type, server address, and port.
     *
     * @param clientType    the type of client to initialize (e.g., "RMI" or "Socket")
     * @param serverAddress the address of the server to connect to
     * @param port          the port number to connect to
     * @return an instance of Client if successful, null otherwise
     */
    public static Client initConnection (String clientType, String serverAddress, int port) {
        if (clientType == null || serverAddress == null || port < 0) {
            LOGGER.severe("Client type or server address or port is invalid");
            return null;
        }
        if (!clientType.equalsIgnoreCase("RMI") && !clientType.equalsIgnoreCase("socket")) {
            LOGGER.severe("Client type must be either 'RMI' or 'Socket'");
            return null;
        }
        // Create the client instance based on the client type
        Client client = ClientFactory.getInstance().createClient(clientType, serverAddress, port);

        // Start the client
        client.start();

        // Add a shutdown hook to stop the client gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.stop();
            LOGGER.info("Client stopped.");
        }));
        return client;
    }
}
