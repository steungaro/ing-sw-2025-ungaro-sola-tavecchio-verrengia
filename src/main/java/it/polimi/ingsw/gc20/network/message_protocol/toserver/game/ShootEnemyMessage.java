package it.polimi.ingsw.gc20.network.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.controller.MatchController;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import org.javatuples.Pair;

import java.util.List;

public record ShootEnemyMessage(
        String username, // the username of the player who wants to shoot
        List<Pair<Integer, Integer>> cannonsCoordinates, // the coordinates of the cannons
        List<Pair<Integer, Integer>> batteriesCoordinates // the coordinates of the batteries
) implements Message {
    @Override
    public String toString() {
        return "ShootEnemyMessage {username = " + username + ", cannonsCoordinates = " + cannonsCoordinates + ", batteriesCoordinates = " + batteriesCoordinates + "}";
    }

    @Override
    public void handleMessage() {
        MatchController.getInstance().getGameControllerForPlayer(username).shootEnemy(username, cannonsCoordinates, batteriesCoordinates);
    }
}
