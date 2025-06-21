package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.server.exceptions.ServerCriticalError;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The RMIServerHandler class manages the operations related to the RMI (Remote Method Invocation) server,
 * including the creation of an RMI registry, exporting remote objects, and maintaining the server port.
 * This class follows the Singleton design pattern to ensure a single instance across the application.
 */
public class RMIServerHandler {
    private Registry registry;
    private boolean registryCreated = false;
    private int currentPort;

    private static RMIServerHandler instance;

    /**
     * Private constructor for the RMIServerHandler class.
     * This constructor is used to prevent direct instantiation of the class,
     * enforcing the Singleton design pattern.
     * Access to the single instance of this class is provided through the
     * getInstance() method.
     */
    private RMIServerHandler() {
    }

    /**
     * Returns the single instance of the RMIServerHandler class, ensuring that only one instance
     * of this class is created throughout the application. This method implements the Singleton
     * design pattern to provide a global point of access to the RMIServerHandler instance.
     *
     * @return the single instance of the RMIServerHandler class
     */
    public static RMIServerHandler getInstance() {
        if (instance == null) {
            instance = new RMIServerHandler();
        }
        return instance;
    }

    /**
     * Creates an RMI (Remote Method Invocation) registry on the specified port.
     * This method initializes the RMI registry, making it possible to bind remote
     * objects for client access. If the registry creation fails, a
     * {@link ServerCriticalError} is thrown to indicate a critical server issue.
     *
     * @param port the port number on which the RMI registry will be created
     * @throws ServerCriticalError if the registry cannot be created on the given port
     */
    public void createRegistry(int port) throws ServerCriticalError {
        try {
            registry = LocateRegistry.createRegistry (port);
            registryCreated = true;
            currentPort = port;
        } catch (RemoteException e) {
            throw new ServerCriticalError("Unable to create RMI registry on port: " + port);
        }
    }

    /**
     * Retrieves the current port number on which the RMI server is operating.
     *
     * @return the current port number as an integer
     */
    public int getCurrentPort() {
        return currentPort;
    }

    /**
     * Exports a remote object to the RMI registry with the specified name.
     * This method binds the given remote object to the registry, allowing it
     * to be accessed by clients using the provided name.
     *
     * @param obj  the remote object to be exported
     * @param name the name under which the remote object will be registered in the RMI registry
     * @throws RemoteException if an error occurs during the export process or if the registry is not created
     */
    public void exportObject(Remote obj, String name) throws RemoteException {
       // Check if the registry is created
        if (!registryCreated) {
            throw new RemoteException("RMI registry not created");
        }

        // Bind the stub to the registry
        registry.rebind(name, obj);
    }
}
