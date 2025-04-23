package it.polimi.ingsw.gc20.network;

public class  MainServer {
    public static void main(String[] args) {
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        // Create servers
        networkFactory.createServer(NetworkFactory.ServerType.RMI);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET);
        networkFactory.startAllServers();

        // Add shutdown hook to stop servers on exit
        Runtime.getRuntime().addShutdownHook(new Thread(networkFactory::stopAllServers));
    }
}