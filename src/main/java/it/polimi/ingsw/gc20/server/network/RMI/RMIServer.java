package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.AuthInterface;
import it.polimi.ingsw.gc20.server.exceptions.ServerCriticalError;
import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.Server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The RMIServer class is responsible for managing a server implementation
 * that utilizes Java RMI (Remote Method Invocation) for communication.
 * It includes operations for starting and stopping the server, managing
 * client connections, and registering services in the RMI registry.
 *
 * This class implements the Server interface, ensuring compliance with
 * the core server management methods and providing a specific RMI-based
 * implementation.
 */
public class RMIServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(RMIServer.class.getName());
    private static RMIServerHandler rmiServerHandler = null;
    private final List<ClientHandler> clients;
    private final ExecutorService executor;
    private static final int DEFAULT_PORT = 1099;

    /**
     * Constructor for the RMIServer class.
     */
    public RMIServer() {
        rmiServerHandler = RMIServerHandler.getInstance();
        this.clients = new CopyOnWriteArrayList<>();
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void start() {
        try {
            // Create the RMI registry
            rmiServerHandler.createRegistry(DEFAULT_PORT);

            // creation of the gameService
            GameControllerInterface gameService = new RMIGameControllerService();

            // creation of the matchService
            MatchControllerInterface matchService = new RMIMatchControllerService();

            // creating the authService
            AuthInterface authService = new RMIAuthService(this);
            rmiServerHandler.exportObject(authService, "AuthService");

            // Export the gameService object
            rmiServerHandler.exportObject(gameService, "GameService");

            // Export the matchService object
            rmiServerHandler.exportObject(matchService, "MatchService");

            LOGGER.info(String.format("RMI Server started at port %d", rmiServerHandler.getCurrentPort()));
        } catch (ServerCriticalError e) {
            // Error in the creation of the registry
            LOGGER.severe("Error in the creation of the RMI registry: " + e.getMessage());
        } catch (RemoteException e) {
            // Error in the export of the object
            LOGGER.severe("Error in the export of the RMI object: " + e.getMessage());
        } // Error in the formation of the RMI URL

    }

    @Override
    public void stop() {
        clients.forEach(ClientHandler::disconnect);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOGGER.info("RMI Server stopped");
    }

    @Override
    public void registerClient (ClientHandler client) {
        clients.add(client);
        NetworkService.getInstance().registerClient(client);
        LOGGER.info("registered client: " + client.getClientUsername());
    }

    @Override
    public void removeClient (ClientHandler client) {
        clients.remove(client);
        NetworkService.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("removed client: " + client.getClientUsername());
    }

    @Override
    public void updateClient(String username, ClientHandler client) {
        ClientHandler existingClient = NetworkService.getInstance().getClient(username);
        if (existingClient != null) {
            // Remove the old client
            this.removeClient(existingClient);
            // Add the new client
            this.registerClient(client);
        } else {
            LOGGER.warning("Client not found: " + username);
            return;
        }
        LOGGER.info("Client updated (RMI): " + client.getClientUsername());
    }
}
