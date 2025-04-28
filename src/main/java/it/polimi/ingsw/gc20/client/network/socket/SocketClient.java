package it.polimi.ingsw.gc20.client.network.socket;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.server.network.socket.SocketServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClient implements Client {
    private final String serverAddress;
    private final int serverPort;
    private int port;
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean running;

    public SocketClient(String serverAddress, int serverPort) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.port = 0; // Using port 0 lets the system assign an available port
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            port = serverSocket.getLocalPort();
            running = true;
            LOGGER.info("Socket client set up on port: " + serverSocket.getLocalPort());
            // Connect to the server
            connectToServer();
        } catch (Exception e) {
            LOGGER.severe("Error while setting up socket client: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            LOGGER.warning("Error while stopping socket client: " + e.getMessage());
        }
        LOGGER.info("Socket client stopped.");
    }

    @Override
    public void sendMessage(String message) {
        // Implementation for sending a message via socket
    }

    @Override
    public void receiveMessage(String message) {
        // Implementation for receiving a message via socket
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean login(String username) {
        return false;
    }

    @Override
    public boolean logout(String username) {
        return false;
    }


    private void connectToServer() {
        try {
            // Create a socket connection to the server
            socket = new Socket(serverAddress, serverPort);
            LOGGER.info("Connected to server at " + serverAddress + ":" + serverPort);
        } catch (Exception e) {
            LOGGER.severe("Error while connecting to server: " + e.getMessage());
        }
    }
}
