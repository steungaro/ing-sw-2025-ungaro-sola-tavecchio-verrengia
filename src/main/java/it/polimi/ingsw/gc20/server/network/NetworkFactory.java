package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.network.RMI.RMIServer;
import it.polimi.ingsw.gc20.server.network.common.Server;
import it.polimi.ingsw.gc20.server.network.socket.SocketServer;

import java.util.ArrayList;
import java.util.List;

public class NetworkFactory {
    private final List<Server> servers = new ArrayList<>();

    public void initialize() {
        NetworkService.getInstance(); // Ensure singleton is created
    }

    public void createServer(ServerType type) {
        Server server = switch (type) {
            case RMI -> new RMIServer();
            case SOCKET -> new SocketServer();
        };
        servers.add(server);
    }

    public void startAllServers() {
        for (Server server : servers) {
            server.start();
        }
    }

    public void stopAllServers() {
        for (Server server : servers) {
            server.stop();
        }
    }

    public enum ServerType {
        RMI, SOCKET
    }
}