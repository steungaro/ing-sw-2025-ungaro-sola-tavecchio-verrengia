package it.polimi.ingsw.gc20.common.interfaces;

import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.rmi.*;

public interface ViewInterface extends Remote {
    void updateView(Message message) throws RemoteException;
    void notifyDisconnection() throws RemoteException;
}
