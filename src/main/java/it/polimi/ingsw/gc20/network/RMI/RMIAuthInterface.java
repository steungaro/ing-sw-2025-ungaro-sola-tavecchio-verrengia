package it.polimi.ingsw.gc20.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAuthInterface extends Remote {
    String login (String username) throws RemoteException, IllegalArgumentException;
    boolean logout(String sessionToken) throws RemoteException;
    String getUsername(String sessionToken) throws RemoteException;
}
