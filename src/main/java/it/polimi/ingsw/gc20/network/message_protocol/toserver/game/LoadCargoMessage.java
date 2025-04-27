package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.io.Serializable;

/**
 * Message that is generated when a player loads a cargo onto their ship
 */
public record LoadCargoMessage(
        String username, // username of the player
        String gameId, // id of the game
        Pair<Integer, Integer> cargoHold, // coordinates of the cargo hold
        CargoColor cargoColor // color of the cargo
) implements Message, Serializable {

    @Override
    public String toString() {
        return "LoadCargoMessage {username = " + username + ", gameId = " + gameId + ", cargoHold = " + cargoHold + ", cargoColor = " + cargoColor + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).loadCargo(username, cargoColor, cargoHold);
    }
}
