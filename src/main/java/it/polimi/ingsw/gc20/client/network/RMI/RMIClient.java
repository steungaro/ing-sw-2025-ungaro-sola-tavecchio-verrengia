package it.polimi.ingsw.gc20.client.network.RMI;

import it.polimi.ingsw.gc20.client.network.common.Client;

public class RMIClient implements Client {
    private String serverAddress;
    private final int port;

    public RMIClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    @Override
    public void start() {
        // Implementation for starting the RMI client
    }

    @Override
    public void stop() {
        // Implementation for stopping the RMI client
    }

    @Override
    public void sendMessage(String message) {
        // Implementation for sending a message via RMI
    }

    @Override
    public void receiveMessage(String message) {
        // Implementation for receiving a message via RMI
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean login(String username) {
        return false;
    }

    @Override
    public boolean logout(String username) {
        return false;
    }
}
