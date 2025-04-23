package it.polimi.ingsw.gc20.network.socket;

import it.polimi.ingsw.gc20.network.NetworkManager;
import it.polimi.ingsw.gc20.network.common.ClientHandler;
import it.polimi.ingsw.gc20.network.common.Server;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(SocketServer.class.getName());
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private boolean running;
    private static final int DEFAULT_PORT = 8080;

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
                Socket clientSocket = serverSocket.accept();
                SocketClientHandler handler = new SocketClientHandler(clientSocket);
                executor.submit(handler::handleRequest);
                registerClient(handler);
            } catch (IOException e) {
                if (running) {
                    LOGGER.warning("Error while accepting connections: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void stop() {
        running = false;
        for (ClientHandler client : new ArrayList<>(clients)) {
            client.disconnect();
        }
        executor.shutdown();
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
        NetworkManager.getInstance().registerClient(client);
        LOGGER.info("Client registered through socket: " + client.getClientUsername());
    }

    @Override
    public void broadcastMessage(Message message) {
        for (ClientHandler client : new ArrayList<>(clients)) {
            executor.submit(() -> client.sendToClient(message));
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        NetworkManager.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("Client removed from socket server: " + client.getClientUsername());
    }
}