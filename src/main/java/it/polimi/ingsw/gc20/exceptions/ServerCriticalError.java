package it.polimi.ingsw.gc20.exceptions;

public class ServerCriticalError extends RuntimeException {
    public ServerCriticalError(String message) {
        super(message);
    }
}
