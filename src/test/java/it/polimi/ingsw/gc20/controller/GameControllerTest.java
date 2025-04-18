package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.network.event.game.PlaceComponentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;

import java.util.*;

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
        abandonedShipState = new AbandonedShipState(model, gameController, adventureCard);
    }

    @Test
    void setState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    /*@Test
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
    }

    @Test
    void getState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
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
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, abandonedStationCard);

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
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, abandonedStationCard);

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
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, abandonedStationCard);

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
    }

    @Test
    void acceptCard() throws InvalidTurnException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, abandonedStationCard);

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
        int size = gameController.getOnlinePlayers();
        assertEquals(4, size);
    }

    @Test
    void activateCannons() {
        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(meteorSwarmState);
        Cannon cannon1 = new Cannon();
        List<Cannon> cannons = new ArrayList<>();
        cannons.add(cannon1);
        Battery battery = new Battery();
        battery.setSlots(3);
        battery.fillBattery();
        List<Battery> batteries = new ArrayList<>();
        batteries.add(battery);
        // TODO: Implement the test for activateCannons
    }

    @Test
    void getModel() {
        GameModel model = gameController.getModel();
        assertNotNull(model);
    }

    @Test
    void loseCrew() throws InvalidTurnException {
        AdventureCard adventureCard1 = new AdventureCard();
        adventureCard1.setCrew(2);
        adventureCard1.setCredits(3);
        adventureCard1.setLostDays(1);
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard1);
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
    void activateShield() throws InvalidTurnException, InvalidShipException {
        AdventureCard adventureCard1 = new AdventureCard();
        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile1 = new Projectile();
        projectile1.setDirection(Direction.DOWN);
        projectile1.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(projectile1);
        adventureCard1.setProjectiles(projectiles);

        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard1);

        Shield shield = new Shield();
        Direction[] directions = {Direction.UP, Direction.RIGHT};
        shield.setCoveredSides(directions);

        Battery battery = new Battery();
        battery.setSlots(3);
        battery.fillBattery();

        gameController.getModel().setActiveCard(adventureCard1);
        gameController.setState(meteorSwarmState);
        gameController.rollDice("player1");
        gameController.activateShield("player1", shield, battery);
    }

    @Test
    void getActiveCard() {
        gameController.getModel().setActiveCard(adventureCard);
        assertEquals(adventureCard, gameController.getActiveCard());
    }

    @Test
    void endMove() throws InvalidTurnException, InvalidShipException {
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.endMove("player1");
    }

    @Test
    void activateEngines() throws InvalidTurnException, InvalidShipException, InvalidTileException {
        CombatZone0State combatZone0State = new CombatZone0State(gameController, gameController.getModel(), adventureCard);
        gameController.setState(combatZone0State);

        Engine engine1 = new Engine();
        engine1.setDoublePower(true);

        List<Engine> engines = new ArrayList<>();
        engines.add(engine1);
        gameController.getPlayerByID("player1").getShip().addComponent(engine1, 1, 1);
        Battery battery = new Battery();
        battery.setSlots(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);
        List<Battery> batteries = new ArrayList<>();
        batteries.add(battery);

        gameController.activateEngines("player1", batteries, engines);
    }

    @Test
    void getPlayerData() {
        Player player = gameController.getPlayerByID("player1");
        assertNotNull(player);
        assertEquals("player1", player.getUsername());
    }

    @Test
    void setPlayerColor() {
        PlayerColor color = PlayerColor.BLUE;
        Player player = gameController.getPlayerByID("player1");
        assertEquals(color, player.getColor());
    }

    @Test
    void getPlayerScores() {
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);

        Map<String, Integer> playerScores = gameController.getPlayerScores();
    }

    @Test
    void giveUp() {
        gameController.giveUp("player1");
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);

        Map<String, Integer> playerScores = gameController.getPlayerScores();

    }

    @Test
    void disconnectPlayer() {
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.getInGameConnectedPlayers().contains("player1"));
        assertTrue(gameController.getDisconnectedPlayers().contains("player1"));
        assertEquals(3, gameController.getOnlinePlayers());

        gameController.disconnectPlayer("player2");
        gameController.disconnectPlayer("player3");
    }

    @Test
    void reconnectPlayer() {
        assertThrows(IllegalArgumentException.class, () -> gameController.reconnectPlayer("player5"));
        gameController.disconnectPlayer("player1");
        gameController.reconnectPlayer("player1");
        assertFalse(gameController.isPlayerDisconnected("player1"));
        assertTrue(gameController.getInGameConnectedPlayers().contains("player1"));
        assertFalse(gameController.getDisconnectedPlayers().contains("player1"));
        assertEquals(4, gameController.getOnlinePlayers());
    }

    @Test
    void getPlayerByID() {
        Player player = gameController.getPlayerByID("player1");
        assertNotNull(player);
        assertEquals("player1", player.getUsername());
    }

    @Test
    void getAllUsernames() {
        List<String> usernames = gameController.getAllUsernames();
        assertNotNull(usernames);
        assertEquals(4, usernames.size());
        assertTrue(usernames.contains("player1"));
        assertTrue(usernames.contains("player2"));
        assertTrue(usernames.contains("player3"));
        assertTrue(usernames.contains("player4"));
    }

    @Test
    void getDisconnectedPlayers() {
        gameController.disconnectPlayer("player1");
        gameController.disconnectPlayer("player2");
        List<String> disconnectedPlayers = gameController.getDisconnectedPlayers();
        assertNotNull(disconnectedPlayers);
        assertEquals(2, disconnectedPlayers.size());
        assertTrue(disconnectedPlayers.contains("player1"));
        assertTrue(disconnectedPlayers.contains("player2"));
    }

    @Test
    void isPlayerDisconnected() {
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.isPlayerDisconnected("player2"));
    }

    @Test
    void takeComponentFromUnviewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));

        gameController.takeComponentFromUnviewed("player1", comp);

        assertFalse(gameController.getModel().getGame().getPile().getUnviewed().contains(comp));
        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void takeComponentFromViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        assertThrows(NoSuchElementException.class, () -> gameController.takeComponentFromViewed("player1", comp));

        gameController.addComponentToViewed("player1", comp);

        Component comp2 = gameController.takeComponentFromViewed("player1", comp);

        assertEquals(comp, comp2);
    }

    @Test
    void takeComponentFromBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        assertThrows(NoSuchElementException.class, () -> gameController.takeComponentFromViewed("player1", comp));

        gameController.addComponentToBooked("player1", comp);

        Component comp2 = gameController.takeComponentFromBooked("player1", comp);

        assertEquals(comp, comp2);
    }

    @Test
    void addComponentToBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        assertThrows(NoSuchElementException.class, () -> gameController.takeComponentFromViewed("player1", comp));

        gameController.addComponentToBooked("player1", comp);

        Component comp2 = gameController.takeComponentFromBooked("player1", comp);

        assertEquals(comp, comp2);
    }

    @Test
    void addComponentToViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        assertThrows(NoSuchElementException.class, () -> gameController.takeComponentFromViewed("player1", comp));

        gameController.addComponentToViewed("player1", comp);

        Component comp2 = gameController.takeComponentFromViewed("player1", comp);

        assertEquals(comp, comp2);
    }

    @Test
    void placeComponent() {
        Cabin cabin = new Cabin();
        PlaceComponentEvent event = new PlaceComponentEvent("player1", 1, 2, 2);
        gameController.placeComponent("player1", cabin, 2, 2);
        assertEquals(cabin, gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2));
    }

    @Test
    void rotateComponentClockwise() {
        Cabin cabin = new Cabin();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.RIGHT, ConnectorEnum.ZERO);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.LEFT, ConnectorEnum.ZERO);
        cabin.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(cabin, 2, 2);
        gameController.rotateComponentClockwise(cabin);
        assertEquals(ConnectorEnum.S, cabin.getConnectors().get(Direction.RIGHT));
        assertEquals(ConnectorEnum.ZERO, cabin.getConnectors().get(Direction.DOWN));
    }

    @Test
    void rotateComponentCounterclockwise() {
        Cabin cabin = new Cabin();
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.S);
        connectors.put(Direction.RIGHT, ConnectorEnum.ZERO);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.LEFT, ConnectorEnum.ZERO);
        cabin.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(cabin, 2, 2);
        gameController.rotateComponentCounterclockwise(cabin);
        assertEquals(ConnectorEnum.S, cabin.getConnectors().get(Direction.LEFT));
        assertEquals(ConnectorEnum.ZERO, cabin.getConnectors().get(Direction.UP));
    }

    @Test
    void removeComponentFromShip() {
        Cabin cabin = new Cabin();
        PlaceComponentEvent event = new PlaceComponentEvent("player1", 1, 2, 2);
        gameController.placeComponent("player1", cabin, 2, 2);

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        gameController.setState(validatingShipState);
        gameController.removeComponentFromShip("player1", cabin);

        assertNull(gameController.getPlayerByID("player1").getShip().getComponentAt(2,2));
    }

    @Test
    void validateShip() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        gameController.setState(validatingShipState);

    }

    @Test
    void stopAssembling() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);
        gameController.stopAssembling("player1", 1);
    }

    @Test
    void turnHourglass() throws HourglassException, InterruptedException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);

        // Wait 6 seconds to simulate the hourglass being turned
        Thread.sleep(5000);

        gameController.turnHourglass("player1");
        assertEquals(1, gameController.getModel().getTurnedHourglass());

        Thread.sleep(5000);

        gameController.stopAssembling("player1", 1);

        gameController.turnHourglass("player1");
        assertEquals(2, gameController.getModel().getTurnedHourglass());
    }

    @Test
    void getHourglassTime() throws InterruptedException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);
        Thread.sleep(500);
        assertEquals(4, gameController.getHourglassTime("player1"));

        Thread.sleep(1000);

        assertEquals(3, gameController.getHourglassTime("player1"));

        Thread.sleep(8000);

        assertEquals(0, gameController.getHourglassTime("player1"));
    }

    @Test
    void peekDeck() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        gameController.setState(assemblingState);
        List<AdventureCard> peekedCards = gameController.peekDeck("player1", 3);
        assertNotNull(peekedCards);
        assertEquals(3, peekedCards.size());
    }

    @Test
    void addAlien() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        gameController.setState(validatingShipState);

        AlienColor alienColor = AlienColor.BROWN;

        Cabin cabin = new Cabin();
        cabin.setColor(alienColor);

        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));

        gameController.getPlayerByID("player1").getShip().addComponent(cabin, 2, 2);

        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        gameController.addAlien("player1", alienColor, cabin);
        gameController.readyToFly("player1");
        validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
        gameController.readyToFly("player2");
        validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
        gameController.readyToFly("player3");
        validatingShipState.isShipValid(gameController.getPlayerByID("player4"));
        gameController.readyToFly("player4");

        assertThrows(IllegalStateException.class, () -> gameController.addAlien("player1", alienColor, cabin));
        assertEquals(alienColor, ((Cabin)(gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2))).getCabinColor());
    }

    @Test
    void readyToFly() {
        assertThrows(IllegalStateException.class, () -> gameController.readyToFly("player1"));


        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        gameController.setState(validatingShipState);

        assertThrows(IllegalArgumentException.class, () -> gameController.readyToFly("player1"));

        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        gameController.readyToFly("player1");
        validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
        gameController.readyToFly("player2");
        validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
        gameController.readyToFly("player3");
        validatingShipState.isShipValid(gameController.getPlayerByID("player4"));
        gameController.readyToFly("player4");

        assertTrue(validatingShipState.allShipsReadyToFly());
    }

    @Test
    void defeated() {
        gameController.defeated("player1");
        assertFalse(gameController.getPlayerByID("player1").isInGame());
    }

    @Test
    void rollDice() throws InvalidTurnException, InvalidShipException {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        assertThrows(InvalidTurnException.class, () -> gameController.rollDice("player2"));

        gameController.rollDice("player1");
        int lastRolled = 0;
        lastRolled=gameController.getModel().getGame().lastRolled();
        assertNotNull(gameController.getModel().getGame().lastRolled());
    }

    @Test
    void lastRolledDice() throws InvalidTurnException, InvalidShipException {
        assertThrows(ArithmeticException.class, () -> gameController.getModel().getGame().lastRolled());

        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        gameController.rollDice("player1");
        int lastRolled = gameController.getModel().getGame().lastRolled();

        gameController.rollDice("player1");
        lastRolled=0;
        lastRolled = gameController.getModel().getGame().lastRolled();
        assertNotEquals(0, lastRolled);
    }

    @Test
    void getCurrentPlayer() {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        String currentPlayer = slaversState.getCurrentPlayer();
        assertEquals("player1", currentPlayer);
    }

    @Test
    void getInGameConnectedPlayers() {
        List<String> inGameConnectedPlayers = gameController.getInGameConnectedPlayers();
        assertNotNull(inGameConnectedPlayers);
        assertEquals(4, inGameConnectedPlayers.size());
        assertTrue(inGameConnectedPlayers.contains("player1"));
        assertTrue(inGameConnectedPlayers.contains("player2"));
        assertTrue(inGameConnectedPlayers.contains("player3"));
        assertTrue(inGameConnectedPlayers.contains("player4"));
    }*/
}