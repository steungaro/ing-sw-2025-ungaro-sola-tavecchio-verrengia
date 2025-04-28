package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby.LoginRequest;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketAuthService {
    private final SocketServer socketServer;
    private final Logger LOGGER = Logger.getLogger(SocketAuthService.class.getName());

    /**
     * Constructor for the SocketAuthService class.
     * @param socketServer The socket server instance.
     */
    public SocketAuthService(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    /**
     * Function to handle a new client connection.
     * @param clientSocket The socket of the client.
     * @return The client handler for the new client or null if the client is not accepted (already connected).
     */
    public ClientHandler handleNewClient(Socket clientSocket) {
        try {
            // Create input and output streams
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // First message is the login request
            LoginRequest loginRequest = (LoginRequest) in.readObject();

            // Authentication logic (message contains only the username)
            ClientHandler client = NetworkService.getInstance().getClient(loginRequest.username());

            if (client == null) { // If the client is not already registered
                // Create a new client handler
                SocketClientHandler newClient = new SocketClientHandler(loginRequest.username(), clientSocket);
                socketServer.registerClient(newClient);
                LOGGER.info("Client " + loginRequest + " connected.");
                return newClient;
            } else { // If the client is already registered
                if (client.isConnected()) {
                    LOGGER.warning("Client " + loginRequest + " is already connected.");
                    out.writeObject("Username already in use");
                    out.flush();
                    return null;
                } else {
                    // Reconnect the client (maybe it was RMI)
                    SocketClientHandler newClient = new SocketClientHandler(loginRequest.username(), clientSocket);
                    socketServer.updateClient(loginRequest.username(), newClient);
                    LOGGER.info("Client " + loginRequest + " reconnected.");
                    return newClient;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error while accepting connections: " + e.getMessage());
            return null;
        }
    }

    /**
     * Function to log out a user (should be called when the client disconnects voluntarily).
     * @param username The username of the user.
     * @return True if the user was logged out successfully, false otherwise.
     */
    public boolean logout(String username) {
        ClientHandler client = NetworkService.getInstance().getClient(username);
        if (client != null) {
            client.disconnect();
            LOGGER.info("Client " + username + " disconnected.");
            return true;
        } else {
            LOGGER.warning("User not found");
            return false;
        }
    }
}
