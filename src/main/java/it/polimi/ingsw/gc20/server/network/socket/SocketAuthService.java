package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginFailedMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginSuccessfulMessage;
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
     */
    public void handleNewClient(Socket clientSocket) {
        SocketClientHandler newClient = null;
        LoginRequest loginRequest;
        try {
            // Create input and output streams
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());


            do {
                // First message is the login request
                loginRequest = (LoginRequest) in.readObject();

                // Check if the username is associated with an existing client
                ClientHandler existingClient = NetworkService.getInstance().getClient(loginRequest.username());

                // If the client is not already registered
                if (existingClient == null) {
                    // Create a new client handler
                    newClient = new SocketClientHandler(loginRequest.username(), clientSocket);
                    socketServer.registerClient(newClient);
                    LOGGER.info("Client " + loginRequest + " connected.");

                // If the client is already registered check if it is connected (reconnection)
                } else {
                    // If the client is connected, refuse this connection and wait for a new username
                    if (existingClient.isConnected()) {
                        LOGGER.warning("Client " + loginRequest + " is already connected.");
                        out.writeObject(new LoginFailedMessage(loginRequest.username()));
                        out.flush();
                        // newClient is still null, so the loop continues
                    } else {
                        // Reconnect the client (maybe it was RMI before or connection crashed)
                        newClient = new SocketClientHandler(loginRequest.username(), clientSocket);
                        socketServer.updateClient(loginRequest.username(), newClient);
                        LOGGER.info("Client " + loginRequest + " reconnected.");
                    }
                }
            } while (newClient == null);

            // Notify the client that the reconnection was successful
            out.writeObject(new LoginSuccessfulMessage(loginRequest.username()));
            out.flush();

            // Start the handler thread
            newClient.handleRequests();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error while accepting connections: " + e.getMessage());
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
