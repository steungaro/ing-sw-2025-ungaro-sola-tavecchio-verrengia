package it.polimi.ingsw.gc20.interfaces;
import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchControllerInterface extends Remote {
    void setMaxLobbies(int maxLobbies) throws RemoteException;
    void setMaxMatches(int maxMatches) throws RemoteException;
    void joinLobby(String id, String user) throws RemoteException;
    void createLobby(String name, String user, int maxPlayers, int level) throws RemoteException;
    void leaveLobby(String userid) throws RemoteException;
    void endGame(String id) throws RemoteException;
    void startLobby(String id) throws RemoteException;
}