package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public interface ClientHandler {

    void sendToClient(Message message);
    void handleRequests();
    void disconnect ();
    String getClientUsername();
    Boolean isConnected();
}
