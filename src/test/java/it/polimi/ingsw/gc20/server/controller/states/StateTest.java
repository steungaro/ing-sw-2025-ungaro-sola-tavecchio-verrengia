package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class StateTest {

    /**
     * Tests the behavior of the {@code State} class to ensure that its methods correctly throw
     * {@code InvalidStateException} when they are called in an invalid state.
     * <p>
     * The test initializes instances of {@code GameController}, {@code GameModel}, and {@code State},
     * and verifies that various methods in {@code State} throw {@code InvalidStateException} when
     * invoked under conditions where the operation is not allowed given the state of the game.
     * <p>
     * Additionally, the test confirms the behavior of specific methods that are expected to return
     * null or default values under the tested circumstances. These include:
     * - Ensuring methods return expected null values for messages such as
     *   {@code createsCannonsMessage}, {@code createsShieldMessage}, {@code createsEnginesMessage},
     *   and {@code createsRollDiceMessage}.
     * - Validating numerical return values via methods such as {@code getCrew},
     *   {@code cargoToRemove}, and ensuring specific strings, such as
     *   {@code getAutomaticActionMessage}, return correct messages.
     */
    @Test
    void test() throws InvalidStateException {
        GameController controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);
        GameModel model = new GameModel();
        State state = new State(controller) {};
        new State(model) {};
        assertThrows(InvalidStateException.class, () ->state.resume(null));
        assertThrows(InvalidStateException.class, () ->state.activateEngines(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.activateCannons(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.activateShield(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.acceptCard(null));
        assertThrows(InvalidStateException.class, () ->state.addComponentToBooked(null));
        assertThrows(InvalidStateException.class, () ->state.addComponentToViewed(null));
        assertThrows(InvalidStateException.class, () ->state.addAlien(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.loadCargo(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.unloadCargo(null, null, null));
        assertThrows(InvalidStateException.class, () ->state.moveCargo(null, null, null, null));
        assertThrows(InvalidStateException.class, () ->state.currentQuit(null));
        assertThrows(InvalidStateException.class, () ->state.chooseBranch(null, null));
        assertThrows(InvalidStateException.class, () ->state.endMove(null));
        assertThrows(InvalidStateException.class, state::getScore);
        assertThrows(InvalidStateException.class, () -> state.landOnPlanet(null, 0));
        assertThrows(InvalidStateException.class, state::automaticAction);
        assertThrows(InvalidStateException.class, () -> state.peekDeck(null, 0));
        assertThrows(InvalidStateException.class, () -> state.placeComponent(null, null));
        assertThrows(InvalidStateException.class, () -> state.removeComp(null, null));
        assertThrows(InvalidStateException.class, () -> state.isShipValid(null));
        assertThrows(InvalidStateException.class, state::allShipsValidated);
        assertThrows(InvalidStateException.class, state::initAllShips);
        assertThrows(InvalidStateException.class, () -> state.loseEnergy(null, null));
        assertThrows(InvalidStateException.class, () -> state.loseCrew(null, null));
        assertThrows(InvalidStateException.class, state::allAssembled);
        assertThrows(InvalidStateException.class, state::allShipsReadyToFly);
        assertThrows(InvalidStateException.class, state::killGame);
        assertThrows(InvalidStateException.class, () -> state.setCurrentPlayer(null));
        assertThrows(InvalidStateException.class, state::nextPlayer);
        assertThrows(InvalidStateException.class, () -> state.rollDice(null));
        assertThrows(InvalidStateException.class, state::nextRound);
        assertThrows(InvalidStateException.class, () -> state.rejoin(null));
        assertNull(state.createsCannonsMessage());
        assertNull(state.createsShieldMessage());
        assertNull(state.createsEnginesMessage());
        assertNull(state.createsRollDiceMessage());
        assertEquals(0, state.getCrew());
        assertEquals("automatic action is taking place", state.getAutomaticActionMessage());
        assertEquals(0, state.cargoToRemove());
        assertNull(state.cargoReward());
        assertNull(state.getPlanets());
    }
  
}