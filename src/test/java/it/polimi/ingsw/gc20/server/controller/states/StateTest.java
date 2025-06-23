package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class StateTest {

    @Test
    void test() throws InvalidStateException {
        GameController controller = new GameController("testGame", "testGame", List.of("player1", "player2"), 2);

        State state = new State(controller) {};

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

    }

}