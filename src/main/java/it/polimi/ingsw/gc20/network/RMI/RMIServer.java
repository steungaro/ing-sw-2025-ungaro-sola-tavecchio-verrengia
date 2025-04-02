package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.exceptions.ServerCriticalError;
import it.polimi.ingsw.gc20.network.NetworkManager;
import it.polimi.ingsw.gc20.network.common.ClientHandler;
import it.polimi.ingsw.gc20.network.common.Server;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class RMIServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(RMIServer.class.getName());
    private static RMIServerHandler rmiServerHandler = null;
    private final List<ClientHandler> clients;
    private final ExecutorService executor;
    private boolean running;
    private static final int DEFAULT_PORT = 1099;

    /**
     * Constructor for the RMIServer class.
     */
    public RMIServer() {
        rmiServerHandler = new RMIServerHandler();
        this.clients = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * Funtion to start the RMI server.
     * //TODO add the port as a parameter
     */
    @Override
    public void start() {
        try {
            // Create the RMI registry
            rmiServerHandler.createRegistry();
            running = true;

            // creation of the gameService
            //RMIGAmeInterface will be the interface that the controller will expose
            RMIGameInterface gameService = new RMIGameService(this);
            // Export the gameService object
            rmiServerHandler.exportObject(gameService, "GameService");

            // creating the authService
            RMIAuthInterface authService = new RMIAuthService(this);
            rmiServerHandler.exportObject(authService, "AuthService");

            LOGGER.info(String.format("RMI Server started at port %d", DEFAULT_PORT));
        } catch (ServerCriticalError e) {
            // Error in the creation of the registry
            LOGGER.severe("Error in the creation of the RMI registry: " + e.getMessage());
        } catch (RemoteException e) {
            // Error in the export of the object
            LOGGER.severe("Error in the export of the RMI object: " + e.getMessage());
        } catch (MalformedURLException e) {
            // Error in the formation of the RMI URL
            LOGGER.severe("Malformed URL during service exportation: " + e.getMessage());
        }
    }

    /**
     * Function to stop the RMI Server, disconnetting all client
     */
    @Override
    public void stop() {
        running = false;
        //Disconnetting all the client, we copy the list clients
        for (ClientHandler client : new ArrayList<>(clients)){
            client.disconnect();
        }
        executor.shutdown();
        LOGGER.info(String.format("RMI Server stopped"));
    }

    /** Function to register a new client in the server and in the network Manager
     *
     * @param client client to register
     */
    public void registerClient (ClientHandler client) {
        clients.add(client);
        NetworkManager.getInstance().registerClient(client);
        LOGGER.info("Client registrato: " + client.getClientUsername());
    }

    /** Function to remove a Client from the server
     *
     * @param client client to remove
     */
    public void removeClient (ClientHandler client) {
        clients.remove(client);
        NetworkManager.getInstance().removeClient(client.getClientUsername());
        LOGGER.info("Client rimosso: " + client.getClientUsername());
    }

    /** Function to broadCast a message
     *
     * @param message messsage to broadcast
     */
    public void broadcastMessage(Object message) {
        //copiamo l'array per evitare modifiche concorrenti da capire se meglio synchronized
        for (ClientHandler client : new ArrayList<>(clients)) {
            executor.submit(() -> client.sendMessage(message));
        }
    }

    /** Getter method for the boolean running
     *
     * @return boolean true if the server is running
     */
    public boolean isRunning() {
        return running;
    }


     /** Getter method for the RMIServerHandler
     *
     * @return RMIServerHandler ServerHandler utilized from the server
     */
    public RMIServerHandler getRMIServerHandler() {
        return rmiServerHandler;
    }

}
