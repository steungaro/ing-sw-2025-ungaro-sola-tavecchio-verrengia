package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent by the player to rotate the game's hourglass.
 */
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
