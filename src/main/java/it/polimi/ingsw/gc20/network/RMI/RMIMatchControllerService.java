package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.rmi.RemoteException;
import java.util.List;

public class RMIMatchControllerService implements MatchControllerInterface {
    @Override
    public GameController getGameController(String id) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getPlayersInLobbies() throws RemoteException {
        return List.of();
    }

    @Override
    public void setMaxLobbies(int maxLobbies) throws RemoteException {

    }

    @Override
    public void setMaxMatches(int maxMatches) throws RemoteException {

    }

    @Override
    public List<Lobby> getLobbies() throws RemoteException {
        return List.of();
    }

    @Override
    public Lobby getLobby(String id) throws RemoteException {
        return null;
    }

    @Override
    public Lobby joinLobby(String id, String user) throws RemoteException {
        return null;
    }

    @Override
    public Lobby createLobby(String name, String user, int maxPlayers, int level) throws RemoteException {
        return null;
    }

    @Override
    public void leaveLobby(String userid) throws RemoteException {

    }

    @Override
    public void endGame(String id) throws RemoteException {

    }

    @Override
    public void startLobby(String id) throws RemoteException {

    }
}
