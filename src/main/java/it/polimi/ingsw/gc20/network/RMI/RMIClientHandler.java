package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.network.common.ClientHandler;
import java.util.logging.Logger;

public class RMIClientHandler implements ClientHandler {
    private final Logger LOGGER = Logger.getLogger(RMIClientHandler.class.getName());
    private String username;
    private boolean connected;



    @Override
    public void handleRequest() {
        // Gestisci le richieste RMI
    }
}
