package it.polimi.ingsw.gc20.server.network.socket;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginFailedMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginSuccessfulMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby.LoginRequest;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * The SocketAuthService class is responsible for handling client authentication
 * and connection management in a socket-based server environment. It manages
 * new client connections, checks for existing clients, and handles reconnections.
 * It also provides methods to log out users.
 */
public class SocketAuthService {
    private final SocketServer socketServer;
    private final Logger LOGGER = Logger.getLogger(SocketAuthService.class.getName());

    /**
     * Constructor for the SocketAuthService class.
     * Initializes the service with a reference to the SocketServer.
     *
     * @param socketServer The SocketServer instance that this service will use
     *                     to manage client connections and registrations.
     */
    public SocketAuthService(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    /**
     * Handles a new client connection by processing the login request.
     * It checks if the client is already connected, creates a new client handler,
     * or reconnects an existing client if necessary.
     *
     * @param clientSocket The socket connection for the new client.
     */
    public void handleNewClient(Socket clientSocket) {
        SocketClientHandler newClient = null;
        LoginRequest loginRequest;
        try {
            // Create input and output streams
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            do {
                // The first message is the login request
                loginRequest = (LoginRequest) in.readObject();

                // Check if the username is associated with an existing client
                ClientHandler existingClient = NetworkService.getInstance().getClient(loginRequest.username());

                // If the client is not already registered
                if (existingClient == null) {
                    // Create a new client handler
                    newClient = new SocketClientHandler(loginRequest.username(), clientSocket, in, out);
                    socketServer.registerClient(newClient);
                    LOGGER.info("Client " + loginRequest + " connected.");

                    // Notify the client that the connection was successful
                    out.writeObject(new LoginSuccessfulMessage(loginRequest.username()));
                    out.flush();

                    MatchController.getInstance().getLobbies(loginRequest.username());
                // If the client is already registered, check if it is connected (reconnection)
                } else {
                    // If the client is connected, refuse this connection and wait for a new username
                    if (existingClient.isConnected()) {
                        LOGGER.warning("Client " + loginRequest + " is already connected.");
                        out.writeObject(new LoginFailedMessage(loginRequest.username()));
                        out.flush();
                        // the newClient is still null, so the loop continues
                    } else {
                        // Reconnect the client (maybe it was RMI before, or connection crashed)
                        newClient = new SocketClientHandler(loginRequest.username(), clientSocket, in, out);
                        socketServer.updateClient(loginRequest.username(), newClient);

                        // Notify the client that the reconnection was successful
                        out.writeObject(new LoginSuccessfulMessage(loginRequest.username()));
                        out.flush();

                        GameController gameController = MatchController.getInstance().getGameControllerForPlayer(loginRequest.username());
                        if (gameController != null) {
                            gameController.reconnectPlayer(loginRequest.username());
                        } else {
                            MatchController.getInstance().getLobbies(loginRequest.username());
                        }
                        LOGGER.info("Client " + loginRequest + " reconnected.");
                    }
                }
            } while (newClient == null);


            // Start the handler thread
            newClient.handleRequests();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Error while accepting connections: " + e.getMessage());
        }
    }

    /**
     * Logs out a user by disconnecting the associated client handler.
     * This method is deprecated and should be replaced with a more robust logout mechanism.
     *
     * @param username The username of the client to log out.
     * @return true if the logout was successful, false if the user was not found.
     * @deprecated This method is deprecated and should not be used in new code.
     */
    @Deprecated
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
