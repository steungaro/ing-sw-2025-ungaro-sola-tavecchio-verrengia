package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import org.javatuples.Pair;

public record AddAlienMessage(
        String username, // the username of the player who wants to add an alien
        Pair<Integer, Integer> alienCoordinates, // the coordinates of the alien to be added
        AlienColor color // the color of the alien to be added
) implements Message {
    @Override
    public String toString() {
        return "AddAlienMessage {username = " + username + ", alienCoordinates = " + alienCoordinates + ", color = " + color + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).addAlien(username, color, alienCoordinates);
    }
}
