package it.polimi.ingsw.gc20.server.exceptions;

public class ServerCriticalError extends RuntimeException {
    public ServerCriticalError(String message) {
        super(message);
    }
}
