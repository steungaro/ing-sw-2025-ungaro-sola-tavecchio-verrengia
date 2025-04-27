package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record ValidateShipMessage(
        String username // the username of the player who wants to validate the ship
) implements Message {
    @Override
    public String toString() {
        return "ValidateShipMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).validateShip(username);
    }
}
