package it.polimi.ingsw.gc20.client.network.RMI;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.network.RMI.RMIAuthInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RMIClient implements Client {
    private final String serverAddress;
    private final int port;
    private ViewInterface view;

    private RMIAuthInterface authService;
    private GameControllerInterface gameService;
    private MatchControllerInterface matchService;

    private Registry registry;

    private boolean connected = false;

    private final Logger LOGGER = Logger.getLogger(RMIClient.class.getName());

    public RMIClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    @Override
    public void start() {
        try {
            // Look up the registry
            registry = LocateRegistry.getRegistry(serverAddress, port);

            // Look up the remote services
            authService = (RMIAuthInterface) registry.lookup("AuthService");
            gameService = (GameControllerInterface) registry.lookup("GameService");
            matchService = (MatchControllerInterface) registry.lookup("MatchService");

            connected = true;
            LOGGER.info("Connected to RMI server at " + serverAddress + ":" + port);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.severe("Error connecting to RMI server: " + e.getMessage());
            connected = false;
        }
    }

    @Override
    public boolean login(String username) {
        if (!connected) return false;

        try {
            boolean result = authService.login(username);
            if (result) {
                LOGGER.info("Successfully logged in as: " + username);
            }
            return result;
        } catch (RemoteException e) {
            LOGGER.warning("Error during login: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            if (registry != null) {
                registry.unbind("AuthService");
                registry.unbind("GameService");
                registry.unbind("MatchService");
            }
            connected = false;
            LOGGER.info("Disconnected from RMI server.");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.warning("Error while stopping RMI client: " + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean logout(String username) {
        // Logout logic not required for this project
        return false;
    }
}
