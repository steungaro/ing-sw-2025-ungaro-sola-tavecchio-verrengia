package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.Serializable;

// Message that is generated when a player rotates the component in their hand
public record RotateComponentMessage(
        String username, // username of the player
        String gameId, // id of the game
        int side // 0 = clockwise, 1 = counterclockwise
) implements Message, Serializable {
    @Override
    public String toString() {
        return "RotateComponentMessage {username =" + username + ", gameId = " + gameId + ", side = " + side + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        if (side == 1)
            matchController.getGameController(gameId).rotateComponentCounterclockwise(username);
        else
            matchController.getGameController(gameId).rotateComponentClockwise(username);
    }
}
