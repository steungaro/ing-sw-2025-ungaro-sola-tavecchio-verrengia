package it.polimi.ingsw.gc20.network;

import it.polimi.ingsw.gc20.network.RMI.RMIServer;
import it.polimi.ingsw.gc20.network.common.Server;
import it.polimi.ingsw.gc20.network.socket.SocketServer;

public class MainServer {
    public static void main(String[] args) {
        // Initialize the network factory
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        RMIServer rmiServer = new RMIServer();
        SocketServer socketServer = new SocketServer();

        // Start the server
        networkFactory.initServer(rmiServer);
        networkFactory.initServer(socketServer);

    }
}
