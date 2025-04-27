package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.server.exceptions.ServerCriticalError;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerHandler {
    private Registry registry;
    private boolean registryCreated = false;
    private int currentPort;

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
            throw new ServerCriticalError("Unable to create RMI registry on port: " + port);
        }
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

        // Bind the stub to the registry
        registry.rebind(name, obj);
    }
}
