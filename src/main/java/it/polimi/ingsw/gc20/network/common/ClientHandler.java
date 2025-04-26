package it.polimi.ingsw.gc20.network.common;

import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

public interface ClientHandler {

    void sendToClient(Message message);
    void handleRequest();
    void disconnect ();
    String getClientUsername();
    Boolean isConnected();
}
