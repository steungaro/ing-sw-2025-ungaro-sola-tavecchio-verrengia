package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClientHandler implements ClientHandler {
    private static final Logger LOGGER = Logger.getLogger(SocketClientHandler.class.getName());
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;
    private boolean connected = true;

    public SocketClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            // Creating input and output streams (output first to avoid deadlock)
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            LOGGER.severe("Error while initializing streams: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void handleRequest() {
        try {
            // First message is the login request
            Object loginRequest = in.readObject();
            // Authentication logic here (e.g., check username, password, etc.)
            // TODO

            // Loop to handle incoming messages
            while (connected) {
                Message message = (Message) in.readObject();
                processMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error while handling request: " + e.getMessage());
            disconnect();
        }
    }

    private void processMessage(Message message) {
        QueueHandler.getInstance().enqueue(message);
    }

    @Override
    public Boolean isConnected() {
        // TODO
        return false;
    }

    @Override
    public void sendToClient(Message message) {
        if (!connected) return;

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while sending message to client: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (!connected) return;

        connected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
        } catch (IOException e) {
            LOGGER.warning("Error on disconnection: " + e.getMessage());
        }
    }

    @Override
    public String getClientUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //TODO heartbeat mechanism?
}