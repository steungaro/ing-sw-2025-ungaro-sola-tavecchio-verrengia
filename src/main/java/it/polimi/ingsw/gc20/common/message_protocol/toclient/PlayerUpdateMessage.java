package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

//TODO wait for the view structure to be defined
public record PlayerUpdateMessage(
        String username, // The username of the receiving client (broadcast to all clients)
        int creditsAdded,
        Boolean inGame
) implements Message {
    @Override
    public String toString() {
        return "PlayerUpdateMessage{" +
                "username='" + username + '\'' +
                ", creditsAdded=" + creditsAdded +
                ", inGame=" + inGame +
                '}';
    }

    @Override
    public void handleMessage() {
        // Handle the player update message (client side)
    }
}
