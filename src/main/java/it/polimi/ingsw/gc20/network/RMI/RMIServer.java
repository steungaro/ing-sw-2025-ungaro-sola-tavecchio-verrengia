package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.network.common.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer implements Server {
    private Registry registry;

    @Override
    public void start() {
        try {
            registry = LocateRegistry.createRegistry(1099);
            // Registra gli oggetti remoti
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Chiudi il registro
    }
}
