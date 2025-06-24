package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.MatchController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EndgameStateTest {
    GameController controller;
    EndgameState state;

    @BeforeEach
    void setUp() throws InvalidStateException {
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        state = new EndgameState(controller);
        controller.setState(state);
    }

    @Test
    void testState() throws InterruptedException {
        Thread.sleep(30000); // wait for the scheduler to run
        assertNull(MatchController.getInstance().getGameController("testGame"));
        assertEquals("EndgameState", state.toString());
    }
}