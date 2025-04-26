package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;

import java.io.Serializable;

/**
 * Message that is generated when a player lands on a planet
 */
public record LandPlanetMessage(
        String username, // the username of the player who wants to land on a planet
        String gameId, // the id of the game
        int planet // the id of the planet where the player wants to land
) implements Message, Serializable {

    @Override
    public String toString() {
        return "LandPlanetMessage {username = " + username + ", gameId = " + gameId + ", planet = " + planet + "}";
    }

    @Override
    public void handleMessage(MatchController matchController) {
        matchController.getGameController(gameId).landOnPlanet(username, planet);
    }
}
