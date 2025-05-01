package it.polimi.ingsw.gc20.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAuthInterface extends Remote {
    boolean login (String username) throws RemoteException;
    boolean logout (String sessionToken) throws RemoteException;
}