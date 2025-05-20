package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface RMIAuthInterface extends Remote {
    boolean login (String username) throws RemoteException;
    @Deprecated
    boolean logout (String username) throws RemoteException;
    boolean setView(String username, ViewInterface view) throws RemoteException;
    void pong(String username) throws RemoteException;
}