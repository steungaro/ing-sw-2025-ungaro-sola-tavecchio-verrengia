package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;

public class  MainServer {
    public static void main(String[] args) {
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        MatchController.getInstance(10, 10);
        // Create servers
        networkFactory.createServer(NetworkFactory.ServerType.RMI);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET);
        networkFactory.startAllServers();

        // Add shutdown hook to stop servers on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkFactory.stopAllServers();
            HeartbeatService.getInstance().stop();
            System.out.println("Servers and heartbeat service stopped.");
        }));
    }
}