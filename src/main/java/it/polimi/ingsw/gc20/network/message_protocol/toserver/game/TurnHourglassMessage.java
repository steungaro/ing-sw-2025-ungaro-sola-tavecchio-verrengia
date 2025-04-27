package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

// Event that is generated when a player rotates the hourglass
public record TurnHourglassMessage(
    String username // the username of the player who wants to rotate the hourglass
) implements Message {
    @Override
    public String toString() {
        return "TurnHourglassMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).turnHourglass(username);
    }
}
