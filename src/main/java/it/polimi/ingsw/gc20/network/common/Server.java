package it.polimi.ingsw.gc20.network.common;

public interface Server {
    void start();
    void stop();
    void registerClient(ClientHandler client);
    void broadcastMessage(Object message);
}
