package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record PeekDeckMessage (
        String username, // username of the player
        int number // number of the deck to peek (1/2/3
) implements Message {
    @Override
    public String toString() {
        return "PeekDeckMessage {username = " + username + ", number = " + number + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).peekDeck(username, number);
    }
}
