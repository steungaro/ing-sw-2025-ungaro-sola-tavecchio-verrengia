package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

public record ReadyToFlyMessage(
        String username // the username of the player who is ready to fly
) implements Message {
    @Override
    public String toString() {
        return "ReadyToFlyMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).readyToFly(username);
    }
}
