package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

public record AcceptCardMessage(
        String username // the username of the player who wants to accept the card
) implements Message {
    @Override
    public String toString() {
        return "AcceptCardMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).acceptCard(username);
    }
}
