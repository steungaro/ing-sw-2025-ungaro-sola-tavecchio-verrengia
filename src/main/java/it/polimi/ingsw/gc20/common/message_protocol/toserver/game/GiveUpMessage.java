package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

public record GiveUpMessage (
        String username // username of the player that gives up
) implements Message {
    @Override
    public String toString() {
        return "GiveUpMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).giveUp(username);
    }
}
