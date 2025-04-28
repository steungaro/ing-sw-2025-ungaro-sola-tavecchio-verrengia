package it.polimi.ingsw.gc20.server.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAuthInterface extends Remote {
    boolean login (String username) throws RemoteException;
    boolean logout (String sessionToken) throws RemoteException;
}