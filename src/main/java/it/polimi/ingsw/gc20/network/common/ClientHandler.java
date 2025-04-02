package it.polimi.ingsw.gc20.network.common;

public interface ClientHandler {

    void sendMessage(Object message);
    void handleRequest();
    void disconnect ();
    String getClientUsername();
}
