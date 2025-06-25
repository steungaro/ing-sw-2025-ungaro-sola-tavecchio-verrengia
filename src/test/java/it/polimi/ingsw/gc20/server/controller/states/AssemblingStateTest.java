package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AssemblingStateTest {
    static GameController controller;
    AssemblingState state;

    /**
     * Sets up the environment and initializes the necessary components before all tests in this class are executed.
     * <p>
     * This method is annotated with {@code @BeforeAll}, indicating that it will be executed once prior to any test methods in the class.
     * It initializes the {@code GameController} instance with a predefined game name, description, list of players, and number of starting players.
     * If any exception occurs during the setup process, the method will fail the test to prevent further execution.
     */
    @BeforeAll
    static void setUp() {
        List<String> players = List.of("player1", "player2", "player3");
        try {
            controller = new GameController("testGame","testGame", players, 2);
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * Tests the constructor and initial state setup of the {@code AssemblingState} class.
     * <p>
     * This test uses reflection to access and verify the initial values of private fields
     * within the {@code AssemblingState} instance after its creation. Specifically, it checks:
     * <p>
     * - The {@code assembled} map to ensure each player is initially set to {@code false}.
     * - The {@code componentsInHand} map to verify each player starts with a {@code null} component.
     * - The {@code deckPeeked} map to confirm its initial state contains {@code null} values for specific keys.
     * <p>
     * The test asserts these initializations align with the expected behavior of the {@code AssemblingState} class.
     *
     * @throws NoSuchFieldException if any of the specified fields do not exist in the {@code AssemblingState} class.
     * @throws IllegalAccessException if access to the declared fields is not permitted.
     */
    @Test
    @SuppressWarnings("unchecked")
    void constructorTest() throws NoSuchFieldException, IllegalAccessException {
        state = new AssemblingState(controller.getModel(), controller);

        // Accesso ai campi privati con reflection
        Field assembledField = AssemblingState.class.getDeclaredField("assembled");
        assembledField.setAccessible(true);
        Map<Player, Boolean> assembled = (Map<Player, Boolean>) assembledField.get(state);

        Field componentsInHandField = AssemblingState.class.getDeclaredField("componentsInHand");
        componentsInHandField.setAccessible(true);
        Map<Player, Component> componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);

        Field deckPeekedField = AssemblingState.class.getDeclaredField("deckPeeked");
        deckPeekedField.setAccessible(true);
        Map<Integer, Player> deckPeeked = (Map<Integer, Player>) deckPeekedField.get(state);

        for (Player player : controller.getModel().getInGamePlayers()) {
            assert assembled.get(player) == false;
            assert componentsInHand.get(player) == null;
        }

        assertNull(deckPeeked.get(1));
        assertNull(deckPeeked.get(2));
        assertNull(deckPeeked.get(3));
    }

    /**
     * Tests the various state transitions and operations within the {@code AssemblingState} class.
     * <p>
     * The test verifies the following behaviors:
     * - Assigning a component to a player when taken from the unviewed components.
     * - Moving components between the states (unviewed, viewed, and booked).
     * - Rotating components clockwise and counterclockwise while retaining their association with the player.
     * - Placing a component at a specified position in the game board.
     * - Peeking into a deck and ensuring the player is correctly recorded in the "deckPeeked" map.
     * - Stopping the assembly process for a player and updating the "assembled" state correctly.
     * - Turning the hourglass with a delay and handling potential exceptions during the process.
     * - Verifying that not all players are assembled and that the state supports concurrency.
     * - Rejoining and resuming the state for a specific player.
     * <p>
     * This test also ensures exceptions, such as {@code ComponentNotFoundException},
     * {@code InvalidStateException}, and others, are properly handled without failing unintentionally.
     * <p>
     * Assertions are made at multiple stages to confirm the correctness of the state transitions
     * and the integrity of the underlying private fields accessed via reflection.
     * <p>
     * The test case is annotated with {@code @Test} and {@code @SuppressWarnings("unchecked")}
     * to indicate its role in validating the state logic and allowing unchecked casts during field access.
     */
    @Test
    @SuppressWarnings("unchecked")
    void stateTest() {
        state = new AssemblingState(controller.getModel(), controller);
        Player player = controller.getModel().getInGamePlayers().getFirst();

        try {
            state.takeComponentFromUnviewed(player, 1);
            // Check if the component is assigned to the player
            Field componentsInHandField = AssemblingState.class.getDeclaredField("componentsInHand");
            componentsInHandField.setAccessible(true);
            Map<Player, Component> componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNotNull(componentsInHand.get(player));
            state.addComponentToViewed(player);
            componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNull(componentsInHand.get(player));
            state.takeComponentFromViewed(player, 0);
            componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNotNull(componentsInHand.get(player));
            state.addComponentToBooked(player);
            componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNull(componentsInHand.get(player));
            state.takeComponentFromBooked(player, 0);
            componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNotNull(componentsInHand.get(player));
            state.rotateComponentClockwise(player);
            state.rotateComponentCounterclockwise(player);
            // Check if the component is still assigned to the player
            componentsInHand = (Map<Player, Component>) componentsInHandField.get(state);
            assertNotNull(componentsInHand.get(player));
            state.placeComponent(player, new Pair<>(3, 1));
            state.peekDeck(player, 1);
            Field deckPeekedField = AssemblingState.class.getDeclaredField("deckPeeked");
            deckPeekedField.setAccessible(true);
            Map<Integer, Player> deckPeeked = (Map<Integer, Player>) deckPeekedField.get(state);
            assertEquals(player, deckPeeked.get(1));
            state.stopAssembling(player, 1);
            Field assembledField = AssemblingState.class.getDeclaredField("assembled");
            assembledField.setAccessible(true);
            Map<Player, Boolean> assembled = (Map<Player, Boolean>) assembledField.get(state);
            assertTrue(assembled.get(player));
            Thread.sleep(90000);
            state.turnHourglass(player);
        } catch (ComponentNotFoundException | InvalidStateException | NoSuchFieldException | IllegalAccessException |
                 DuplicateComponentException | NoSpaceException | InvalidTileException | InvalidIndexException |
                 InterruptedException | HourglassException e) {
            fail(e);
        }
        assertFalse(state.allAssembled());
        assertTrue(state.isConcurrent());
        state.rejoin("player1");
        state.resume("player1");
    }
}
