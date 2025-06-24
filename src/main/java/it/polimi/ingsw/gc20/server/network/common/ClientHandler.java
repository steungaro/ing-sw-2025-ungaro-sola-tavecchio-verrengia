package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * Interface representing the operations that any client handler implementation
 * must provide for managing the communication and state of a client in the system.
 * <p>
 * A ClientHandler is responsible for handling requests and sending responses
 * between the server and a connected client. It abstracts the specifics of
 * client communication, such as maintaining connections and processing incoming
 * messages.
 */
public interface ClientHandler {

    /**
     * Sends a message to the associated client.
     * This method is used to transmit a given {@code Message} object
     * to the client represented by the implementation of the {@code ClientHandler}.
     *
     * @param message the {@code Message} instance to be sent to the client.
     *                This object encapsulates the data and logic needed for communication.
     */
    void sendToClient(Message message);
    /**
     * Terminates the connection with the associated client.
     * This method should handle all necessary operations to cleanly
     * disconnect a client from the server, such as releasing resources,
     * updating connection states, and notifying relevant services or components
     * of the disconnection.
     */
    void disconnect ();
    /**
     * Retrieves the username of the client associated with this handler.
     *
     * @return the username of the client as a String.
     */
    String getClientUsername();
    /**
     * Checks if the client associated with this handler is currently connected.
     * This method is typically used to verify the active connection status of a client.
     *
     * @return {@code true} if the client is connected, {@code false} otherwise.
     */
    boolean isConnected();
}
