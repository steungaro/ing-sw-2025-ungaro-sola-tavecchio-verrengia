package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby.*;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class RMIMatchControllerService extends UnicastRemoteObject implements MatchControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIMatchControllerService.class.getName());
    private final QueueHandler queueHandler;
    @Serial
    private static final long serialVersionUID = 1L;

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
