package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.network.common.ClientHandler;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class RMIClientHandler implements ClientHandler {
    private final Logger LOGGER = Logger.getLogger(RMIClientHandler.class.getName());
    private final String username;
    private boolean connected;
    private ViewInterface view;

    /**
     * Constructor for the RMIClientHandler class.
     * @param username The username of the client.
     */
    public RMIClientHandler(String username) {
        this.username = username;
        this.connected = true;
        LOGGER.info("ClientHandler created for user: " + username);
    }

    /**
     * Function to set the view interface for the client.
     * @param view The view interface.
     */
    public void setView (ViewInterface view) {
        this.view = view;
        LOGGER.info("View created for user: " + username);
    }

    /**
     * Function to send a message to the client.
     * @param message The message to send.
     */
    @Override
    public void sendMessage(Object message) {
        if (!connected || view == null) return;

        try {
            //call the updateView method of the view interface
            view.updateView(message);
        } catch (RemoteException e) {
            LOGGER.warning("Error while sending the message: " + e.getMessage());
            disconnect();
        }
    }

    /**
     * Function to disconnect the client.
     */
    public void disconnect() {
        if (connected) {
            connected = false;
            if (view != null) {
                try {
                    view.notifyDisconnection();
                } catch (RemoteException e) {
                    LOGGER.warning("Error while disconnecting the client: " + e.getMessage());
                }
            }
        } else return;
        LOGGER.info("Client disconnected: " + username);
    }

    /**
     * Function to get the username of the client.
     * @return The username of the client.
     */
    public String getClientUsername() {
        return username;
    }

    /**
     * Function to check if the client is connected.
     *
     * @return True if the client is connected, false otherwise.
     */
    public Boolean isConnected() {
        return connected;
    }


    @Override
    public void handleRequest() {
        // Not implemented for RMI
    }



}
