package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

/**
 * This message is sent by the player to roll the dice during their turn.
 */
public record RollDiceMessage(
        String username // the username of the player who wants to roll the dice
) implements Message {
    @Override
    public String toString() {
        return "RollDiceMessage {username = " + username + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).rollDice(username);
    }
}
