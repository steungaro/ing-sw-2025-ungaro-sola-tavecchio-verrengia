package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;

/**
 * Message that is generated when a player activates the double cannons
 */
public record MoveCargoMessage(
        String username, // username of the player
        Pair<Integer, Integer> source, // coordinates of the source
        Pair<Integer, Integer> destination, // coordinates of the destination (can be null)
        CargoColor cargoColor // color of the cargo
) implements Message {

    @Override
    public String toString() {
        return "MoveCargoMessage {username = " + username + ", source = " + source + ", destination = " + destination + ", cargoColor = " + cargoColor + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).moveCargo(username, cargoColor, source, destination);
    }
}
