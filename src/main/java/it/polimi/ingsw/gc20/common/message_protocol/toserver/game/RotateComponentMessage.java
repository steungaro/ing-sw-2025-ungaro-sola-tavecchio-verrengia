package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent by the player to rotate the component in their hand.
 */
public record RotateComponentMessage(
        String username, // username of the player
        int side // 0 = clockwise, 1 = counterclockwise
) implements Message {
    @Override
    public String toString() {
        return "RotateComponentMessage {username =" + username + ", side = " + side + "}";
    }

    @Override
    public void handleMessage() {
        if (side == 1)
            MatchController.getInstance().getGameControllerForPlayer(username).rotateComponentCounterclockwise(username);
        else
            MatchController.getInstance().getGameControllerForPlayer(username).rotateComponentClockwise(username);
    }
}
