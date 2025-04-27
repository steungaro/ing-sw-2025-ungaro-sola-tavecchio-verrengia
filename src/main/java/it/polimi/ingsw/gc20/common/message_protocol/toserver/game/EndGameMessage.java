package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record EndGameMessage(
        String username // the username of the player who wants to end the game they are playing
) implements Message {
    @Override
    public String toString() {
        return "EndGameMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).killGame(username);
    }
}
