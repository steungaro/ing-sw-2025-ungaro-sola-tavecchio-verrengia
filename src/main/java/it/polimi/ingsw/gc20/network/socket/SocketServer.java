package it.polimi.ingsw.gc20.network.socket;

import it.polimi.ingsw.gc20.network.NetworkManager;
import it.polimi.ingsw.gc20.network.common.ClientHandler;
import it.polimi.ingsw.gc20.network.common.Server;

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
            LOGGER.info("Socket server avviato sulla porta " + DEFAULT_PORT);

            // Thread per accettare nuove connessioni
            new Thread(this::acceptConnections).start();
        } catch (IOException e) {
            LOGGER.severe("Errore nell'avvio del server Socket: " + e.getMessage());
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
                    LOGGER.warning("Errore nell'accettare connessioni: " + e.getMessage());
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
            LOGGER.info("Socket server fermato");
        } catch (IOException e) {
            LOGGER.warning("Errore nella chiusura del server Socket: " + e.getMessage());
        }
    }

    @Override
    public void registerClient(ClientHandler client) {
        clients.add(client);
        NetworkManager.getInstance().registerClient(client);
        LOGGER.info("Client registrato: " + client.getClientUsername());
    }

    @Override
    public void broadcastMessage(Object message) {
        for (ClientHandler client : new ArrayList<>(clients)) {
            executor.submit(() -> client.sendMessage(message));
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        NetworkManager.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("Client rimosso: " + client.getClientUsername());
    }
}