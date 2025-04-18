package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.components.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    GameController gameController;
    GameModel model;
    AbandonedShipState abandonedShipState;
    AdventureCard adventureCard;
    String id = "0";

    @BeforeEach
    void setUp() {
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
        adventureCard = new AdventureCard();
        adventureCard.setCrew(2);
        adventureCard.setCredits(3);
        adventureCard.setLostDays(1);
        abandonedShipState = new AbandonedShipState(gameController, model, adventureCard);
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
    }

    @Test
    void placeComponent() {
    }

    @Test
    void rotateComponentClockwise() {
    }

    @Test
    void rotateComponentCounterclockwise() {
    }

    @Test
    void stopAssembling() {
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
    }
}