package it.polimi.ingsw.gc20.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.controller.event.*;
import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.interfaces.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController gameController;
    GameModel model;
    AbandonedShipState abandonedShipState;
    AdventureCard adventureCard;
    String id = "0";

    @BeforeEach
    void setUp() {
        // Initialize the GameController object before each test
        // This is a placeholder, replace with actual initialization code
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
        model = new GameModel();
        adventureCard = new AdventureCard();
        adventureCard.setCrew(2);
        adventureCard.setCredits(3);
        adventureCard.setLostDays(1);
        abandonedShipState = new AbandonedShipState(gameController, model, adventureCard);
        model.startGame(2, players, "10");

        gameController.validateShip("player1");
    }

    @Test
    void setState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
    void getGameID() {
        assertEquals(id, gameController.getGameID());
    }

    @Test
    void getFirstOnlinePlayer() {
        String firstOnlinePlayer = gameController.getFirstOnlinePlayer();
        assertEquals("player1", firstOnlinePlayer);
    }

    @Test
    void drawCard() {
        gameController.drawCard();
        AdventureCard drawnCard = gameController.getModel().getActiveCard();
        assertNotNull(drawnCard);
        /*assertEquals(adventureCard.getCrew(), drawnCard.getCrew());
        assertEquals(adventureCard.getCredits(), drawnCard.getCredits());
        assertEquals(adventureCard.getLostDays(), drawnCard.getLostDays());*/
    }

    @Test
    void getState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
    void landOnPlanet() {
        /*Planet planetCard = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        planetCard.setReward(reward);*/

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
    void acceptCard() {
    }

    @Test
    void getOnlinePlayers() {
    }

    @Test
    void activateCannons() {
    }

    @Test
    void loseCrew() {
    }

    @Test
    void activateShield() {
    }

    @Test
    void getActiveCard() {
    }

    @Test
    void endMove() {
    }

    @Test
    void activateEngines() {
    }

    @Test
    void getAvailableColors() {
    }

    @Test
    void getPlayerData() {
    }

    @Test
    void setPlayerColor() {
    }

    @Test
    void getPlayerScores() {
    }

    @Test
    void giveUp() {
    }

    @Test
    void disconnectPlayer() {
    }

    @Test
    void reconnectPlayer() {
    }

    @Test
    void getPlayerByID() {
    }

    @Test
    void getAllUsernames() {
    }

    @Test
    void getDisconnectedPlayers() {
    }

    @Test
    void isPlayerDisconnected() {
    }

    @Test
    void takeComponentFromUnviewed() {
    }

    @Test
    void takeComponentFromViewed() {
    }

    @Test
    void takeComponentFromBooked() {
    }

    @Test
    void addComponentToBooked() {
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
    void removeComponentFromShip() {
    }

    @Test
    void validateShip() {
    }

    @Test
    void stopAssembling() {
    }

    @Test
    void turnHourglass() {
    }

    @Test
    void getHourglassTime() {
    }

    @Test
    void peekDeck() {
    }

    @Test
    void addAlien() {
    }

    @Test
    void readyToFly() {
    }

    @Test
    void rollDice() {
    }

    @Test
    void lastRolledDice() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void getInGameConnectedPlayers() {
    }
}