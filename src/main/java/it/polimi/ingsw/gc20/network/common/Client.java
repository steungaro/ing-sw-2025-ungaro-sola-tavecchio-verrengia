package it.polimi.ingsw.gc20.network.common;

public interface Client {
    void connect(String host, int port);
    void disconnect();
}
