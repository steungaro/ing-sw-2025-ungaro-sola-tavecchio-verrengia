package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PausedStateTest {
    PausedState state;
    GameController controller;

    /**
     * Sets up the test environment for unit tests related to the PausedState class.
     * <p>
     * This method initializes the required objects, including the GameController,
     * the PausedState instance, and an AdventureCard object with pre-defined
     * properties. The method sets these objects in a specific state to ensure
     * consistent and reliable test execution.
     *
     * @throws InvalidStateException if the state transition during setup is invalid.
     */
    void setUp() throws InvalidStateException {
        AdventureCard card = new AdventureCard();
        card.setName("Stardust");
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        assertNotNull(controller.getModel());
        controller.getModel().setActiveCard(card);
        state = new PausedState(new OpenSpaceState(controller.getModel(), controller, card), controller.getModel(), controller);
        controller.setState(state);
    }

    /**
     * Tests the behavior of the {@code resume} method in the {@code PausedState} class.
     * <p>
     * This test ensures that the {@code resume} method correctly transitions the game
     * state from {@code PausedState} to the previous state and verifies that the state
     * is not concurrent after the transition.
     * <p>
     * The method sets up the test environment, invokes the {@code resume} method with
     * a player's username, and asserts that the resulting state is not concurrent.
     *
     * @throws InvalidStateException if the state transition is invalid.
     */
    @Test
    void testResume() throws InvalidStateException {
        setUp();
        state.resume("player1");
        assertFalse(state.isConcurrent());
    }

    /**
     * Tests the behavior of the {@code resume} method in the {@code PausedState} class
     * when using a specific {@code AdventureCard} state context.
     * <p>
     * This test validates that the {@code resume} method transitions the game state
     * from {@code PausedState} to its associated state correctly and ensures that the
     * resulting state is not concurrent.
     * <p>
     * The test sets up the {@code PausedState} with a {@code CombatZone1State} context,
     * employs a player's username to invoke {@code resume}, and asserts post-conditions
     * on the state.
     *
     * @throws InvalidStateException if the state transition during the test is invalid.
     */
    @Test
    void testResume2() throws InvalidStateException {
        AdventureCard card = new AdventureCard();
        card.setName("CombatZone");
        controller = new GameController("testGame", "testGame", List.of("player1", "player2", "player3"), 2);
        assertNotNull(controller.getModel());
        controller.getModel().setActiveCard(card);
        state = new PausedState(new CombatZone1State(controller.getModel(), controller, card), controller.getModel(), controller);
        controller.setState(state);
        state.resume("player1");
        assertFalse(state.isConcurrent());
    }
}