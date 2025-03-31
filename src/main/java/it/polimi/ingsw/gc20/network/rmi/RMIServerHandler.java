package it.polimi.ingsw.gc20.network.rmi;

import it.polimi.ingsw.gc20.exceptions.ServerCriticalError;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ServerError;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerHandler {
    private final int port = 1099;
    boolean registryCreated = false;
    private int usedPort = 0;


    /**
     *  Method to create a registry on a specific port
     * @param port the port on which the registry will be created
     *  throws ServerCriticalError if the server could not be started
     *  throws RemoteException if the registry is already created
     *  throw  RemoteException if the registry could not be created
     */
    public void createRegistry(int port) throws ServerCriticalError, RemoteException {
        if (!registryCreated) {
            try {
                String localIP = InetAddress.getLocalHost().getHostAddress();
                System.setProperty("java.rmi.server.hostname", localIP);
            } catch (UnknownHostException e) {
                throw new ServerCriticalError("Server could not be started");
            }

            // Creating the registry
            registryCreated = true;
            LocateRegistry.createRegistry(port);
            usedPort = port;
        } else {
            throw new RemoteException("Registry already created");
        }
    }

    /**
     *  Method to create a registry on the default port
     *  throws ServerCriticalError if the server could not be started
     *  throws RemoteException if the registry is already created
     *  throw  RemoteException if the registry could not be created
     */
    public void createRegistry() throws ServerCriticalError, RemoteException {
        createRegistry(this.port);
    }

    /**
     * Method to destroy the registry
     *
     * @throws RemoteException if the registry is not created
     * trhows RemoteException if the registry could not be destroyed
     */
    public void destroyRegistry() throws RemoteException {
        if (registryCreated) {
            Registry registry = LocateRegistry.getRegistry(usedPort);

            UnicastRemoteObject.unexportObject(registry, true);

            usedPort = 0;
            registryCreated = false;
        } else {
            throw new RemoteException("Registry not created");
        }
    }

    
}
