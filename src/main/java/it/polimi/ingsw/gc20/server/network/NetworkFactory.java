package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.network.RMI.RMIServer;
import it.polimi.ingsw.gc20.server.network.common.Server;
import it.polimi.ingsw.gc20.server.network.socket.SocketServer;

import java.util.ArrayList;
import java.util.List;

/**
 * The NetworkFactory class is responsible for managing network servers
 * and provides functionality to initialize the network service, create servers,
 * and manage all servers within the network.
 */
public class NetworkFactory {
    private final List<Server> servers = new ArrayList<>();

    /**
     * Initializes the network service by ensuring that the singleton instance of
     * {@link NetworkService} is created. This method must be called prior to using
     * any functionality dependent on the network service.
     */
    public void initialize() {
        NetworkService.getInstance(); // Ensure a singleton is created
    }

    /**
     * Creates and initializes a new server instance based on the specified type.
     * The created server is then added to the collection of managed servers.
     *
     * @param type the type of server to create. Can be either {@code ServerType.RMI} or {@code ServerType.SOCKET}.
     */
    public void createServer(ServerType type) {
        Server server = switch (type) {
            case RMI -> new RMIServer();
            case SOCKET -> new SocketServer();
        };
        servers.add(server);
    }

    /**
     * Starts all the servers managed by the NetworkFactory instance.
     * <p>
     * This method iterates through the list of servers and invokes the {@code start}
     * method on each server. It ensures that all servers within the network
     * are activated and ready to handle incoming connections or operations.
     * <p>
     * This method assumes that the servers have already been created and added
     * to the collection in the factory using {@code createServer}.
     * <p>
     * Note: Ensure the network is properly initialized using the {@code initialize}
     * method before invoking this method, as server start operations may depend on it.
     */
    public void startAllServers() {
        for (Server server : servers) {
            server.start();
        }
    }

    /**
     * Stops all the servers managed by the NetworkFactory instance.
     * <p>
     * This method iterates through the list of servers and invokes the {@code stop}
     * method on each server. It ensures that all servers within the network
     * are properly shut down and no longer handle incoming connections or operations.
     */
    public void stopAllServers() {
        for (Server server : servers) {
            server.stop();
        }
    }

    /**
     * Represents the types of servers that can be instantiated and managed within the network system.
     * The server types are used to define the specific implementation of server behavior and
     * communication mechanisms.
     * The available options include:
     * - RMI: Remote Method Invocation-based server providing remote communication capabilities.
     * - SOCKET: Socket-based server providing low-level network communication.
     */
    public enum ServerType {
        RMI, SOCKET
    }
}