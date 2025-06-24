package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent by the player to take a component from one of the piles (unviewed, viewed, or booked).
 */
public record TakeComponentMessage(
        String username, // username of the player
        int index, // index of the component
        PileEnum fromType // type of the component (unviewed, viewed, booked)
) implements Message {
    @Override
    public String toString() {
        return "TakeComponentMessage {username = " + username + ", index = " + index +
                ", from = " + fromType + "}";
    }

    @Override
    public void handleMessage() {
        if (fromType == PileEnum.BOOKED) {
            MatchController.getInstance().getGameControllerForPlayer(username).takeComponentFromBooked(username, index);
        } else if (fromType == PileEnum.UNVIEWED) {
            MatchController.getInstance().getGameControllerForPlayer(username).takeComponentFromUnviewed(username, index);
        } else if (fromType == PileEnum.VIEWED) {
            MatchController.getInstance().getGameControllerForPlayer(username).takeComponentFromViewed(username, index);
        }
    }
}
