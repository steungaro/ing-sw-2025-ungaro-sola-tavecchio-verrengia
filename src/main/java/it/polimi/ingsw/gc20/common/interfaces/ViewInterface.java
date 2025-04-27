package it.polimi.ingsw.gc20.common.interfaces;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.rmi.*;

public interface ViewInterface extends Remote {
    void updateView(Message message) throws RemoteException;

    void showError(Message errorMessage) throws RemoteException;

    void showInfo(Message infoMessage) throws RemoteException;

    void showWarning(Message warningMessage) throws RemoteException;

    void showSuccess(Message successMessage) throws RemoteException;

    void close() throws RemoteException;

    void notifyDisconnection() throws RemoteException;
}
