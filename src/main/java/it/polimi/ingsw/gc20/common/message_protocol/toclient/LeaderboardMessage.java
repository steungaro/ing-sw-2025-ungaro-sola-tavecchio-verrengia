package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.Message;

import java.util.Map;

/**
 * This message is sent to the client to inform them about the current leaderboard.
 * It contains a map of player names and their corresponding scores.
 */
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
