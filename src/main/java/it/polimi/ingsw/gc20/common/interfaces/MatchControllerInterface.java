package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatchControllerInterface extends Remote {
    void joinLobby(String id, String user) throws RemoteException;
    void createLobby(String name, String user, int maxPlayers, int level) throws RemoteException;
    void leaveLobby(String userid) throws RemoteException;
    void startLobby(String id) throws RemoteException;
}