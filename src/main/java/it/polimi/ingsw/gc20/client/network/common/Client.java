package it.polimi.ingsw.gc20.client.network.common;

public interface Client {
    void start();

    void stop();

    void sendMessage(String message);

    void receiveMessage(String message);

    boolean isConnected();

    boolean login(String username);

    boolean logout(String username);
}
