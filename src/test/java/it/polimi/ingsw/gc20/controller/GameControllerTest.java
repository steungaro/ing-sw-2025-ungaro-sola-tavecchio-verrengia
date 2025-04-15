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
<<<<<<< Updated upstream
        adventureCard = new AdventureCard();
        adventureCard.setCrew(2);
        adventureCard.setCredits(3);
        adventureCard.setLostDays(1);
        abandonedShipState = new AbandonedShipState(gameController, model, adventureCard);
=======
        model.startGame(2, players, "10");
        abandonedShipState = new AbandonedShipState(gameController, model, new AdventureCard());
>>>>>>> Stashed changes
    }

    @Test
    void setState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
    void getGameID() {
<<<<<<< Updated upstream
        assertEquals(id, gameController.getGameID());
=======
        String expectedId = "0";
        assertEquals(expectedId, gameController.getGameID());
>>>>>>> Stashed changes
    }

    @Test
    void getFirstOnlinePlayer() {
<<<<<<< Updated upstream
        String firstOnlinePlayer = gameController.getFirstOnlinePlayer();
        assertEquals("player1", firstOnlinePlayer);
=======
        String expectedPlayer = "player1";
        assertEquals(expectedPlayer, gameController.getFirstOnlinePlayer());
>>>>>>> Stashed changes
    }

    @Test
    void drawCard() {
<<<<<<< Updated upstream
        gameController.drawCard();
        AdventureCard drawnCard = gameController.getModel().getActiveCard();
        assertNotNull(drawnCard);
=======
        // Assuming the deck is not empty
        gameController.drawCard();
>>>>>>> Stashed changes
    }

    @Test
    void getState() {
<<<<<<< Updated upstream
=======
        // Assuming the state is set to AbandonedShipState
>>>>>>> Stashed changes
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
<<<<<<< Updated upstream
    void landOnPlanet() throws InvalidTurnException {
        Planet planet = new Planet();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        planet.setReward(reward);
        planet.setAvailable(true);
        List<Planet> planets = new ArrayList<>();
        planets.add(planet);

        AdventureCard planetsCard = new AdventureCard();
        planetsCard.setPlanets(planets);
        planetsCard.setLostDays(1);
        planetsCard.setPlanets(planets);

        PlanetsState planetsState = new PlanetsState(model, gameController, planetsCard);
        gameController.setState(planetsState);

        gameController.landOnPlanet("player1", 0);
    }

    @Test
    void loadCargo() throws InvalidTurnException, CargoException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController, model, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHold = new CargoHold();
        CargoHold.setSlots(3);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.loadCargo("player1", loaded, CargoHold);
    }

    @Test
    void unloadCargo() throws InvalidTurnException, CargoException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController, model, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHold = new CargoHold();
        CargoHold.setSlots(3);
        List<CargoColor> cargoHeld = new ArrayList<>();
        cargoHeld.add(loaded);
        CargoHold.setCargoHeld(cargoHeld);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.loadCargo("player1", loaded, CargoHold);
        gameController.unloadCargo("player1", loaded, CargoHold);
    }

    @Test
    void moveCargo() throws InvalidTurnException, CargoException {
        // TODO -> Better test in State section
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController, model, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        CargoHoldFrom.setCargoHeld(cargoHeldFrom);

        CargoHold CargoHoldTo = new CargoHold();
        CargoHoldTo.setSlots(3);
        List<CargoColor> cargoHeldTo = new ArrayList<>();
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.loadCargo("player1", loaded, CargoHoldTo);
        CargoHoldTo.setCargoHeld(cargoHeldTo);

        gameController.unloadCargo("player1", loaded, CargoHoldFrom);
=======
    void landOnPlanet() {
        // Assuming the player is on a planet
        // TODO : first fix the planet (Adventure Card) and then test this method
    }

    @Test
    void loadCargo() {
        // Assuming the player has a cargo hold and the cargo is available
        // TODO : first fix the cargo (Adventure Card) and then test this method
    }

    @Test
    void unloadCargo() {
        // Assuming the player has a cargo hold and the cargo is available
        // TODO : first fix the cargo (Adventure Card) and then test this method
    }

    @Test
    void moveCargo() {
        // Assuming the player has a cargo hold and the cargo is available
        // TODO : first fix the cargo (Adventure Card) and then test this method
>>>>>>> Stashed changes
    }

    @Test
    void acceptCard() throws InvalidTurnException {
        // TODO -> Better test in State section
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController, model, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        CargoHoldFrom.setCargoHeld(cargoHeldFrom);

        CargoHold CargoHoldTo = new CargoHold();
        CargoHoldTo.setSlots(3);
        List<CargoColor> cargoHeldTo = new ArrayList<>();
        gameController.getModel().setActiveCard(abandonedStationCard);

        gameController.acceptCard("player1");
    }

    @Test
    void getOnlinePlayers() {
        // TODO -> int or list ????
        int size = gameController.getOnlinePlayers();
        assertEquals(4, size);
    }

    @Test
    void activateCannons() {
        // TODO -> Better test in State section
        // TODO -> First FIX the cannon
    }

    @Test
    void loseCrew() throws InvalidTurnException {
        // TODO -> Better test in State section

        AdventureCard adventureCard1 = new AdventureCard();
        adventureCard1.setCrew(2);
        adventureCard1.setCredits(3);
        adventureCard1.setLostDays(1);
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController, gameController.getModel(), adventureCard1);
        gameController.setState(abandonedShipState1);
        gameController.getModel().setActiveCard(adventureCard);

        Cabin cabin1 = new Cabin();
        cabin1.setAstronauts(2);

        Cabin cabin2 = new Cabin();
        cabin2.setAstronauts(1);

        List<Cabin> cabins = new ArrayList<>();
        cabins.add(cabin1);
        cabins.add(cabin2);

        gameController.loseCrew("player1", cabins);
        assertEquals(cabin1.getAstronauts() + cabin2.getAstronauts(), 1);
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