package it.polimi.ingsw.gc20.server.controller;

import it.polimi.ingsw.gc20.server.controller.states.*;
import it.polimi.ingsw.gc20.server.model.cards.*;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.*;
import it.polimi.ingsw.gc20.server.model.player.*;
import it.polimi.ingsw.gc20.server.model.ship.*;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.gc20.server.exceptions.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController gameController;
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
        try {
            gameController = new GameController(id, players, level);
            adventureCard = new AdventureCard();
            adventureCard.setCrew(2);
            adventureCard.setCredits(3);
            adventureCard.setLostDays(1);
            abandonedShipState = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        } catch (InvalidStateException _){
            fail();
        }
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
    }

    @Test
    void getState() {
        gameController.setState(abandonedShipState);
        assertEquals(abandonedShipState.toString(), gameController.getState());
    }

    @Test
    void landOnPlanet(){
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

        PlanetsState planetsState = new PlanetsState(gameController.getModel(), gameController, planetsCard);
        gameController.setState(planetsState);

        gameController.landOnPlanet("player1", 0);
    }

    @Test
    void loadCargo() throws InvalidTileException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        gameController.getPlayerByID("player1").getShip().addComponent(cargoHold, 2, 2);
        gameController.getModel().setActiveCard(abandonedStationCard);
        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.loadCargo("player1", loaded, position);
    }

    @Test
    void unloadCargo() throws InvalidTileException, CargoNotLoadable, CargoFullException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHold = new CargoHold();
        CargoHold.setSlots(3);
        List<CargoColor> cargoHeld = new ArrayList<>();
        cargoHeld.add(loaded);
        gameController.getPlayerByID("player1").getShip().addComponent(CargoHold, 2, 2);

        for (CargoColor cargoColor : cargoHeld) {
            CargoHold.loadCargo(cargoColor);
        }

        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.getModel().setActiveCard(abandonedStationCard);
        gameController.loadCargo("player1", loaded, position);
        gameController.unloadCargo("player1", loaded, position);
    }

    @Test
    void moveCargo() throws CargoNotLoadable, CargoFullException, InvalidTileException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        for (CargoColor cargoColor : cargoHeldFrom) {
            CargoHoldFrom.loadCargo(cargoColor);
        }

        gameController.getPlayerByID("player1").getShip().addComponent(CargoHoldFrom, 2, 2);

        CargoHold CargoHoldTo = new CargoHold();
        CargoHoldTo.setSlots(3);

        gameController.getModel().setActiveCard(abandonedStationCard);
        Pair<Integer, Integer> position = new Pair<>(2, 2);
        gameController.loadCargo("player1", loaded, position);

        gameController.unloadCargo("player1", loaded, position);

        gameController.moveCargo("player1", loaded, position, position);
    }

    @Test
    void acceptCard() throws CargoNotLoadable, CargoFullException {
        AdventureCard abandonedStationCard = new AdventureCard();
        List<CargoColor> reward = new ArrayList<>();
        reward.add(CargoColor.BLUE);
        reward.add(CargoColor.YELLOW);
        abandonedStationCard.setReward(reward);
        AbandonedStationState abandonedStationState = new AbandonedStationState(gameController.getModel(), gameController, abandonedStationCard);

        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);

        CargoHold CargoHoldFrom = new CargoHold();
        CargoHoldFrom.setSlots(3);
        List<CargoColor> cargoHeldFrom = new ArrayList<>();
        cargoHeldFrom.add(loaded);
        for (CargoColor cargoColor : cargoHeldFrom) {
            CargoHoldFrom.loadCargo(cargoColor);
        }

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
    void activateCannons() throws InvalidTileException {
        // Configurazione dello stato di sciame meteoriti


        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile1 = new Projectile();
        projectile1.setDirection(Direction.DOWN);
        projectile1.setFireType(FireType.HEAVY_METEOR);
        Projectile projectile2 = new Projectile();
        projectile2.setDirection(Direction.LEFT);
        projectile2.setFireType(FireType.LIGHT_METEOR);
        Projectile projectile3 = new Projectile();
        projectile3.setDirection(Direction.RIGHT);
        projectile3.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(projectile1);
        projectiles.add(projectile2);
        projectiles.add(projectile3);
        adventureCard.setProjectiles(projectiles);

        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(meteorSwarmState);

        // Aggiungo un cannone alla nave del giocatore
        Cannon cannon = new Cannon();
        gameController.getPlayerByID("player1").getShip().addComponent(cannon, 1, 1);
        Pair<Integer, Integer> cannonCoord = new Pair<>(1, 1);
        List<Pair<Integer, Integer>> cannons = new ArrayList<>();
        cannons.add(cannonCoord);

        // Aggiungo una batteria carica alla nave
        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);
        Pair<Integer, Integer> batteryCoord = new Pair<>(2, 2);
        List<Pair<Integer, Integer>> batteries = new ArrayList<>();
        batteries.add(batteryCoord);

        //tiro il dado
        gameController.rollDice("player1");
        // Attivo il cannone
        gameController.activateCannons("player1", cannons, batteries);

        // Verifico che l'energia della batteria sia diminuita (il cannone ha consumato energia)
        Battery usedBattery = (Battery) gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2);
        assertEquals(2, usedBattery.getAvailableEnergy());

        // NOTE: can't confirm the cannon was activated, as it depends on the game logic
    }

    @Test
    void getModel() {
        GameModel model = gameController.getModel();
        assertNotNull(model);
    }

    @Test
    void loseCrew() throws InvalidTileException {
        AdventureCard adventureCard1 = new AdventureCard();
        adventureCard1.setCrew(2);
        adventureCard1.setCredits(3);
        adventureCard1.setLostDays(1);
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard1);
        gameController.setState(abandonedShipState1);
        gameController.getModel().setActiveCard(adventureCard);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();


        Cabin cabin1 = new Cabin();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);

        Cabin cabin2 = new Cabin();

        cabin1.setConnectors(connectors);
        cabin2.setConnectors(connectors);

        gameController.getPlayerByID("player1").getShip().addComponent(cabin1, 2, 2);
        gameController.getPlayerByID("player1").getShip().addComponent(cabin2, 1, 1);
        gameController.getPlayerByID("player1").getShip().initAstronauts();

        Pair<Integer, Integer> position = new Pair<>(2, 2);
        Pair<Integer, Integer> position2 = new Pair<>(1, 1);
        List<Pair<Integer, Integer>> positions = new ArrayList<>();
        positions.add(position);
        positions.add(position2);
        gameController.acceptCard("player1");
        gameController.loseCrew("player1", positions);
        assertEquals(2, cabin1.getAstronauts() + cabin2.getAstronauts());
    }

    @Test
    void activateShield() throws InvalidTileException, InterruptedException {
        AdventureCard adventureCard1 = new AdventureCard();
        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile1 = new Projectile();
        projectile1.setDirection(Direction.DOWN);
        projectile1.setFireType(FireType.LIGHT_METEOR);
        projectiles.add(projectile1);
        adventureCard1.setProjectiles(projectiles);

        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);

        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(gameController.getModel(), gameController, adventureCard1);

        Shield shield = new Shield();
        Direction[] directions = {Direction.UP, Direction.RIGHT};
        shield.setCoveredSides(directions);
        shield.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(shield, 1, 1);
        Pair<Integer, Integer> shieldCoord = new Pair<>(1, 1);

        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        battery.setConnectors(connectors);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);
        Pair<Integer, Integer> batteryCoord = new Pair<>(2, 2);

        gameController.getModel().setActiveCard(adventureCard1);
        gameController.setState(meteorSwarmState);
        gameController.rollDice("player1");

        gameController.activateShield("player1", shieldCoord, batteryCoord);

        assertEquals(2, battery.getAvailableEnergy());
    }

    @Test
    void getActiveCard() {
        gameController.getModel().setActiveCard(adventureCard);
        assertEquals(adventureCard, gameController.getActiveCard());
    }

    @Test
    void endMove(){
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.endMove("player1");
    }

    @Test
    void activateEngines() throws InvalidTileException {
        CombatZone0State combatZone0State = new CombatZone0State(gameController.getModel(), gameController, adventureCard);
        gameController.setState(combatZone0State);

        Engine engine1 = new Engine();
        engine1.setDoublePower(true);

        gameController.getPlayerByID("player1").getShip().addComponent(engine1, 1, 1);
        Battery battery = new Battery();
        battery.setSlots(3);
        battery.setAvailableEnergy(3);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 2);

        gameController.getPlayerByID("player1").getShip().addComponent(engine1, 1, 2);
        gameController.getPlayerByID("player1").getShip().addComponent(battery, 2, 1);
        Pair<Integer, Integer> engineCoord1 = new Pair<>(1, 2);
        Pair<Integer, Integer> batteryCoord2 = new Pair<>(2, 1);
        List<Pair<Integer, Integer>> engineCoords = new ArrayList<>();
        engineCoords.add(engineCoord1);
        List<Pair<Integer, Integer>> batteryCoords = new ArrayList<>();
        batteryCoords.add(batteryCoord2);

        gameController.activateEngines("player1", engineCoords, batteryCoords);
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
        gameController.getScore();
    }

    @Test
    void giveUp() {
        PreDrawState preDrawState = new PreDrawState(gameController);
        gameController.setState(preDrawState);
        gameController.giveUp("player1");
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);

        assertFalse(gameController.getModel().getInGamePlayers().contains(gameController.getPlayerByID("player1")));
    }

    @Test
    void disconnectPlayer() {
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
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
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.getInGameConnectedPlayers().contains("player1"));
        gameController.reconnectPlayer("player1");
        gameController.preDrawConnect();
        assertFalse(gameController.isPlayerDisconnected("player1"));
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
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
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
        AbandonedShipState abandonedShipState1 = new AbandonedShipState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(abandonedShipState1);
        gameController.disconnectPlayer("player1");
        assertTrue(gameController.isPlayerDisconnected("player1"));
        assertFalse(gameController.isPlayerDisconnected("player2"));
    }

    @Test
    void takeComponentFromUnviewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();

        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));

        gameController.takeComponentFromUnviewed("player1", 0);

        assertFalse(gameController.getModel().getGame().getPile().getUnviewed().contains(comp));
    }

    @Test
    void takeComponentFromViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);

        gameController.addComponentToViewed("player1");

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void takeComponentFromBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToBooked("player1");
        gameController.takeComponentFromBooked("player1", 0);
        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));
        assertFalse(((NormalShip)(gameController.getPlayerByID("player1").getShip())).getBooked().contains(comp));
    }

    @Test
    void addComponentToBooked() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToBooked("player1");

        assertTrue(((NormalShip)(gameController.getPlayerByID("player1").getShip())).getBooked().contains(comp));
    }

    @Test
    void addComponentToViewed() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        assertNotNull(assemblingState.getModel());
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();

        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToViewed("player1");

        assertTrue(gameController.getModel().getGame().getPile().getViewed().contains(comp));

    }

    @Test
    void placeComponent() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);

        Pair<Integer, Integer> coordinates = new Pair<>(2, 2);
        gameController.placeComponent("player1", coordinates);
        assertEquals(comp, gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2));
    }

    @Test
    void rotateComponentClockwise() {
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.LEFT));

        gameController.rotateComponentClockwise("player1");

        assertEquals(connectors.get(Direction.UP), comp.getConnectors().get(Direction.RIGHT));
        assertEquals(connectors.get(Direction.RIGHT), comp.getConnectors().get(Direction.DOWN));
        assertEquals(connectors.get(Direction.DOWN), comp.getConnectors().get(Direction.LEFT));
        assertEquals(connectors.get(Direction.LEFT), comp.getConnectors().get(Direction.UP));
    }

    @Test
    void rotateComponentCounterclockwise() {
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();

        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        Component comp = gameController.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        connectors.put(Direction.UP, comp.getConnectors().get(Direction.UP));
        connectors.put(Direction.RIGHT, comp.getConnectors().get(Direction.RIGHT));
        connectors.put(Direction.DOWN, comp.getConnectors().get(Direction.DOWN));
        connectors.put(Direction.LEFT, comp.getConnectors().get(Direction.LEFT));

        gameController.rotateComponentCounterclockwise("player1");

        assertEquals(connectors.get(Direction.DOWN), comp.getConnectors().get(Direction.RIGHT));
        assertEquals(connectors.get(Direction.LEFT), comp.getConnectors().get(Direction.DOWN));
        assertEquals(connectors.get(Direction.UP), comp.getConnectors().get(Direction.LEFT));
        assertEquals(connectors.get(Direction.RIGHT), comp.getConnectors().get(Direction.UP));
    }

    @Test
    void removeComponentFromShip() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.takeComponentFromUnviewed("player1", 0);
        Pair<Integer, Integer> coordinates = new Pair<>(2, 2);
        gameController.placeComponent("player1", coordinates);

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);
        gameController.removeComponentFromShip("player1", coordinates);

        assertNull(gameController.getPlayerByID("player1").getShip().getComponentAt(2,2));
    }

    @Test
    void validateShip() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);
        gameController.validateShip("player1");
        assertFalse(validatingShipState.allShipsValidated());
    }

    @Test
    void stopAssembling() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.stopAssembling("player1", 1);
    }

   @Test
    void turnHourglass() throws InterruptedException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);

        Thread.sleep(90000);

        gameController.turnHourglass("player1");
    }

    @Test
    void getHourglassTime() throws InterruptedException {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Thread.sleep(9500);
        assertEquals(80, gameController.getModel().getRemainingTime());

        Thread.sleep(1000);

        assertEquals(79, gameController.getModel().getRemainingTime());
    }

    @Test
    void peekDeck() {
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        gameController.peekDeck("player1", 3);
    }

    @Test
    void addAlien() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
        gameController.setState(validatingShipState);

        AlienColor alienColor = AlienColor.BROWN;

        Cabin cabin = new Cabin();
        cabin.setColor(alienColor);
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.RIGHT, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.U);
        connectors.put(Direction.LEFT, ConnectorEnum.U);
        cabin.setConnectors(connectors);

        try {
            gameController.getPlayerByID("player1").getShip().addComponent(cabin, 2, 2);

            validatingShipState.isShipValid(gameController.getPlayerByID("player1"));

            Pair<Integer, Integer> cabinCoord = new Pair<>(2, 2);
            gameController.addAlien("player1", alienColor, cabinCoord);
            gameController.endMove("player1");
            validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
            gameController.endMove("player2");
            validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
            gameController.endMove("player3");
            validatingShipState.isShipValid(gameController.getPlayerByID("player4"));
            gameController.endMove("player4");
            assertEquals(alienColor, ((Cabin) (gameController.getPlayerByID("player1").getShip().getComponentAt(2, 2))).getCabinColor());
        } catch (InvalidStateException e) {
            fail("Ship validation failed: " + e.getMessage());
        } catch (InvalidTileException e) {
            fail("Invalid tile exception: " + e.getMessage());
        }
    }

    @Test
    void readyToFly() {
        try {
            ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel(), gameController);
            gameController.setState(validatingShipState);

            validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
            gameController.endMove("player1");
            validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
            gameController.endMove("player2");
            validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
            gameController.endMove("player3");
            validatingShipState.isShipValid(gameController.getPlayerByID("player4"));
            gameController.endMove("player4");

            assertTrue(validatingShipState.allShipsReadyToFly());
        } catch (InvalidStateException e) {
            fail("Ship validation failed: " + e.getMessage());
        }
    }

    @Test
    void defeated() {
        gameController.defeated("player1");
        assertFalse(gameController.getPlayerByID("player1").isInGame());
    }

    @Test
    void rollDice() throws DieNotRolledException {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        gameController.rollDice("player1");
        gameController.getModel().getGame().lastRolled();
    }

    @Test
    void lastRolledDice() throws DieNotRolledException {
        SlaversState slaversState = new SlaversState(gameController.getModel(), gameController, adventureCard);
        gameController.setState(slaversState);

        gameController.rollDice("player1");

        gameController.rollDice("player1");
        int lastRolled = gameController.getModel().getGame().lastRolled();
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
    }

    @Test
    void shootEnemyTest() throws InvalidTileException {
        PiratesState piratesState = new PiratesState(gameController.getModel(), gameController, adventureCard);
        // ==
        try {
            assertEquals(0, piratesState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>()));
        } catch (InvalidStateException e) {
            fail("Invalid state exception: " + e.getMessage());
        } catch (InvalidTurnException e) {
            fail("Invalid turn exception: " + e.getMessage());
        } catch (InvalidCannonException e) {
            fail("Invalid cannon exception: " + e.getMessage());
        } catch (EnergyException e) {
            fail("Energy exception: " + e.getMessage());
        }
        // Ship più potente
        Cannon cannon = new Cannon();
        cannon.setPower(2);
        gameController.getModel().addToShip(cannon, gameController.getPlayerByID("player1"), 1, 2);

        Battery battery = new Battery();
        battery.setSlots(2);
        battery.setAvailableEnergy(1);

        gameController.getModel().addToShip(battery, gameController.getPlayerByID("player1"), 1, 3);

        List<Pair<Integer, Integer>> coordinatesBat = new ArrayList<>();
        coordinatesBat.add(new Pair<>(1, 3));

        List<Pair<Integer, Integer>> coordinatesCan = new ArrayList<>();
        coordinatesCan.add(new Pair<>(1, 2));
        piratesState.setCurrentPlayer("player1");

        gameController.getModel().setActiveCard(adventureCard);
        gameController.getModel().setActiveCard(adventureCard);
        gameController.setState(piratesState);
        gameController.shootEnemy("player1", coordinatesCan, coordinatesBat);

    }

    @Test
    void getScoreTest(){
        EndgameState endgameState = new EndgameState(gameController);
        gameController.setState(endgameState);
        gameController.getScore();
    }

    @Test
    void getPlayerDataTest(){
        Player player1 = gameController.getPlayerByID("player1");
        Player asked = gameController.getPlayerData("player2", "player1");

        assertEquals(player1.getUsername(), asked.getUsername());
    }

    @Test
    void takeComponentFromViewedTest(){
        AssemblingState assemblingState = new AssemblingState(gameController.getModel(), gameController);
        gameController.setState(assemblingState);
        Component comp = assemblingState.getModel().getGame().getPile().getUnviewed().getFirst();
        gameController.takeComponentFromUnviewed("player1", 0);
        gameController.addComponentToViewed("player1");

        gameController.takeComponentFromViewed("player1", 0);

        assertFalse(gameController.getModel().getGame().getPile().getViewed().contains(comp));
    }

    @Test
    void killGame(){
        gameController.killGame("player1");
        assertNull(MatchController.getInstance().getGameControllerForPlayer("player1"));
    }
}