package it.polimi.ingsw.gc20.client.network;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.network.common.ClientFactory;

import java.util.logging.Logger;

public class NetworkInit {
    private int port;
    private String serverAddress;
    private static final int RMI_PORT = 1099;
    private static final int SOCKET_PORT = 8080;
    private static final String DEFAULT_SERVER_ADDRESS = "localhost"; // Default server address
    private String clientType;
    private Logger logger = Logger.getLogger(NetworkInit.class.getName());

    public Client initConnection (String clientType) {
        return initConnection(clientType, DEFAULT_SERVER_ADDRESS);
    }

    public Client initConnection (String clientType, String serverAddress) {
        return initConnection(clientType, serverAddress, clientType.equals("RMI") ? RMI_PORT : SOCKET_PORT);
    }

    public Client initConnection (String clientType, String serverAddress, int port) {
        switch (clientType) {
            case "RMI":
                this.clientType = "RMI";
                this.port = port;
                this.serverAddress = serverAddress;
                break;
            case "SOCKET":
                this.clientType = "SOCKET";
                this.port = port;
                this.serverAddress = serverAddress;
                break;
            default:
                logger.warning("Client type " + clientType + " not supported.");
                return null;
        }
        // Create the client instance based on the client type
        Client client = ClientFactory.getInstance().createClient(clientType, serverAddress, port);

        // Start the client
        client.start();

        // Add shutdown hook to stop the client gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.stop();
            logger.info("Client stopped.");
        }));
        return client;
    }
}
