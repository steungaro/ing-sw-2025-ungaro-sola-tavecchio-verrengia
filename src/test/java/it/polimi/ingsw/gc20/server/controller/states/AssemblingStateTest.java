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

    @BeforeAll
    static void setUp() {
        List<String> players = List.of("player1", "player2", "player3");
        try {
            controller = new GameController("testGame","testGame", players, 2);
        } catch (Exception e) {
            fail();
        }

    }

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
    }
}
