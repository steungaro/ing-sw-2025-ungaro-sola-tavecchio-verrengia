package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.server.network.NetworkManager;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class RMIAuthService extends UnicastRemoteObject implements RMIAuthInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIAuthService.class.getName());
    private final Map<String, String> tokenToUsername = new HashMap<>();
    private final RMIServer server;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the RMIAuthService class.
     * @param server The RMIServer instance.
     */
    public RMIAuthService(RMIServer server) throws RemoteException {
        this.server = server;
    }

    /**Function to log in a user
     * @param username The username of the user.
     * @return The session token for the user.
     * @throws IllegalArgumentException if the username is invalid or already in use.
     */
    public String login(String username) throws IllegalArgumentException {
        ClientHandler existingClient = NetworkManager.getInstance().getClient(username);

        // Case 1: if is a new username
        if (existingClient==null) {
            // Generate a session token
            String token = UUID.randomUUID().toString();
            tokenToUsername.put(token, username);
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.registerClient(clientHandler);
            LOGGER.info(String.format("User logged via RMI: " + username));
            return token;
        }
        // Case 2: if the username is already in use -- verify if is connected
        else if (!existingClient.isConnected()) {
            // remove old token
            tokenToUsername.entrySet().removeIf(entry -> username.equals(entry.getValue()));
            // Generate a new session token
            String token = UUID.randomUUID().toString();
            tokenToUsername.put(token, username);

            // Update the client handler
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.updateClient(username, clientHandler);
            LOGGER.info(String.format("User reconnected via RMI: " + username));
            return token;
        }
        // Case 3: if the username is already in use and connected
        else {
            LOGGER.warning("Username already taken");
            throw new IllegalArgumentException("Username already taken");
        }
    }

    /**
     * Function to log out a user.
     *
     * @param token The session token of the user.
     * @return True if the user was logged out successfully, false otherwise.
     * @throws RemoteException if the token is invalid.
     */
    public boolean logout(String token) throws RemoteException {
        String username = tokenToUsername.remove(token);
        if (username != null) {
            LOGGER.info(String.format("User disconnected: " + username));
            ClientHandler client = NetworkManager.getInstance().getClient(username);
            if (client != null) {
                client.disconnect();
            }
            return true;
        } else {
            LOGGER.warning("Token not found");
            return false;
        }
    }

    /**
     * Function to get the username associated with a token.
     * @param token The session token of the user.
     * @return The username of the user.
     */
    public String getUsername(String token) {
        return tokenToUsername.get(token);
    }
}

