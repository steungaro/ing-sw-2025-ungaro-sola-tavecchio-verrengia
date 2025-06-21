package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

/**
 * The MainServer class serves as the entry point for initializing and starting
 * the necessary parts of the server application. It ensures various services
 * and handlers required for the application operation are properly initialized,
 * started, and managed.
 * <p>
 * Responsibilities:
 * - Initializes the network service and creates the necessary server instances (RMI and Socket).
 * - Manages the lifecycle of the servers, including start and stop operations.
 * - Initializes and starts the HeartbeatService for monitoring purposes.
 * - Initializes the QueueHandler for handling queued operations.
 * - Adds a shutdown hook to gracefully stop all services and servers upon application termination.
 */
public class  MainServer {
    public static void main(String[] args) {
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        MatchController.getInstance(10, 10);
        // Create servers
        networkFactory.createServer(NetworkFactory.ServerType.RMI);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET);
        networkFactory.startAllServers();

        // Start the heartbeat service
        HeartbeatService.getInstance().start();

        // Start the queue handler
        QueueHandler.getInstance();

        // Add shutdown hook to stop servers on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkFactory.stopAllServers();
            HeartbeatService.getInstance().stop();
            QueueHandler.getInstance().shutdown();
            System.out.println("Servers, queue handler and heartbeat service stopped.");
        }));
    }
}