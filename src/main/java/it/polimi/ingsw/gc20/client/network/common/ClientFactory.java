package it.polimi.ingsw.gc20.client.network.common;

import it.polimi.ingsw.gc20.client.network.RMI.RMIClient;
import it.polimi.ingsw.gc20.client.network.socket.SocketClient;

public class ClientFactory {
    private static ClientFactory instance;

    private ClientFactory() {
        // Private constructor to prevent instantiation
    }

    public static ClientFactory getInstance() {
        if (instance == null) {
            instance = new ClientFactory();
        }
        return instance;
    }

    public Client createClient(String clientType, String serverAddress, int port) {
        return switch (clientType.toUpperCase()) {
            case "SOCKET" -> new SocketClient(serverAddress, port);
            case "RMI" -> new RMIClient(serverAddress, port);
            default -> throw new IllegalArgumentException("Unknown client type: " + clientType);
        };
    }
}
