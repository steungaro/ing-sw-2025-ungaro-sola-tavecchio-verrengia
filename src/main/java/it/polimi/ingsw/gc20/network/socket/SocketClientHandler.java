package it.polimi.ingsw.gc20.network.socket;

import it.polimi.ingsw.gc20.network.common.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClientHandler implements ClientHandler {
    private static final Logger LOGGER = Logger.getLogger(SocketClientHandler.class.getName());
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;
    private boolean connected = true;

    public SocketClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            // Importante: creare prima l'output stream per evitare deadlock
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            LOGGER.severe("Errore nell'inizializzazione degli stream: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void handleRequest() {
        try {
            // Prima ricezione è tipicamente l'autenticazione
            Object loginRequest = in.readObject();
            // Gestisci autenticazione

            // Loop principale di ascolto
            while (connected) {
                Object message = in.readObject();
                processMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Errore nella gestione della richiesta: " + e.getMessage());
            disconnect();
        }
    }

    private void processMessage(Object message) {
        // Delega alle classi appropriate in base al tipo di messaggio
    }

    @Override
    public void sendMessage(Object message) {
        if (!connected) return;

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Errore nell'invio del messaggio: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (!connected) return;

        connected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
        } catch (IOException e) {
            LOGGER.warning("Errore nella disconnessione: " + e.getMessage());
        }
    }

    @Override
    public String getClientUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}