package it.polimi.ingsw.gc20.network.common;

import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

public interface Server {
    void start();
    void stop();
    void registerClient(ClientHandler client);
    void broadcastMessage(Message message);
}
