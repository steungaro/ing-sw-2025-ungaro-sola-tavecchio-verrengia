package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

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
