package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.AuthInterface;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginFailedMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.LoginSuccessfulMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Pong;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * The RMIAuthService class provides implementation for authenticating users
 * via Remote Method Invocation (RMI). It manages user login, logout, and
 * RMI-specific interactions. This class facilitates user authentication
 * and communication management between clients and the server.
 *<p>
 * This class extends UnicastRemoteObject to enable remote method calls and
 * implements AuthInterface, defining the required methods for RMI-based
 * authentication.
 */
public class RMIAuthService extends UnicastRemoteObject implements AuthInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIAuthService.class.getName());
    private final RMIServer server;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of the RMIAuthService class.
     * This constructor initializes the RMI authentication service
     * with a reference to the provided RMIServer instance.
     *
     * @param server The RMIServer instance that the authentication service will use
     *               to manage interactions and connections through RMI.
     * @throws RemoteException If an error occurs during the creation of the RMI service.
     */
    public RMIAuthService(RMIServer server) throws RemoteException {
        this.server = server;
    }

    /**
     * Handles the login process for a client attempting to connect to the server via RMI (Remote Method Invocation).
     * This method manages three scenarios for the client: new user login, reconnection of an existing but disconnected user,
     * and handling of attempts to log in with an already connected username.
     *
     * @param username The username of the client attempting to log in.
     * @param view     The {@code ViewInterface} implementation provided by the client for communication and updates.
     * @return {@code true} if the login process was successful, or {@code false} if login failed (e.g., due to the username
     *         already being in use and connected).
     * @throws RemoteException If a remote invocation error occurs during the login process.
     */
    @Override
    public boolean login(String username, ViewInterface view) throws RemoteException {
        ClientHandler existingClient = NetworkService.getInstance().getClient(username);

        // Case 1: it is a new username
        if (existingClient==null) {
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.registerClient(clientHandler);

            // Set the view for the client
            if (setView(username, view)) {
                LOGGER.warning("Error while setting the view");
                return false;
            }
            LOGGER.info(String.format("User logged via RMI: " + username));

            MatchController.getInstance().getLobbies(username);
            return true;
        }
        // Case 2: the username is already in use -- verify if it is connected
        else if (!existingClient.isConnected()) {
            // Update the client handler
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.updateClient(username, clientHandler);
            // Set the view for the client
            if (setView(username, view)) {
                LOGGER.warning("Error while setting the view");
                return false;
            }
            NetworkService.getInstance().sendToClient(username, new LoginSuccessfulMessage(username));

            GameController gameController = MatchController.getInstance().getGameControllerForPlayer(username);
            if (gameController != null) {
                // Reconnect the player to the game
                gameController.reconnectPlayer(username);
            } else {
                // If the player is not in a game, get the lobbies
                MatchController.getInstance().getLobbies(username);
            }
            LOGGER.info(String.format("User reconnected via RMI: " + username));
            return true;
        }
        // Case 3: if the username is already in use and connected
        else {
            // Notify the client that the username is already taken through a direct message (not going through the network service)
            view.updateView(new LoginFailedMessage(username));
            LOGGER.warning("Username already taken");
            return false;
        }
    }

    /**
     * Handles the logout process for a client identified by their username.
     * This method attempts to disconnect the client if they are found in the system.
     * If the client is successfully disconnected, it returns true; otherwise, false.
     *
     * @param username the unique identifier of the client attempting to log out
     * @return {@code true} if the logout was successful, or {@code false} if the client was not found
     * @throws RemoteException if a remote invocation error occurs during the logout process
     * @deprecated This method is not required for this project, please do not use it.
     */
    @Override
    @Deprecated
    public boolean logout(String username) throws RemoteException {
        LOGGER.info(String.format("User disconnected: " + username));
        ClientHandler client = NetworkService.getInstance().getClient(username);
        if (client != null) {
            client.disconnect();
            return true;
        } else {
            LOGGER.warning("User not found");
            return false;
        }
    }

    /**
     * Sets the view interface for a specified user in the system.
     * This method retrieves the corresponding client handler for the given username,
     * assigns the provided view interface to it, and performs additional post-setup operations
     * such as sending login success notifications and fetching lobby information.
     *
     * @param username The username of the client whose view is being set.
     * @param view     The {@code ViewInterface} implementation to be set for the client.
     * @return {@code false} if the operation completes successfully, or {@code true} if an error occurs during the process.
     * @throws RemoteException If a remote invocation error occurs during the operation.
     */
    private boolean setView(String username, ViewInterface view) throws RemoteException {
        try {
            RMIClientHandler clientHandler = (RMIClientHandler) NetworkService.getInstance().getClient(username);
            clientHandler.setView(view);
            LOGGER.info("View set for user: " + username);
            MatchController.getInstance().getLobbies(username);
            NetworkService.getInstance().sendToClient(username, new LoginSuccessfulMessage(username));
            return false;
        } catch (Exception e) {
            LOGGER.severe("Error while setting the view: " + e.getMessage());
            return true;
        }
    }

    /**
     * Handles the "pong" response from a client to indicate activity.
     * This method enqueues a {@code Pong} message for processing
     * and logs that a pong signal has been received from the specified client.
     *
     * @param username The username of the client sending the pong.
     *                 Must not be {@code null}.
     * @throws RemoteException If a remote invocation error occurs during execution.
     */
    @Override
    public void pong(String username) throws RemoteException {
        QueueHandler.getInstance().enqueue(new Pong(username));
        LOGGER.info("Pong received from client");
    }
}

