package it.polimi.ingsw.gc20.network;

public class MainServer {
    public static void main(String[] args) {
        NetworkFactory networkFactory = new NetworkFactory();
        networkFactory.initialize();

        // Crea e avvia entrambi i tipi di server
        networkFactory.createServer(NetworkFactory.ServerType.RMI);
        networkFactory.createServer(NetworkFactory.ServerType.SOCKET);
        networkFactory.startAllServers();

        // Aggiungi un shutdown hook per terminare i server correttamente
        Runtime.getRuntime().addShutdownHook(new Thread(networkFactory::stopAllServers));
    }
}