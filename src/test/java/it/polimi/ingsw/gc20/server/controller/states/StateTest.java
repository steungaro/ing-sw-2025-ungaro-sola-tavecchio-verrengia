package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;

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
        Cabin cabin = new Cabin();
        cabin.setColor(AlienColor.BROWN);
        Map<Direction, ConnectorEnum> connectors = new HashMap<>();
        connectors.put(Direction.UP, ConnectorEnum.U);
        connectors.put(Direction.DOWN, ConnectorEnum.D);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.RIGHT, ConnectorEnum.D);
        cabin.setConnectors(connectors);

        assemblingState.placeComponent(gameController.getPlayerByID("player1"), new Pair<>(2, 2));

        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));

        Pair<Integer, Integer> cabinCoord = new Pair<>(2, 2);

        assertThrows(InvalidAlienPlacement.class, () -> validatingShipState.addAlien(gameController.getPlayerByID("player1"), AlienColor.PURPLE, cabinCoord));
        try {
            validatingShipState.addAlien(gameController.getPlayerByID("player1"), AlienColor.BROWN, cabinCoord);
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
    void loadCargo() throws InvalidTurnException, EmptyDeckException, InvalidCannonException, EnergyException, CargoException, CargoNotLoadable, CargoFullException {
        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        Pair<Integer, Integer> coordinates = new Pair<>(1, 2);
        try {
            gameController.getModel().addToShip(cargoHold, gameController.getPlayerByID("player1"), coordinates.getValue0(), coordinates.getValue1());
        } catch (InvalidTileException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.GREEN);
        adventureCard.setReward(cargo);

        // CargoState: AbandonedStationState, Smugglers, PlanetsState,
        adventureCard.setPlayed(false);
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, adventureCard);
        Player player = gameController.getPlayerByID("player1");
        CargoColor loaded = CargoColor.BLUE;
        gameController.setState(abandonedStationState);
        model.setActiveCard(adventureCard);
        try {
            abandonedStationState.loadCargo(player, loaded, coordinates);
        }catch (IllegalArgumentException | CargoException | InvalidTurnException | CargoNotLoadable | CargoFullException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(1, cargoHold.getCargoHeld().get(CargoColor.BLUE));

        adventureCard.setPlayed(false);
        adventureCard.setFirePower(-1);
        cargo.add(CargoColor.BLUE);
        adventureCard.setReward(cargo);
        SmugglersState smugglersState = new SmugglersState(model, gameController, adventureCard);
        gameController.setState(smugglersState);
        model.setActiveCard(adventureCard);

        smugglersState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        smugglersState.acceptCard(gameController.getPlayerByID("player1"));

        smugglersState.loadCargo(player, loaded, coordinates);

        assertEquals(2, cargoHold.getCargoHeld().get(CargoColor.BLUE));

        List<Planet> planets = new ArrayList<>();
        Planet planet = new Planet();
        planets.add(planet);
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.GREEN);
        planet.setReward(cargo);
        adventureCard.setPlanets(planets);

        adventureCard.setPlayed(false);
        PlanetsState planetsState = new PlanetsState(model, gameController, adventureCard);
        gameController.setState(planetsState);
        model.setActiveCard(adventureCard);

        // Prima atterrare sul pianeta
        try {
            planetsState.landOnPlanet(player, 0);
        } catch (InvalidParameterException | InvalidTurnException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            planetsState.loadCargo(player, loaded, coordinates);
        } catch (IllegalArgumentException | CargoException | InvalidTurnException | CargoNotLoadable | CargoFullException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(3, cargoHold.getCargoHeld().get(CargoColor.BLUE));
    }

    @Test
    void unloadCargo() throws InvalidTileException, CargoNotLoadable, CargoFullException, InvalidTurnException, CargoException, InvalidCargoException, EmptyDeckException, InvalidCannonException, EnergyException, InvalidEngineException {
        // Classi da testare: AbandonedStationState, SmugglersState, PlanetsState, CombactZone1, CargoState
        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        cargoHold.loadCargo(CargoColor.BLUE);
        model.addToShip(cargoHold, gameController.getPlayerByID("player1"), 1, 2);

        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, adventureCard);
        CargoColor unloaded = CargoColor.BLUE;
        Pair<Integer, Integer> coordinates = new Pair<>(1, 2);
        gameController.setState(abandonedStationState);
        model.setActiveCard(adventureCard);

        abandonedStationState.unloadCargo(gameController.getPlayerByID("player1"), unloaded, coordinates);
        assertEquals(0, cargoHold.getCargoHeld().get(CargoColor.BLUE));
        adventureCard.setFirePower(-1);

        // SmugglersState
        SmugglersState smugglersState = new SmugglersState(model, gameController, adventureCard);
        gameController.setState(smugglersState);
        model.setActiveCard(adventureCard);
        smugglersState.setCurrentPlayer("player1");
        smugglersState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        smugglersState.setCurrentPlayer("player1");
        smugglersState.acceptCard(gameController.getPlayerByID("player1"));
        cargoHold.loadCargo(CargoColor.BLUE);
        smugglersState.unloadCargo(gameController.getPlayerByID("player1"), unloaded, coordinates);
        assertEquals(0, cargoHold.getCargoHeld().get(CargoColor.BLUE));

        // PlanetsState
        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        Planet planet = new Planet();
        List<Planet> planets = new ArrayList<>();
        planets.add(planet);
        adventureCard.setPlanets(planets);

        adventureCard.setReward(cargo);
        PlanetsState planetsState = new PlanetsState(model, gameController, adventureCard);
        gameController.setState(planetsState);
        model.setActiveCard(adventureCard);
        planetsState.setCurrentPlayer("player1");
        cargoHold.loadCargo(CargoColor.BLUE);
        planetsState.landOnPlanet(gameController.getPlayerByID("player1"), 0);
        planetsState.unloadCargo(gameController.getPlayerByID("player1"), unloaded, coordinates);
        assertEquals(0, cargoHold.getCargoHeld().get(CargoColor.BLUE));

        // CombactZone1
        cargoHold.loadCargo(CargoColor.BLUE);
        CombatZone1State combactZone1 = new CombatZone1State(model, gameController, adventureCard);
        gameController.setState(combactZone1);
        combactZone1.activateEngines(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player3"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player4"), new ArrayList<>(), new ArrayList<>());
        model.setActiveCard(adventureCard);
        combactZone1.setCurrentPlayer("player1");
        combactZone1.unloadCargo(gameController.getPlayerByID("player1"), unloaded, coordinates);

    }

    @Test
    void moveCargo() throws InvalidTileException, CargoNotLoadable, CargoFullException, InvalidTurnException, CargoException, InvalidCargoException, InvalidEngineException, EnergyException {
        // Classi da testare: AbandonedStationState, PlanetsState, CombatZone1

        // AbandonedStationState
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, adventureCard);
        Pair<Integer, Integer> coordinatesFrom = new Pair<>(1, 2);
        Pair<Integer, Integer> coordinatesTo = new Pair<>(1, 3);
        CargoColor loaded = CargoColor.BLUE;
        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        cargoHold.loadCargo(loaded);
        model.addToShip(cargoHold, gameController.getPlayerByID("player1"), coordinatesFrom.getValue0(), coordinatesFrom.getValue1());
        gameController.setState(abandonedStationState);
        model.setActiveCard(adventureCard);

        CargoHold cargoHold2 = new CargoHold();
        cargoHold2.setSlots(3);
        cargoHold2.loadCargo(loaded);
        model.addToShip(cargoHold2, gameController.getPlayerByID("player1"), coordinatesTo.getValue0(), coordinatesTo.getValue1());

        abandonedStationState.moveCargo(gameController.getPlayerByID("player1"), loaded, coordinatesFrom, coordinatesTo);
        assertEquals(0, cargoHold.getCargoHeld().get(loaded));
        assertEquals(2, cargoHold2.getCargoHeld().get(loaded));

        // PlanetsState
        Planet planet = new Planet();
        List<Planet> planets = new ArrayList<>();
        planets.add(planet);
        adventureCard.setPlanets(planets);
        PlanetsState planetsState = new PlanetsState(model, gameController, adventureCard);
        gameController.setState(planetsState);
        model.setActiveCard(adventureCard);

        planetsState.landOnPlanet(gameController.getPlayerByID("player1"), 0);
        planetsState.moveCargo(gameController.getPlayerByID("player1"), loaded, coordinatesTo, coordinatesFrom);

        assertEquals(1, cargoHold.getCargoHeld().get(loaded));
        assertEquals(1, cargoHold2.getCargoHeld().get(loaded));

        CombatZone1State combactZone1 = new CombatZone1State(model, gameController, adventureCard);
        gameController.setState(combactZone1);
        model.setActiveCard(adventureCard);
        combactZone1.activateEngines(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player3"), new ArrayList<>(), new ArrayList<>());
        combactZone1.activateEngines(gameController.getPlayerByID("player4"), new ArrayList<>(), new ArrayList<>());
        combactZone1.setCurrentPlayer("player1");

        combactZone1.moveCargo(gameController.getPlayerByID("player1"), loaded, coordinatesFrom, coordinatesTo);
        assertEquals(0, cargoHold.getCargoHeld().get(loaded));
        assertEquals(2, cargoHold2.getCargoHeld().get(loaded));
    }

    @Test
    void loseEnergy() throws InvalidTileException, InvalidTurnException, EnergyException, CargoNotLoadable, CargoFullException, InvalidCannonException, InvalidEngineException {
        // Classe da testare: , CargoState, AbandonedStationState, CombactZone1
        // Smugglers
        adventureCard.setFirePower(-1);
        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.BLUE);
        adventureCard.setReward(new ArrayList<>(cargo));
        SmugglersState smugglersState = new SmugglersState(model, gameController, adventureCard);
        gameController.setState(smugglersState);

        assertThrows(InvalidTurnException.class, () -> smugglersState.loseEnergy(gameController.getPlayerByID("player2"), new Pair<>(1, 2)));

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);
        model.addToShip(cargoHold, gameController.getPlayerByID("player1"), 1, 2);

        gameController.getPlayerByID("player1").getShip().loadCargo(CargoColor.BLUE, cargoHold);

        assertThrows(IllegalStateException.class, () -> smugglersState.loseEnergy(gameController.getPlayerByID("player1"), new Pair<>(1, 2)));

        gameController.unloadCargo("player1", CargoColor.BLUE, new Pair<>(1, 2));

        Battery battery = new Battery();
        battery.setSlots(2);
        battery.setAvailableEnergy(1);

        gameController.getModel().setActiveCard(adventureCard);
        smugglersState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        model.addToShip(battery, gameController.getPlayerByID("player1"), 1, 3);
        Pair<Integer, Integer> coordinates = new Pair<>(1, 3);
        smugglersState.loseEnergy(gameController.getPlayerByID("player1"), coordinates);

        assertEquals(0, battery.getAvailableEnergy());

        // CargoState
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, adventureCard);
        gameController.setState(abandonedStationState);


        assertThrows(InvalidTurnException.class, () -> abandonedStationState.loseEnergy(gameController.getPlayerByID("player2"), new Pair<>(1, 2)));
        gameController.getActiveCard().setPlayed(false);
        gameController.loadCargo("player1", CargoColor.BLUE, new Pair<>(1, 2));

        assertThrows(IllegalStateException.class, () -> abandonedStationState.loseEnergy(gameController.getPlayerByID("player1"), new Pair<>(1, 3)));
        gameController.unloadCargo("player1", CargoColor.BLUE, new Pair<>(1, 2));

        battery.setAvailableEnergy(1);
        smugglersState.loseEnergy(gameController.getPlayerByID("player1"), coordinates);

        assertEquals(0, battery.getAvailableEnergy());

        // CombactZone1
        CombatZone1State combatZone1State = new CombatZone1State(model, gameController, adventureCard);
        gameController.setState(combatZone1State);


        assertThrows(InvalidTurnException.class, () -> combatZone1State.loseEnergy(gameController.getPlayerByID("player2"), new Pair<>(1, 2)));
        gameController.getActiveCard().setPlayed(false);

        assertThrows(IllegalStateException.class, () -> combatZone1State.loseEnergy(gameController.getPlayerByID("player1"), new Pair<>(1, 3)));
        gameController.setState(abandonedStationState);
        //gameController.unloadCargo("player1", CargoColor.BLUE, new Pair<>(1, 2));

        battery.setAvailableEnergy(1);
        gameController.getPlayerByID("player1").getShip().addBatteries(2);
        combatZone1State.activateEngines(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        combatZone1State.activateEngines(gameController.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        combatZone1State.activateEngines(gameController.getPlayerByID("player3"), new ArrayList<>(), new ArrayList<>());
        combatZone1State.activateEngines(gameController.getPlayerByID("player4"), new ArrayList<>(), new ArrayList<>());
        combatZone1State.setCurrentPlayer("player1");
        combatZone1State.loseEnergy(gameController.getPlayerByID("player1"), coordinates);

        assertEquals(0, battery.getAvailableEnergy());

        // TODO -> combatZone1State
    }

    @Test
    void testLandOnPlanet() throws InvalidTurnException {
        // Classe da testare: PlanetsState
        List<Planet> planets = new ArrayList<>();
        Planet planet = new Planet();
        List<CargoColor> cargo = new ArrayList<>();
        cargo.add(CargoColor.BLUE);
        cargo.add(CargoColor.GREEN);
        planet.setReward(cargo);
        planets.add(planet);
        adventureCard.setPlanets(planets);

        PlanetsState planetsState = new PlanetsState(model, gameController, adventureCard);

        assertThrows( InvalidTurnException.class, () -> planetsState.landOnPlanet(gameController.getPlayerByID("player2"), 0));

        planetsState.landOnPlanet(gameController.getPlayerByID("player1"), 0);

        assertThrows(IllegalStateException.class, () -> planetsState.landOnPlanet(gameController.getPlayerByID("player1"), 0));
    }


    @Test
    void acceptCard() throws InvalidTurnException, InvalidCannonException, EnergyException {
        // Classe da testare: AbandonedStationState, PirateState, SlaversState, SmugglersState
        // AbandonedStationState
        adventureCard.setCrew(-1);
        adventureCard.setFirePower(-1);
        AbandonedStationState abandonedStationState = new AbandonedStationState(model, gameController, adventureCard);
        gameController.setState(abandonedStationState);
        gameController.getModel().setActiveCard(adventureCard);
        assertThrows(InvalidTurnException.class, () -> abandonedStationState.acceptCard(gameController.getPlayerByID("player2")));
        abandonedStationState.acceptCard(gameController.getPlayerByID("player1"));

        adventureCard.setCrew(1);
        AbandonedStationState abandonedStationState2 = new AbandonedStationState(model, gameController, adventureCard);
        gameController.setState(abandonedStationState2);
        gameController.getModel().setActiveCard(adventureCard);
        assertThrows(IllegalStateException.class, () -> abandonedStationState2.acceptCard(gameController.getPlayerByID("player1")));

        // PirateState
        PiratesState piratesState = new PiratesState(model, gameController, adventureCard);
        gameController.setState(piratesState);
        gameController.getModel().setActiveCard(adventureCard);
        assertThrows(InvalidTurnException.class, () -> piratesState.acceptCard(gameController.getPlayerByID("player2")));
        piratesState.acceptCard(gameController.getPlayerByID("player1"));

        // SlaversState
        SlaversState slaversState = new SlaversState(model, gameController, adventureCard);
        gameController.setState(slaversState);
        gameController.getModel().setActiveCard(adventureCard);
        assertThrows(InvalidTurnException.class, () -> slaversState.acceptCard(gameController.getPlayerByID("player3")));
        assertThrows(IllegalStateException.class, () -> slaversState.acceptCard(gameController.getPlayerByID("player2")));
        slaversState.shootEnemy(gameController.getPlayerByID("player2"), new ArrayList<>(), new ArrayList<>());
        slaversState.acceptCard(gameController.getPlayerByID("player2"));
    }

    @Test
    void loseCrew() {
        // Classi da testare: AbandonedShipState, CombactZone0State, SlaversState
        // TODO: CombactZone1, ComactZone0
    }

    @Test
    void endMove() {
        // Classi da testare: AbandonedShipState, AbandoneStationState, CombatZone1State, PiratesState, PlatesState, SmugglersState
        // TODO: CombactZone1, ComactZone0
    }

    @Test
    void activateEngines() {
        // Classe da testare: OpenSpaceState, CombactZone1State, CombactZone0State
        // TODO: CombactZone1, ComactZone0
    }

    @Test
    void activateShield() {
        // Classe da testare: CombactZone1, CombactZone0, MeteorSwarm, PirateState
        // TODO: CombactZone1, ComactZone0
    }

    @Test
    void shootEnemy() throws InvalidTileException, InvalidTurnException, EmptyDeckException, InvalidCannonException, EnergyException {
        // Classi da testare: SmugglersState, SlaversState, PirateState
        PiratesState piratesState = new PiratesState(model, gameController, adventureCard);
            // ==
            assertEquals(0, piratesState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>()));
            // Ship più potente
        Cannon cannon = new Cannon();
        cannon.setPower(2);
        model.addToShip(cannon, gameController.getPlayerByID("player1"), 1, 2);

        Battery battery = new Battery();
        battery.setSlots(2);
        battery.setAvailableEnergy(1);

        model.addToShip(battery, gameController.getPlayerByID("player1"), 1, 3);

        List<Pair<Integer, Integer>> coordinatesBat = new ArrayList<>();
        coordinatesBat.add(new Pair<>(1, 3));

        List<Pair<Integer, Integer>> coordinatesCan = new ArrayList<>();
        coordinatesCan.add(new Pair<>(1, 2));
        piratesState.setCurrentPlayer("player1");

        model.setActiveCard(adventureCard);
        assertEquals(1, piratesState.shootEnemy(gameController.getPlayerByID("player1"), coordinatesCan, coordinatesBat));

        // --------------------------

        SlaversState slaversState = new SlaversState(model, gameController, adventureCard);
        // ==
        assertEquals(0, slaversState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>()));
        // Ship più potente
        List<Pair<Integer, Integer>> coordinatesBat2 = new ArrayList<>();
        coordinatesBat2.add(new Pair<>(1, 4));
        List<Pair<Integer, Integer>> coordinatesCan2 = new ArrayList<>();
        coordinatesCan2.add(new Pair<>(1, 2));
        slaversState.setCurrentPlayer("player1");
        model.setActiveCard(adventureCard);

        Battery battery2 = new Battery();
        battery2.setSlots(2);
        battery2.setAvailableEnergy(1);
        model.addToShip(battery2, gameController.getPlayerByID("player1"), 1, 4);
        assertEquals(1, slaversState.shootEnemy(gameController.getPlayerByID("player1"), coordinatesCan2, coordinatesBat2));

        // --------------------------
        SmugglersState smugglersState = new SmugglersState(model, gameController, adventureCard);
        // ==
        assertEquals(0, smugglersState.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>()));
        // Ship più potente
        Battery battery3 = new Battery();
        battery3.setSlots(2);
        battery3.setAvailableEnergy(1);
        model.addToShip(battery3, gameController.getPlayerByID("player1"), 2, 1);


        List<Pair<Integer, Integer>> coordinatesBat3 = new ArrayList<>();
        coordinatesBat3.add(new Pair<>(2, 1));
        List<Pair<Integer, Integer>> coordinatesCan3 = new ArrayList<>();
        coordinatesCan3.add(new Pair<>(1, 2));
        smugglersState.setCurrentPlayer("player1");
        model.setActiveCard(adventureCard);
        assertEquals(1, smugglersState.shootEnemy(gameController.getPlayerByID("player1"), coordinatesCan3, coordinatesBat3));

    }

    @Test
    void activateCannons() throws InvalidTileException, InvalidTurnException, EmptyDeckException, InvalidCannonException, EnergyException, InvalidShipException {
        // Classe da testare: CombactZone1, MeteorSwarm, CombactZone0
        // CombatZone0State
        CombatZone0State combatZone0State = new CombatZone0State(model, gameController, adventureCard);
        combatZone0State.activateCannons(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        // NOTE -> se uguagliamo giocatore scelto a caso

        // CombatZone1State
        CombatZone1State combatZone1State = new CombatZone1State(model, gameController, adventureCard);
        combatZone1State.setCurrentPlayer("player1");
        combatZone1State.activateCannons(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());

        // MeteorSwarmState
        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(model, gameController, adventureCard);
        meteorSwarmState.setCurrentPlayer("player1");
        meteorSwarmState.activateCannons(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());

        // Maybe can be tested in other ways
    }

    @Test
    void allAssembled() {
        // Classe da testare: AssemblingState
        AssemblingState assemblingState = new AssemblingState(gameController.getModel());
        assertFalse(assemblingState.allAssembled());
        try {
            assemblingState.stopAssembling(gameController.getPlayerByID("player1"), 1);
            assemblingState.stopAssembling(gameController.getPlayerByID("player2"), 2);
            assemblingState.stopAssembling(gameController.getPlayerByID("player3"), 3);
            assemblingState.stopAssembling(gameController.getPlayerByID("player4"), 4);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(assemblingState.allAssembled());
    }

    @Test
    void allShipsReady() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        assertFalse(validatingShipState.allShipsReadyToFly());

        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player2"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player3"));
        validatingShipState.isShipValid(gameController.getPlayerByID("player4"));

        validatingShipState.readyToFly(gameController.getPlayerByID("player1"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player2"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player3"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player4"));

        assertTrue(validatingShipState.allShipsReadyToFly());
        // Classe da testare: ValidatingShipState
    }

    @Test
    void readyToFly() {
        ValidatingShipState validatingShipState = new ValidatingShipState(gameController.getModel());
        assertThrows(IllegalArgumentException.class, () -> validatingShipState.readyToFly(gameController.getPlayerByID("player1")));
        validatingShipState.isShipValid(gameController.getPlayerByID("player1"));
        validatingShipState.readyToFly(gameController.getPlayerByID("player1"));
        // Classe da testare: ValidatingShipState
    }

    @Test
    void automaticAction() {
        // Classe da testare: Epidemic, Stardust, ComactZone1, ComactZone0
        // TODO: CombactZone1, ComactZone0
    }

    @Test
    void resume() {
        // Classi da testare: PausedState
        CombatZone0State combatZone0State = new CombatZone0State(model, gameController, adventureCard);
        gameController.setState(combatZone0State);

        PausedState pausedState = new PausedState(combatZone0State, gameController.getModel(), gameController);
        gameController.setState(pausedState);

        pausedState.resume();

        assertEquals(combatZone0State.toString(), gameController.getState().toString());
    }

    @Test
    void rollDice() throws InvalidTurnException, InvalidCannonException, EnergyException {
        // Classi da testare: CombactZone1, CombactZone0, MeteorSwarmState, PirateState, PlayingState
        // TODO: CombatZone1, CombactZone0

        // MeteorSwarmState
        adventureCard.setFirePower(10);
        MeteorSwarmState meteorSwarmState = new MeteorSwarmState(model, gameController, adventureCard);
        PiratesState piratesState = new PiratesState(model, gameController, adventureCard);
        assertThrows(InvalidTurnException.class, () -> meteorSwarmState.rollDice(gameController.getPlayerByID("player2")));
        assertThrows(IllegalStateException.class, () -> meteorSwarmState.rollDice(gameController.getPlayerByID("player1")));
        Projectile projectile = new Projectile();
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(projectile);
        adventureCard.setProjectiles(projectiles);
        MeteorSwarmState meteorSwarmState2 = new MeteorSwarmState(model, gameController, adventureCard);
        meteorSwarmState2.setCurrentPlayer("player1");

        try {
            int result = meteorSwarmState2.rollDice(gameController.getPlayerByID("player1"));
            assertTrue(result >= 1 && result <= 12);
        } catch (InvalidTurnException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // PirateState
        assertThrows(InvalidTurnException.class, () -> piratesState.rollDice(gameController.getPlayerByID("player2")));
        assertThrows(IllegalStateException.class, () -> piratesState.rollDice(gameController.getPlayerByID("player1")));
        PiratesState piratesState2 = new PiratesState(model, gameController, adventureCard);
        piratesState2.setCurrentPlayer("player1");
        piratesState2.shootEnemy(gameController.getPlayerByID("player1"), new ArrayList<>(), new ArrayList<>());
        try {
            int result = piratesState2.rollDice(gameController.getPlayerByID("player1"));
            assertTrue(result >= 1 && result <= 12);
        } catch ( InvalidShipException | DieNotRolledException | InvalidTurnException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // PlayingState
        EpidemicState epidemicState = new EpidemicState(model, gameController, adventureCard);
        epidemicState.setCurrentPlayer("player1");
        assertThrows(InvalidTurnException.class, () -> epidemicState.rollDice(gameController.getPlayerByID("player2")));
        try{
            int result = epidemicState.rollDice(gameController.getPlayerByID("player1"));
            assertTrue(result >= 1 && result <= 12);
        } catch (InvalidShipException | DieNotRolledException | InvalidTurnException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}