package it.polimi.ingsw.gc20.network.socket;

import it.polimi.ingsw.gc20.network.common.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SocketServer implements Server {
    private ServerSocket serverSocket;

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(8080);
            // Accetta connessioni socket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
