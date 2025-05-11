package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

//TODO wait for the view structure to be defined
public record PlayerUpdateMessage(
        String username, // The username of the receiving client (broadcast to all clients)
        int creditsAdded,
        Boolean inGame,
        PlayerColor color,
        int posInBoard //needs to be adapted to the board space (position % spaces)
) implements Message {
    @Override
    public String toString() {
        return username + " has been updated: " +
                "creditsAdded=" + creditsAdded +
                ", inGame=" + inGame +
                ", color=" + color +
                ", posInBoard=" + posInBoard;
    }

    @Override
    public void handleMessage() {
        // Handle the player update message (client side)
    }
}
