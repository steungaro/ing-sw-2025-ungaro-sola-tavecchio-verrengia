package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.io.Serializable;

/**
 * Message that is generated when a player activates a shield
 */
public record ActivateShieldMessage(
        String username, // username of the player
        String gameId, // id of the game
        Pair<Integer, Integer> shieldCoordinates,    // coordinates of the shield
        Pair<Integer, Integer> batteryCoordinates    // coordinates of the battery
) implements Message, Serializable {

    @Override
    public String toString() {
        return "ActivateShieldMessage {username = " + username + "shield = " + shieldCoordinates + "battery = " + batteryCoordinates + "}";
    }
    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).activateShield(username, shieldCoordinates, batteryCoordinates);
    }

}
