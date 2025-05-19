package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.RMIAuthInterface;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Pong;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class RMIAuthService extends UnicastRemoteObject implements RMIAuthInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIAuthService.class.getName());
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
     * @return True if the user was logged in successfully, false otherwise.
     */
    @Override
    public boolean login(String username) {
        ClientHandler existingClient = NetworkService.getInstance().getClient(username);

        // Case 1: it is a new username
        if (existingClient==null) {
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.registerClient(clientHandler);
            LOGGER.info(String.format("User logged via RMI: " + username));
            MatchController.getInstance().getLobbies(username);

            return true;
        }
        // Case 2: the username is already in use -- verify if it is connected
        else if (!existingClient.isConnected()) {
            // Update the client handler
            RMIClientHandler clientHandler = new RMIClientHandler(username);
            server.updateClient(username, clientHandler);
            MatchController.getInstance().getGameControllerForPlayer(username).reconnectPlayer(username);
            LOGGER.info(String.format("User reconnected via RMI: " + username));
            return true;
        }
        // Case 3: if the username is already in use and connected
        else {
            LOGGER.warning("Username already taken");
            return false;
        }
    }

    /**
     * Function to log out a user.
     *
     * @param username The username of the user.
     * @return True if the user was logged out successfully, false otherwise.
     * @throws RemoteException if the token is invalid.
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

    @Override
    public boolean setView(String username) throws RemoteException {
        try {
            RMIClientHandler clientHandler = (RMIClientHandler) NetworkService.getInstance().getClient(username);
            clientHandler.setView((ViewInterface) RMIServerHandler.getInstance().locateObject("View " + username));
            LOGGER.info("View set for user: " + username);
            return true;
        } catch (RemoteException e) {
            LOGGER.severe("Error while setting the view: " + e.getMessage());
            return false;
        }
    }

    public void pong(String username) throws RemoteException {
        QueueHandler.getInstance().enqueue(new Pong(username));
        LOGGER.info("Pong received from client");
    }
}

