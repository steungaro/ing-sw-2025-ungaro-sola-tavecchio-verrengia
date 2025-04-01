package it.polimi.ingsw.gc20.network.socket;

import it.polimi.ingsw.gc20.network.common.ClientHandler;

import java.net.Socket;

public class SocketClientHandler extends ClientHandler {
    private Socket clientSocket;

    public SocketClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleRequest() {
        // Gestisci richieste socket
    }
}
