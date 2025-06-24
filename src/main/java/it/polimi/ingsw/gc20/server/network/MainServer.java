package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

import java.util.Scanner;

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

        System.out.println("Network service initialized.");

        System.out.println("Please provide the maximum number of lobbies and the maximum number of matches simultaneously running.");
        System.out.println("Maximum number of lobbies: ");
        System.out.print(" > ");
        Scanner scanner = new Scanner(System.in);
        int maxLobbies = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Maximum number of matches: ");
        System.out.print(" > ");
        int maxMatches = Integer.parseInt(scanner.nextLine().trim());

        MatchController.getInstance(maxMatches, maxLobbies);
        // Create servers

        System.out.println("Please provide the port for the RMI server (default one is 1099): ");
        System.out.print(" > ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            input = "1099"; // Default RMI port
        }
        int rmiPort = Integer.parseInt(input);
        System.out.println("Please provide the port for the Socket server (default one is 8080): ");
        System.out.print(" > ");
        input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            input = "8080"; // Default Socket port
        }
        int socketPort = Integer.parseInt(input);
        networkFactory.createServer(NetworkFactory.ServerType.RMI, rmiPort);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET, socketPort);
        networkFactory.startAllServers();

        // Start the heartbeat service
        HeartbeatService.getInstance().start();

        // Start the queue handler
        QueueHandler.getInstance();

        System.out.println("Servers, heartbeat service and queue handler started successfully.");
        System.out.println("Press Ctrl+C to stop the servers and exit.");

        // Add a shutdown hook to stop servers on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkFactory.stopAllServers();
            HeartbeatService.getInstance().stop();
            QueueHandler.getInstance().shutdown();
            System.out.println("Servers, queue handler and heartbeat service stopped.");
        }));
    }
}