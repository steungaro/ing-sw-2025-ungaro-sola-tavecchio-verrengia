package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.components.Battery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    int level = 2;
    List<String> players = new ArrayList<>();
    String gameId = "1";
    GameModel gameModel = new GameModel();

    @BeforeEach
    void setUp() {
        players.add("player1");
        players.add("player2");
    }

    @Test
    void startGame() {
        gameModel.startGame(level, players, gameId);

        assertTrue(gameModel.getGame().getPile().getUnviewed().getFirst() instanceof Battery);
    }
}