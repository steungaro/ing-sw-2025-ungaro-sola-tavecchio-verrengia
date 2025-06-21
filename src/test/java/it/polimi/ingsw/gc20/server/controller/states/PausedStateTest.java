package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PausedStateTest {
    PausedState state;
    GameController controller;

    @BeforeEach
    void setUp() throws InvalidStateException {
        AdventureCard card = new AdventureCard();
        card.setName("Stardust");
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        assertNotNull(controller.getModel());
        controller.getModel().setActiveCard(card);
        state = new PausedState(new OpenSpaceState(controller.getModel(), controller, card), controller.getModel(), controller);
        controller.setState(state);
    }

    @Test
    void testResume() throws InvalidStateException {
        assertEquals("PausedState", state.toString());
        state.resume("player1");
        assertEquals("OpenSpaceState", controller.getState().toString());
        assertFalse(state.isConcurrent());
    }
}