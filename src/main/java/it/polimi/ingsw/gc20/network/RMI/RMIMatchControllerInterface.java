package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIMatchControllerInterface extends Remote {
        GameController getGameController(String id) throws RemoteException;
        List<String> getPlayersInLobbies() throws RemoteException;
        void setMaxLobbies(int maxLobbies) throws RemoteException;
        void setMaxMatches(int maxMatches)throws RemoteException;
        List<Lobby> getLobbies() throws RemoteException;
        Lobby getLobby(String id) throws RemoteException;
        Lobby joinLobby(String id, String user) throws RemoteException;
        Lobby createLobby(String name, String user, int maxPlayers, int level) throws RemoteException;
        void leaveLobby(String userid) throws RemoteException;
        void endGame(String id) throws RemoteException;
        void startLobby(String id) throws RemoteException;
}
