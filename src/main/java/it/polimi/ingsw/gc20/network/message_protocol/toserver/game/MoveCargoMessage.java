package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.io.Serializable;

/**
 * Message that is generated when a player activates the double cannons
 */
public record MoveCargoMessage(
        String username, // username of the player
        String gameId, // id of the game
        Pair<Integer, Integer> source, // coordinates of the source
        Pair<Integer, Integer> destination, // coordinates of the destination (can be null)
        CargoColor cargoColor // color of the cargo
) implements Message, Serializable {

    @Override
    public String toString() {
        return "MoveCargoMessage {username = " + username + ", gameId = " + gameId + ", source = " + source + ", destination = " + destination + ", cargoColor = " + cargoColor + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).moveCargo(username, cargoColor, source, destination);
    }
}
