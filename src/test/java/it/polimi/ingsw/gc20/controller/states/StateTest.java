package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Planet;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.BooleanSupplier;
import org.javatuples.Pair;

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


    @Test
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

        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        try {
            assemblingState.addComponentToViewed(gameController.getPlayerByID("player1"));
        } catch (DuplicateComponentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(comp, gameController.getModel().getGame().getPile().getViewed().getFirst());
    }

    @Test
    void takeComponentFromViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        try {
            assemblingState.addComponentToViewed(gameController.getPlayerByID("player1"));
        } catch (DuplicateComponentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assemblingState.takeComponentFromViewed(gameController.getPlayerByID("player1"), 0);
        /*IndexOutOfBoundsException exception = assertThrows(
                IndexOutOfBoundsException.class,
                () -> gameController.takeComponentFromViewed("player1", 0)
        );*/
        try {
            assemblingState.addComponentToBooked(gameController.getPlayerByID("player1"));
        } catch (NoSpaceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(gameController.getModel().getGame().getPile().getViewed().isEmpty());
        assertTrue(((NormalShip) (gameController.getModel().getInGamePlayers().getFirst().getShip())).getBooked().contains(comp));
    }

    @Test
    void takeComponentFromBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        //assertThrows(IllegalArgumentException.class,  () -> gameController.takeComponentFromBooked("player1", 0));
        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToBooked("player1");
        assemblingState.takeComponentFromBooked(gameController.getPlayerByID("player1"), 0);
        try {
            assemblingState.addComponentToViewed(gameController.getPlayerByID("player1"));
        } catch (DuplicateComponentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        //assertTrue(((NormalShip) (gameController.getModel().getInGamePlayers().getFirst().getShip())).getBooked().isEmpty());
        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void addComponentToBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);

        try {
            assemblingState.addComponentToBooked(gameController.getPlayerByID("player1"));
        } catch (NoSpaceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertTrue( ((NormalShip) (gameController.getModel().getInGamePlayers().getFirst().getShip())).getBooked().contains(comp));
    }

    @Test
    void addComponentToViewed() throws DuplicateComponentException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);

        assemblingState.addComponentToViewed(gameController.getPlayerByID("player1"));

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void placeComponent() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        Pair <Integer, Integer> coordinates = new Pair<>(0, 0);
        assertThrows(InvalidParameterException.class, () -> assemblingState.placeComponent(gameController.getPlayerByID("player1"), coordinates));
        Pair <Integer, Integer> coordinates2 = new Pair<>(2, 2);
        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        assemblingState.placeComponent(gameController.getPlayerByID("player1"), coordinates2);

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

        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        assemblingState.rotateComponentClockwise(gameController.getPlayerByID("player1"));

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

        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        assemblingState.rotateComponentCounterclockwise(gameController.getPlayerByID("player1"));

        assertEquals(connectors, comp.getConnectors());
    }

    @Test
    void stopAssembling() throws InvalidIndexException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assemblingState.stopAssembling(gameController.getPlayerByID("player1"), 1);
        assertEquals(1, gameController.getPlayerByID("player1").getPosition());
    }

    @Test
    void peekDeck() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        List<AdventureCard> cards = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            try {
                cards.addAll(assemblingState.peekDeck(gameController.getPlayerByID("player1"), i));
            } catch (InvalidIndexException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
        assertEquals(9, cards.size());
    }

    @Test
    void getHourglassTime() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        int time = assemblingState.getHourglassTime(gameController.getPlayerByID("player1"));
        assertTrue(time == 90 || time == 89);
    }

    /*@Test
    void turnHourglass() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        try {
            Thread.sleep(90000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            assemblingState.turnHourglass(gameController.getPlayerByID("player1"));
        } catch (HourglassException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(1, gameController.getModel().getTurnedHourglass());
    }*/

    @Test
    void isShipValid() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        assertTrue(validatingShipState.isShipValid(gameController.getPlayerByID("player1")));

        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        assemblingState.placeComponent(gameController.getPlayerByID("player1"), new Pair<>(1, 2));
        assertFalse(validatingShipState.isShipValid(gameController.getPlayerByID("player1")));
        // TODO: add more tests for isShipValid
    }

    @Test
    void removeComp() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        assemblingState.takeComponentFromUnviewed(gameController.getPlayerByID("player1"), 0);
        assemblingState.placeComponent(gameController.getPlayerByID("player1"), new Pair<>(1, 2));

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        try {
            validatingShipState.removeComp(gameController.getPlayerByID("player1"), new Pair<>(1, 2));
        } catch (ComponentNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull(gameController.getPlayerByID("player1").getShip().getComponentAt(1, 2));
    }

    @Test
    void addAlien() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        Cabin cabin = new Cabin();
        cabin.setColor(AlienColor.BROWN);
        assertThrows(InvalidAlienPlacement.class, () -> validatingShipState.addAlien(gameController.getPlayerByID("player1"), AlienColor.PURPLE, cabin));
        try {
            validatingShipState.addAlien(gameController.getPlayerByID("player1"), AlienColor.BROWN, cabin);
        } catch (InvalidAlienPlacement e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
    }

    @Test
    void initAllShips() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player4"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player1"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player2"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player3"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player4"));

        validatingShipState.initAllShips();
    }

    @Test
    void landOnPlanet() {
        List<Planet> planets = new ArrayList<>();
        Planet planet = new Planet();
        planets.add(planet);
        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.GREEN);
        planet.setReward(cargo);
        adventureCard.setPlanets(planets);
        PlanetsState planetsState = new PlanetsState(gameController.getModel(), gameController, adventureCard);
        try{
            planetsState.landOnPlanet(gameController.getPlayerByID("player1"), 0);
        } catch (InvalidParameterException | InvalidTurnException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
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
    }
}