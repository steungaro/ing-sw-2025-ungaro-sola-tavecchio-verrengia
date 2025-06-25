package it.polimi.ingsw.gc20.common.interfaces;

import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.rmi.*;

/**
 * Interface for the view in the RMI architecture.
 * It allows the server to update the view with messages and notify disconnections.
 */
public interface ViewInterface extends Remote {
    /**
     * Updates the view with a given message.
     *
     * @param message the message to update the view with
     * @throws RemoteException if there is an error during the remote method call
     */
    void updateView(Message message) throws RemoteException;

    /**
     * Notifies the view of a disconnection.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void notifyDisconnection() throws RemoteException;
}
