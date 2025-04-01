package it.polimi.ingsw.gc20.network;
import it.polimi.ingsw.gc20.network.common.Server;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
    private List<Server> servers = new ArrayList<>();

    public void addServer(Server server) {
        servers.add(server);
    }

    public void startAll() {
        for(Server server : servers) {
            server.start();
        }
    }

    public void stopAll() {
        for(Server server : servers) {
            server.stop();
        }
    }
}
