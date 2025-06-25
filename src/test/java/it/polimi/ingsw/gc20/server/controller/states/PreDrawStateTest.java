package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PreDrawStateTest {
    GameController controller;
    PreDrawState state;

    /**
     * Sets up the initial state for testing the PreDrawState functionality.
     * <p>
     * This method is executed before each test case within the test class.
     * It initializes a GameController instance with specified parameters and
     * sets up the PreDrawState object to be tested, ensuring the proper
     * environment is prepared for the test execution.
     *
     * @throws InvalidStateException if the initialization of the game state
     * causes an invalid state exception to be thrown.
     */
    @BeforeEach
    void setUp() throws InvalidStateException {
        controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
        state = new PreDrawState(controller);
    }

    /**
     * Ensures that the timer functionality behaves as expected by introducing a delay
     * in the test execution. This method waits for a specified period (6000 milliseconds)
     * to allow actions or states relying on the passage of time to complete before progressing
     * with further testing or assertions.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping.
     */
    @Test
    void test() throws InterruptedException {
        Thread.sleep(6000); // wait for the timer to expire
    }

}