package it.polimi.ingsw.gc20.common.message_protocol.toserver.game;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import org.javatuples.Pair;

public record LoseEnergyMessage(
        String username,
        Pair<Integer, Integer> coordinates
) implements Message {

    @Override
    public void handleMessage() {

        MatchController.getInstance().getGameControllerForPlayer(username).loseEnergy(username, coordinates);
    }
}
