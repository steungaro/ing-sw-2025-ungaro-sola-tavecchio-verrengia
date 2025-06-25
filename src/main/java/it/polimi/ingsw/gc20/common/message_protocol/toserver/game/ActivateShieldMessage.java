package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import org.javatuples.Pair;

/**
 * Message that is generated when a player activates a shield
 */
public record ActivateShieldMessage(
        String username, // username of the player
        Pair<Integer, Integer> shieldCoordinates,    // coordinates of the shield
        Pair<Integer, Integer> batteryCoordinates    // coordinates of the battery
) implements Message {

    @Override
    public String toString() {
        return "ActivateShieldMessage {username = " + username + "shield = " + shieldCoordinates + "battery = " + batteryCoordinates + "}";
    }
    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).activateShield(username, shieldCoordinates, batteryCoordinates);
    }

}
