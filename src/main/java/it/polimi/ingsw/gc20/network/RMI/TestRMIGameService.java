package it.polimi.ingsw.gc20.network.RMI;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestRMIGameService implements RMIGameInterface {
    private final RMIAuthInterface authService;

    private final Map<String, ViewInterface> tokenToView = new HashMap<>();


    private final Logger LOGGER = Logger.getLogger(TestRMIGameService.class.getName());
    public TestRMIGameService(RMIAuthInterface authService) {
        this.authService = authService;
    }

    @Override
    public void registerView(String token, ViewInterface view) throws RemoteException {
        // Simulate registering a view
        String username = authService.getUsername(token);
        if (username == null) {
            throw new RemoteException("Invalid token");
        }
        tokenToView.put(token, view);
        LOGGER.info("View registered for user: " + username);
        System.out.println("View registered: " + view);
    }

    @Override
    public void sendMessage (String token, Object message) throws RemoteException {
        String username = authService.getUsername(token);
        if (username == null) {
            throw new RemoteException("Invalid token");
        }

        LOGGER.info(username + " sent message: " + message);

        //echo of the message to all clients
        for (Map.Entry<String, ViewInterface> entry : tokenToView.entrySet()) {
            try {
                entry.getValue().updateView(message);
            } catch (RemoteException e) {
                LOGGER.warning("Failed to send message to client: " + entry.getKey());
            }
        }
    }

    @Override
    public void joinGame (String token) throws RemoteException {
        String username = authService.getUsername(token);
        if (username == null) {
            throw new RemoteException("Invalid token");
        }

        LOGGER.info(username + " joined the game");

        // Notify all clients that a new player has joined
        for (Map.Entry<String, ViewInterface> entry : tokenToView.entrySet()) {
            try {
                entry.getValue().updateView(username + " has joined the game");
            } catch (RemoteException e) {
                LOGGER.warning("Failed to notify client: " + entry.getKey());
            }
        }
    }

    @Override
    public void leaveGame (String token) throws RemoteException {
        String username = authService.getUsername(token);
        if (username == null) {
            throw new RemoteException("Invalid token");
        }

        LOGGER.info(username + " left the game");

        // Notify all clients that a player has left
        for (Map.Entry<String, ViewInterface> entry : tokenToView.entrySet()) {
            try {
                entry.getValue().updateView(username + " has left the game");
            } catch (RemoteException e) {
                LOGGER.warning("Failed to notify client: " + entry.getKey());
            }
        }
    }

    @Override
    public boolean ping(String token) throws RemoteException {
        String username = authService.getUsername(token);
        if (username == null) {
            return false;
        }

        LOGGER.info(username + " pinged the server");

        // Simulate a ping response
        return true;
    }
}
