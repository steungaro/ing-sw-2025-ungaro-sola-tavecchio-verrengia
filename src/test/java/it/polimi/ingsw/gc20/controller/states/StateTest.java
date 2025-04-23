package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.ConnectorEnum;
import it.polimi.ingsw.gc20.model.components.Direction;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.components.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    static GameController gameController;
    static GameModel model;
    static AbandonedShipState abandonedShipState;
    static AdventureCard adventureCard;
    static String id = "0";

    @BeforeAll
    static void setUp() {
        String player1 = "player1";
        String player2 = "player2";
        String player3 = "player3";
        String player4 = "player4";
        List<String> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        int level = 2;

        gameController = new GameController(id, players, level);
        model = gameController.getModel(); // Inizializzazione mancante
        adventureCard = new AdventureCard();
        adventureCard.setCrew(2);
        adventureCard.setCredits(3);
        adventureCard.setLostDays(1);
        abandonedShipState = new AbandonedShipState(model, gameController, adventureCard);
    }


    /*@Test
    void getModel() {
        assertEquals(gameController.getModel(), abandonedShipState.getModel());
    }

    @Test
    void getController() {
        assertEquals(gameController, abandonedShipState.getController());
    }

    @Test
    void takeComponentFromUnviewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), comp);
        assertEquals(comp, gameController.getModel().getGame().getPile().getViewed().getFirst());
    }

    @Test
    void takeComponentFromViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.getModel().getGame().getPile().getViewed().add(comp);

        assemblingState.takeComponentFromViewed(gameController.getPlayerByID("player1"), comp);
        assertThrows(NoSuchElementException.class,  () -> gameController.takeComponentFromViewed("player1", comp));
    }

    @Test
    void takeComponentFromBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assertThrows(IllegalArgumentException.class,  () -> gameController.takeComponentFromBooked("player1", comp));

        gameController.addComponentToBooked("player1", comp);

        assemblingState.takeComponentFromBooked(gameController.getPlayerByID("player1"), comp);
    }

    @Test
    void addComponentToBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.addComponentToBooked(gameController.getPlayerByID("player1"), comp);
        assertTrue( ((NormalShip) (gameController.getModel().getInGamePlayers().getFirst().getShip())).getBooked().contains(comp));
    }

    @Test
    void addComponentToViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.addComponentToViewed(comp);

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void placeComponent() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        assertThrows(InvalidParameterException.class, () -> assemblingState.placeComponent(gameController.getPlayerByID("player1"), comp, 0, 0));
        assemblingState.placeComponent(gameController.getPlayerByID("player1"), comp, 2, 2);

        assertEquals(gameController.getPlayerByID("player1").getShip().getComponentAt(2,2), comp);
        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void rotateComponentClockwise() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.LEFT));

        assemblingState.rotateComponentClockwise(comp);

        assertEquals(connectors, comp.getConnectors());
    }

    @Test
    void rotateComponentCounterclockwise() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.LEFT));

        assemblingState.rotateComponentCounterclockwise(comp);

        assertEquals(connectors, comp.getConnectors());
    }

    @Test
    void stopAssembling() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.stopAssembling(gameController.getPlayerByID("player1"), 0);
        assertTrue(assemblingState.allAssembled());
    }

    @Test
    void peekDeck() {
    }

    @Test
    void getHourglassTime() {
    }

    @Test
    void turnHourglass() {
    }

    @Test
    void isShipValid() {
    }

    @Test
    void removeComp() {
    }

    @Test
    void addAlien() {
    }

    @Test
    void initAllShips() {
    }

    @Test
    void landOnPlanet() {
    }

    @Test
    void loadCargo() {
    }

    @Test
    void unloadCargo() {
    }

    @Test
    void moveCargo() {
    }

    @Test
    void loseEnergy() {
    }

    @Test
    void testLandOnPlanet() {
    }

    @Test
    void acceptCard() {
    }

    @Test
    void loseCrew() {
    }

    @Test
    void endMove() {
    }

    @Test
    void activateEngines() {
    }

    @Test
    void activateShield() {
    }

    @Test
    void shootEnemy() {
    }

    @Test
    void activateCannons() {
    }

    @Test
    void allAssembled() {
    }

    @Test
    void allShipsReady() {
    }

    @Test
    void readyToFly() {
    }

    @Test
    void automaticAction() {
    }

    @Test
    void resume() {
    }

    @Test
    void rollDice() {
    }*/
}