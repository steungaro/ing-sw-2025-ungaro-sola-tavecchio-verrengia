package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public interface Server {
    void start();
    void stop();
    void registerClient(ClientHandler client);
    void updateClient(String username, ClientHandler client);
    void removeClient(ClientHandler client);
}
