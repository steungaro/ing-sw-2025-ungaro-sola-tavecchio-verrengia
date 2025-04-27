package it.polimi.ingsw.gc20.server.network;
import it.polimi.ingsw.gc20.server.network.common.ClientHandler;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static NetworkManager instance;
    private final Map<String, ClientHandler> clients = new HashMap<>();

    private NetworkManager() {
        // Private constructor to prevent instantiation
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public void registerClient(ClientHandler client) {
        clients.put(client.getClientUsername(), client);
    }

    public void removeClient(String username) {
        clients.remove(username);
    }

    public ClientHandler getClient(String username) {
        return clients.get(username);
    }

    public void sendToClient(String username, Message message) {
        ClientHandler client = clients.get(username);
        if (client != null) {
            client.sendToClient(message);
        }
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients.values()) {
            client.sendToClient(message);
        }
    }
}
