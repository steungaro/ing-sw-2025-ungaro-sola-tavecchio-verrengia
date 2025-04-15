package it.polimi.ingsw.gc20.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGameInterface extends Remote {

    void registerView (String token, ViewInterface view) throws RemoteException;

    void sendMessage (String token, Object message) throws RemoteException;

    void joinGame (String token) throws RemoteException;

    void leaveGame (String token) throws RemoteException;

    boolean ping (String token) throws RemoteException;

}
