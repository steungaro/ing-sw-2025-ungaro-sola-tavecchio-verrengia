package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * The {@code RMIClientHandler} class is responsible for managing the communication and state
 * for a specific client in an RMI-based system. This implementation of {@code ClientHandler}
 * handles sending messages to the client and managing client connections.
 * It also interacts with the game and connection controllers to manage disconnection events.
 */
public class RMIClientHandler implements ClientHandler {
    private final Logger LOGGER = Logger.getLogger(RMIClientHandler.class.getName());
    private final String username;
    private boolean connected;
    private ViewInterface view;

    /**
     * Constructor for the RMIClientHandler class that initializes the client handler
     * for a given username and sets the initial status as connected.
     * A corresponding log message is also generated during instantiation.
     *
     * @param username the username of the client associated with this handler
     */
    public RMIClientHandler(String username) {
        this.username = username;
        this.connected = true;
        LOGGER.info("ClientHandler created for user: " + username);
    }

    /**
     * Sets the view associated with this client handler.
     * The view is responsible for handling client-side updates
     * and interactions in the application. Additionally, this method
     * logs the creation of the view for the user.
     *
     * @param view the ViewInterface implementation to be associated
     *             with this client handler
     */
    public void setView (ViewInterface view) {
        this.view = view;
        LOGGER.info("View created for user: " + username);
    }

    @Override
    public void sendToClient(Message message) {
        if (!connected || view == null) return;

        try {
            //call the updateView method of the view interface
            view.updateView(message);
        } catch (ConnectException c) {
            LOGGER.info("Connection refused for user: " + username + ". The client might have disconnected on its own.");
            disconnect();
        } catch (RemoteException e) {
            LOGGER.warning("Error while sending the message: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (connected) {
            if (view != null) {
                try {
                    GameController gameController = MatchController.getInstance().getGameControllerForPlayer(username);
                    if (gameController != null) {
                        gameController.disconnectPlayer(username);
                    } else if (MatchController.getInstance().getPlayersInLobbies().contains(username)) {
                        MatchController.getInstance().leaveLobby(username);
                    }
                    connected = false;
                    view.notifyDisconnection();
                } catch (ConnectException c) {
                    LOGGER.info("Connection refused for user: " + username + ". The client might have disconnected on its own.");
                    connected = false;
                } catch (RemoteException e) {
                    LOGGER.warning("Error while disconnecting the client: " + e.getMessage());
                    connected = false;
                }
            }
        } else return;
        LOGGER.info("Client disconnected: " + username);
    }

    @Override
    public String getClientUsername() {
        return username;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}
