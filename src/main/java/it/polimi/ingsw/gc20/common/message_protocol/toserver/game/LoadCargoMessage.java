package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;

/**
 * Message that is generated when a player loads a cargo onto their ship
 */
public record LoadCargoMessage(
        String username, // username of the player
        Pair<Integer, Integer> cargoHold, // coordinates of the cargo hold
        CargoColor cargoColor // color of the cargo
) implements Message {

    @Override
    public String toString() {
        return "LoadCargoMessage {username = " + username + ", cargoHold = " + cargoHold + ", cargoColor = " + cargoColor + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).loadCargo(username, cargoColor, cargoHold);
    }
}
