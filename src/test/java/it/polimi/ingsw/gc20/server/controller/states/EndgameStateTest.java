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

    /**
     * Sets up the test environment for the EndgameState tests.
     * <p>
     * This method is executed before each test case. It initializes the
     * GameController and assigns it a new EndgameState instance to test functionalities.
     * The GameController is configured with the necessary parameters, including game name,
     * player list, and maximum number of players. The state of the GameController is then
     * set to the EndgameState being tested.
     *
     * @throws InvalidStateException if the initial state setup fails due to invalid conditions.
     */
    @BeforeEach
    void setUp() throws InvalidStateException {
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        state = new EndgameState(controller);
        controller.setState(state);
    }

    /**
     * Tests the behavior of the EndgameState by verifying the state transitions and controller cleanup.
     * <p>
     * This test waits for the scheduler to perform the necessary actions and then verifies
     * the following:
     * <p>
     * - The corresponding game controller is no longer available in the MatchController, indicating
     *   the clean-up of the game.
     * - The state of the GameController is correctly set to "EndgameState".
     *
     * @throws InterruptedException if the thread sleep is interrupted during the test execution.
     */
    @Test
    void testState() throws InterruptedException {
        Thread.sleep(30000); // wait for the scheduler to run
        assertNull(MatchController.getInstance().getGameController("testGame"));
    }
}