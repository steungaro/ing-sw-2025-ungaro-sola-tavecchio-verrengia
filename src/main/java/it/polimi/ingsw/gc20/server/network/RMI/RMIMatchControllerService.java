package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby.*;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * This class provides the implementation of the {@code MatchControllerInterface}
 * for handling RMI (Remote Method Invocation) requests related to lobby management
 * in a multiplayer application. It extends {@code UnicastRemoteObject} to allow remote clients
 * to interact with the service.
 * <p>
 * The {@code RMIMatchControllerService} acts as a bridge between remote clients
 * and the internal message handling system. All RMI requests are encapsulated
 * as messages and forwarded to the {@code QueueHandler} for processing.
 */
public class RMIMatchControllerService extends UnicastRemoteObject implements MatchControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIMatchControllerService.class.getName());
    private final QueueHandler queueHandler;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code RMIMatchControllerService} instance, initializing the service
     * for handling RMI requests related to lobby management for a multiplayer application.
     * <p>
     * This constructor invokes the superclass constructor of {@code UnicastRemoteObject}
     * to enable remote communication for the service. It also retrieves the singleton
     * instance of the {@code QueueHandler} to manage message processing.
     *
     * @throws RemoteException if a communication-related error occurs during the RMI setup.
     */
    public RMIMatchControllerService() throws RemoteException {
        super();
        this.queueHandler = QueueHandler.getInstance();
    }

    @Override
    public void joinLobby(String id, String user) throws RemoteException {
        LOGGER.fine("Received RMI call: joinLobby from " + user);
        queueHandler.enqueue(new JoinLobbyMessage(user, id));
    }

    @Override
    public void createLobby(String name, String user, int maxPlayers, int level) throws RemoteException {
        LOGGER.fine("Received RMI call: createLobby from " + user);
        queueHandler.enqueue(new CreateLobbyMessage(name, user, maxPlayers, level));
    }

    @Override
    public void leaveLobby(String userid) throws RemoteException {
        LOGGER.fine("Received RMI call: leaveLobby from " + userid);
        queueHandler.enqueue(new LeaveLobbyMessage(userid));
    }

    @Override
    public void startLobby(String userid) throws RemoteException {
        LOGGER.fine("Received RMI call: startLobby from " + userid);
        queueHandler.enqueue(new StartLobbyMessage(userid));
    }

    @Override
    public void killLobby(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: killLobby from " + username);
        queueHandler.enqueue(new KillLobbyMessage(username));
    }

    @Override
    public void getLobbies(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: requestLobbyList from " + username);
        queueHandler.enqueue(new LobbyListRequest(username));
    }
}
