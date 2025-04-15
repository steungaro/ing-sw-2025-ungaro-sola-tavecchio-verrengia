package it.polimi.ingsw.gc20.network.RMI;

import java.util.logging.Logger;

public class TestViewImpl implements ViewInterface {
    private static final Logger LOGGER = Logger.getLogger(TestViewImpl.class.getName());

    @Override
    public void updateView(Object message) {
        // Handle the message received from the server
        LOGGER.info("Message received: " + message);
    }

    @Override
    public void notifyDisconnection() {
        // Handle the disconnection notification
        LOGGER.info("Client disconnected");
    }

}
