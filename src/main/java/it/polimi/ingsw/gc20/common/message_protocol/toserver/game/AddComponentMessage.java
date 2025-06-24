package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * Message generated when a player places the component in their hand on the ship
 */
public record AddComponentMessage(
        String username, // username of the player
        PileEnum toType // type of the component (viewed, booked)
) implements Message {
    @Override
    public String toString() {
        return "AddComponentMessage {username = " + username + ", to = " + toType + "}";
    }

    @Override
    public void handleMessage() {
        if (toType == PileEnum.BOOKED) {
            MatchController.getInstance().getGameControllerForPlayer(username).addComponentToBooked(username);
        } else if (toType == PileEnum.VIEWED) {
            MatchController.getInstance().getGameControllerForPlayer(username).addComponentToViewed(username);
        }
    }
}
