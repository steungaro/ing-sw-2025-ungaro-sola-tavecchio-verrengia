package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

/**
 * Message that is generated when a player lands on a planet
 */
public record LandPlanetMessage(
        String username, // the username of the player who wants to land on a planet
        int planet // the id of the planet where the player wants to land
) implements Message {

    @Override
    public String toString() {
        return "LandPlanetMessage {username = " + username + ", planet = " + planet + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).landOnPlanet(username, planet);
    }
}
