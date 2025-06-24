package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

public record EndMoveMessage (
        String username // username of the player that ends the move
) implements Message {
    @Override
    public String toString() {
        return "EndMoveMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).endMove(username);
    }
}
