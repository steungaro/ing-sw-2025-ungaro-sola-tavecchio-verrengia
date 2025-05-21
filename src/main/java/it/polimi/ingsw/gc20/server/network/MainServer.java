package it.polimi.ingsw.gc20.server.network;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.network.common.HeartbeatService;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;

public class  MainServer {
    public static void main(String[] args) {
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        MatchController.getInstance(10, 10);
        // Create servers
        networkFactory.createServer(NetworkFactory.ServerType.RMI);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET);
        networkFactory.startAllServers();

        // Start the heartbeat service
        HeartbeatService.getInstance().start();

        // Start the queue handler
        QueueHandler.getInstance();

        // Add shutdown hook to stop servers on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkFactory.stopAllServers();
            HeartbeatService.getInstance().stop();
            QueueHandler.getInstance().shutdown();
            System.out.println("Servers, queue handler and heartbeat service stopped.");
        }));
    }
}