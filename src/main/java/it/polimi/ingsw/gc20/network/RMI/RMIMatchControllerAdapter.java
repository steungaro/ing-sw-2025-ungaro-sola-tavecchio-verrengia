package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.rmi.RemoteException;
import java.util.List;

public class RMIMatchControllerAdapter implements RMIMatchControllerInterface {
    private final MatchController controller;

    public RMIMatchControllerAdapter(MatchController controller) {
        this.controller = controller;
    }

    @Override
    public GameController getGameController(String id) throws RemoteException {
        return controller.getGameController(id);
    }

    @Override
    public List<String> getPlayersInLobbies() throws RemoteException {
        return controller.getPlayersInLobbies();
    }

    @Override
    public void setMaxLobbies(int maxLobbies) throws RemoteException {
        this.controller.setMaxLobbies(maxLobbies);
    }

    @Override
    public void setMaxMatches(int maxMatches) throws RemoteException {
        this.controller.setMaxMatches(maxMatches);
    }

    @Override
    public List<Lobby> getLobbies() throws RemoteException {
        return this.controller.getLobbies();
    }

    @Override
    public Lobby getLobby(String id) throws RemoteException {
        return this.controller.getLobby(id);
    }

    @Override
    public Lobby joinLobby(String id, String user) throws RemoteException {
        return this.controller.joinLobby(id, user);
    }

    @Override
    public Lobby createLobby(String name, String user, int maxPlayers, int level) throws RemoteException {
        return this.controller.createLobby(name, user, maxPlayers, level);
    }

    @Override
    public void leaveLobby(String userid) throws RemoteException {
        this.controller.leaveLobby(userid);
    }

    @Override
    public void endGame(String id) throws RemoteException {
        this.controller.endGame(id);
    }

    @Override
    public void startLobby(String id) throws RemoteException {
        this.controller.startLobby(id);
    }

}
