package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAuthInterface extends Remote {
    boolean login (String username) throws RemoteException;
    @Deprecated
    boolean logout (String username) throws RemoteException;
    boolean setView(String username) throws RemoteException;
    void pong(String username) throws RemoteException;
}