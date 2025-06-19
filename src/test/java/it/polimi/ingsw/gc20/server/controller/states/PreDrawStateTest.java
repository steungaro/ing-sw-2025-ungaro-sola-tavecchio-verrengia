package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreDrawStateTest {
    GameController controller;
    PreDrawState state;

    @BeforeEach
    void setUp() throws InvalidStateException {
        controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
        state = new PreDrawState(controller);
    }

    @Test
    void test() throws InterruptedException {
        assertEquals("PreDrawState", state.toString());
        Thread.sleep(6000); // wait for the timer to expire
    }

}