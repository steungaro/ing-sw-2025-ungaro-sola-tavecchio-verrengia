package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.network.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class RMIAuthService implements RMIAuthInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIAuthService.class.getName());
    private final Map<String, String> tokenToUsername = new HashMap<>();
    private final RMIServer server;

    /**
     * Constructor for the RMIAuthService class.
     * @param server The RMIServer instance.
     */
    public RMIAuthService(RMIServer server) {
        this.server = server;
    }

    /**Function to log in a user
     * @param username The username of the user.
     * @return The session token for the user.
     * @throws IllegalArgumentException if the username is invalid or already in use.
     */
    public String login(String username) throws IllegalArgumentException {
        // Check if the username is valid
        if (NetworkManager.getInstance().getClient(username) != null) {
            LOGGER.warning("Username already exists");
            throw new IllegalArgumentException("Username already exists");
        }


        // Generate a session token
        String token = UUID.randomUUID().toString();
        tokenToUsername.put(token, username);
        RMIClientHandler clientHandler = new RMIClientHandler(username);
        server.registerClient(clientHandler);
        LOGGER.info(String.format("Utente Registrato via RMI: " + username));
        return token;
    }

    /**
     * Function to log out a user.
     *
     * @param token The session token of the user.
     * @return
     * @throws RemoteException if the token is invalid.
     */
    public boolean logout(String token) throws RemoteException {
        String username = tokenToUsername.remove(token);
        if (username != null) {
            LOGGER.info(String.format("Utente disconnesso: " + username));
            NetworkManager.getInstance().removeClient(username);
            return true;
        } else {
            LOGGER.warning("Token non valido");
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

