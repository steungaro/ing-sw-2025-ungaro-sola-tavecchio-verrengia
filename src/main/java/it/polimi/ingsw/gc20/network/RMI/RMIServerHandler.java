package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.exceptions.ServerCriticalError;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerHandler {
    private Registry registry;
    private boolean registryCreated = false;
    private static final int DEFAULT_PORT = 1099;
    private int currentPort = DEFAULT_PORT;

    /**
     * Function to create the RMI registry.
     *
     * @throws ServerCriticalError if the registry cannot be created.
     */
    public void createRegistry() throws ServerCriticalError {
        try {
            registry = LocateRegistry.createRegistry(DEFAULT_PORT);
            registryCreated = true;
        } catch (RemoteException e) {
            throw new ServerCriticalError("Unable to create RMI registry");
        }
    }

    /**
     * Function to create the RMI registry on a specific port.
     *
     * @param port the port to create the registry on.
     * @throws ServerCriticalError if the registry cannot be created.
     */
    public void createRegistry(int port) throws ServerCriticalError {
        try {
            registry = LocateRegistry.createRegistry (port);
            registryCreated = true;
            currentPort = port;
        } catch (RemoteException e) {
            throw new ServerCriticalError("Unable to create RMI registry on port ");
        }
    }

    /**
     * Function to get the RMI registry.
     *
     * @return the RMI registry.
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * Function to get the current port.
     *
     * @return int the current port.
     */
    public int getCurrentPort() {
        return currentPort;
    }

    /**
     * Function to export a remote object and register it in the RMI registry.
     *
     * @param obj the remote object to export.
     * @apiNote the object must implement the Remote interface.
     * @param name the name to bind the object to in the registry.
     * @throws RemoteException if the operation fails.
     * @throws MalformedURLException if the name is formatted incorrectly.
     */
    public void exportObject(Remote obj, String name) throws RemoteException, MalformedURLException {
       // Check if the registry is created
        if (!registryCreated) {
            throw new RemoteException("RMI registry not created");
        }

        // export the object and obtain the stub
        Remote stub = UnicastRemoteObject.exportObject(obj, 0);

        // Bind the stub to the registry
        Naming.rebind("rmi://localhost:" + currentPort + "/" + name, stub);
    }
}
