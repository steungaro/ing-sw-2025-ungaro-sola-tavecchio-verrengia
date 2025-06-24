package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The SocketServer class implements the Server interface and provides a socket-based
 * server implementation for handling client connections and communication.
 * It manages client authentication, connection handling, and client registration.
 */
public class SocketServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(SocketServer.class.getName());
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private boolean running;
    private final int port;
    private final SocketAuthService authService = new SocketAuthService(this);

    /**
     * Constructs a SocketServer with the specified port.
     *
     * @param port the port on which the server will listen for incoming connections
     */
    public SocketServer(int port) {
        this.port = port;
        this.running = false;
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            LOGGER.info("Socket server set up on port: " + port);

            // Thread to accept connections
            new Thread(this::acceptConnections).start();
        } catch (IOException e) {
            LOGGER.severe("Error while setting up socket server: " + e.getMessage());
        }
    }

    /**
     * Accepts incoming client connections and handles them through the authService.
     * Each new connection is processed in a separate thread.
     */
    private void acceptConnections() {
        while (running) {
            try {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Client connected: " + clientSocket.getInetAddress());

                // Create a new client handler through the authService (login request)
                // if the client is not accepted, it will return null
                executor.submit(() -> authService.handleNewClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    LOGGER.warning("Error while accepting connections: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void stop() {
        clients.forEach(ClientHandler::disconnect);
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        try {
            serverSocket.close();
            LOGGER.info("Socket server stopped.");
        } catch (IOException e) {
            LOGGER.warning("Error while stopping socket server: " + e.getMessage());
        }
    }

    @Override
    public void registerClient(ClientHandler client) {
        clients.add(client);
        NetworkService.getInstance().registerClient(client);
        LOGGER.info("Client registered through socket: " + client.getClientUsername());
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
        LOGGER.info("Client updated (Socket): " + client.getClientUsername());
    }

    @Override
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        NetworkService.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("Client removed from socket server: " + client.getClientUsername());
    }
}