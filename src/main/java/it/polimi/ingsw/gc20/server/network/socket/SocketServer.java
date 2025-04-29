package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.Server;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginSuccessfulMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SocketServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(SocketServer.class.getName());
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private boolean running;
    private static final int DEFAULT_PORT = 8080;
    private final SocketAuthService authService = new SocketAuthService(this);

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            running = true;
            LOGGER.info("Socket server set up on port: " + DEFAULT_PORT);

            // Thread to accept connections
            new Thread(this::acceptConnections).start();
        } catch (IOException e) {
            LOGGER.severe("Error while setting up socket server: " + e.getMessage());
        }
    }

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
    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients) { // No need to create a copy
            executor.submit(() -> client.sendToClient(message));
        }
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

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        NetworkService.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("Client removed from socket server: " + client.getClientUsername());
    }
}