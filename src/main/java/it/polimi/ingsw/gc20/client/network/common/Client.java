package it.polimi.ingsw.gc20.client.network.common;

public interface Client {
    void start();

    void stop();

    boolean isConnected();

    boolean login(String username);

    boolean logout(String username);
}
