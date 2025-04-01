package it.polimi.ingsw.gc20.network.common;

public abstract class ClientHandler {
    protected boolean running;

    public abstract void handleRequest();

    public void setRunning(boolean running) {
        this.running = running;
    }
}
