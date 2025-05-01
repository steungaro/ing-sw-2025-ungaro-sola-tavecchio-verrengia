package it.polimi.ingsw.gc20.client.network.socket;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketClient implements Client {
    private final String serverAddress;
    private final int serverPort;
    private int port;
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean running;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ObjectInputStream in;
    private ObjectOutputStream out;

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

    public void sendMessage(Message message) {
        // Implementation for sending messages to the server
        try {
            if (out != null) {
                out.writeObject(message);
                out.flush();
            } else {
                LOGGER.warning("Output stream is null, cannot send message.");
            }
        } catch (IOException e) {
            LOGGER.warning("Error while sending message: " + e.getMessage());
            disconnect();
        }
    }

    public void receiveMessages() {
        // Implementation for receiving messages from the server
        while (running) {
            try {
                // Check if the socket is closed
                if (socket == null || socket.isClosed()) {
                    LOGGER.warning("Socket is closed, stopping message reception.");
                    break;
                }
                // Read messages from the socket
                Message message = (Message) in.readObject();

                message.handleMessage();
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.warning("Error while receiving messages: " + e.getMessage());
                disconnect();
            }
        }
    }

    @Override
    public boolean isConnected() {
        //TODO
        return false;
    }

    @Override
    public boolean login(String username) {
        //TODO
        return false;
    }

    @Override
    public boolean logout(String username) {
        //TODO
        return false;
    }


    private void connectToServer() {
        try {
            // Create a socket connection to the server
            socket = new Socket(serverAddress, serverPort);
            LOGGER.info("Connected to server at " + serverAddress + ":" + serverPort);
            // Start a thread to handle incoming messages
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            executor.submit(this::receiveMessages);
        } catch (Exception e) {
            LOGGER.severe("Error while connecting to server: " + e.getMessage());
        }
    }

    private void disconnect() {
        try {
            if (!running) return;
            running = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            LOGGER.warning("Error while disconnecting: " + e.getMessage());
        }
    }
}
