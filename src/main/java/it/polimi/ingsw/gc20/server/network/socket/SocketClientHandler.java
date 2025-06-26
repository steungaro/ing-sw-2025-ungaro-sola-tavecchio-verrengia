package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClientHandler implements ClientHandler {
    private static final Logger LOGGER = Logger.getLogger(SocketClientHandler.class.getName());
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final String username;
    private boolean connected = true;

    public SocketClientHandler(String username, Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
        this.clientSocket = clientSocket;
        this.username = username;
        this.in = in;
        this.out = out;
        LOGGER.info("ClientHandler created for user: " + username);
    }

    /**
     * Function to handle incoming requests from the client.
     * This method runs in a separate thread looping to handle incoming messages.
     */
    public void handleRequests() {
        try {
            // Loop to handle incoming messages
            while (connected) {
                Message message = (Message) in.readObject();
                processMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.info("Error while handling request, user disconnected: " + e.getMessage());
            disconnect();
        }
    }

    /**
     * Function to process the incoming message.
     * @param message The message to process.
     */
    private void processMessage(Message message) {
        QueueHandler.getInstance().enqueue(message);
    }

    /**
     * Function to get the status of the connection.
     * @return True if the client is connected, false otherwise.
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Function to send a message to the client.
     * @param message The message to send.
     */
    @Override
    public void sendToClient(Message message) {
        if (!connected) return;

        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while sending message to client: " + e.getMessage());
            disconnect();
        }
    }

    /**
     * Function to disconnect the client, closing the socket and streams.
     * This method should be called when the client disconnects (voluntary or involuntary).
     */
    @Override
    public void disconnect() {
        if (!connected) return;

        connected = false;
        try {
            if (in != null) in.close();
            GameController gameController = MatchController.getInstance().getGameControllerForPlayer(username);
            if (gameController != null) {
                gameController.disconnectPlayer(username);
            } else {
                MatchController.getInstance().leaveLobby(username);
            }
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
        } catch (IOException e) {
            LOGGER.warning("Error on disconnection: " + e.getMessage());
        }
    }

    /**
     * Function to get the username of the client.
     * @return The username of the client.
     */
    @Override
    public String getClientUsername() {
        return username;
    }
}