package it.polimi.ingsw.gc20.client.network;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.network.common.ClientFactory;

import java.util.logging.Logger;

public class NetworkManager {
    private static final int RMI_PORT = 1099;
    private static final int SOCKET_PORT = 8080;
    private static final String DEFAULT_SERVER_ADDRESS = "localhost"; // Default server address
    private static final Logger LOGGER = Logger.getLogger(NetworkManager.class.getName());

    public static Client initConnection (String clientType) {
        return initConnection(clientType, DEFAULT_SERVER_ADDRESS);
    }

    public static Client initConnection (String clientType, String serverAddress) {
        return initConnection(clientType, serverAddress, clientType.equals("RMI") ? RMI_PORT : SOCKET_PORT);
    }

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
