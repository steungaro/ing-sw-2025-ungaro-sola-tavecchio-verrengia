package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.util.Map;

public record LeaderboardMessage(
        Map<String, Integer> leaderboard
) implements Message {
    @Override
    public String toString() {
        return "leaderboard=" + leaderboard;
    }

    @Override
    public void handleMessage() {
        ClientGameModel.getInstance().leaderBoardMenu(leaderboard);
    }
}
