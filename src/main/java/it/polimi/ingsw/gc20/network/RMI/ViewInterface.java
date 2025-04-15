package it.polimi.ingsw.gc20.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends Remote {
    void updateView (Object message) throws RemoteException;
    void notifyDisconnection() throws RemoteException;
}
