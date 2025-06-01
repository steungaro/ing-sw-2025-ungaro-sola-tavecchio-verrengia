package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    int level = 2;
    List<String> players = new ArrayList<>();
    String gameId = "1";
    GameModel gameModel = new GameModel();

    @BeforeEach
    void setUp() {
        players.add("player1");
        players.add("player2");
    }

    private NormalShip ship, ship2;

    @BeforeEach
    void setUp2() {
        // Create a new NormalShip
        ship = new NormalShip();
        ship2 = new NormalShip();

        // Create components
        Cannon upCannon = new Cannon();
        upCannon.setOrientation(Direction.UP);
        upCannon.setPower(1);

        Cannon downCannon = new Cannon();
        downCannon.setOrientation(Direction.DOWN);
        downCannon.setPower(2);

        Engine singleEngine = new Engine();
        singleEngine.setDoublePower(false);

        Engine doubleEngine = new Engine();
        doubleEngine.setDoublePower(true);

        Battery battery = new Battery();
        battery.setSlots(2);
        battery.setAvailableEnergy(2);

        Battery battery2 = new Battery();
        battery2.setSlots(2);
        battery2.setAvailableEnergy(2);

        Cabin cabin1 = new Cabin();
        cabin1.setColor(AlienColor.NONE);

        Cabin cabin2 = new Cabin();
        cabin2.setColor(AlienColor.NONE);

        CargoHold cargoHold = new CargoHold();
        cargoHold.setSlots(3);

        CargoHold cargoHold2 = new CargoHold();
        cargoHold2.setSlots(3);


        try {
            // Add components to ship at valid positions
            ship.addComponent(upCannon, 1, 3);
            ship.addComponent(downCannon, 3, 3);
            ship.addComponent(singleEngine, 3, 2);
            ship.addComponent(doubleEngine, 3, 4);
            ship.addComponent(battery, 2, 2);
            ship.addComponent(cabin1, 2, 4);
            ship.addComponent(cargoHold, 1, 2);

            ship2.addComponent(upCannon, 1, 3);
            ship2.addComponent(downCannon, 3, 3);
            ship2.addComponent(singleEngine, 3, 2);
            ship2.addComponent(doubleEngine, 3, 4);
            ship2.addComponent(battery2, 2, 2);
            ship2.addComponent(cabin2, 2, 4);
            ship2.addComponent(cargoHold2, 1, 2);
        } catch (InvalidTileException e){
            fail("Exception should not be thrown");
        }


        // Setting the connectors
        Map<Direction, ConnectorEnum> connectorsCargoHold = new HashMap<>();
        connectorsCargoHold.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargoHold.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargoHold.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold.setConnectors(connectorsCargoHold);

        Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
        connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCargoHold2.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.D);
        cargoHold2.setConnectors(connectorsCargoHold2);

        Map<Direction, ConnectorEnum> connectorsBattery = new HashMap<>();
        connectorsBattery.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsBattery.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorsBattery.put(Direction.UP, ConnectorEnum.D);
        connectorsBattery.put(Direction.DOWN, ConnectorEnum.S);
        battery.setConnectors(connectorsBattery);

        Map<Direction, ConnectorEnum> connectorsBattery2 = new HashMap<>();
        connectorsBattery2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsBattery2.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorsBattery2.put(Direction.UP, ConnectorEnum.D);
        connectorsBattery2.put(Direction.DOWN, ConnectorEnum.S);
        battery.setConnectors(connectorsBattery2);

        Map<Direction, ConnectorEnum> connectorsSingleEngine = new HashMap<>();
        connectorsSingleEngine.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.LEFT, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.UP, ConnectorEnum.S);
        connectorsSingleEngine.put(Direction.DOWN, ConnectorEnum.ZERO);
        singleEngine.setConnectors(connectorsSingleEngine);

        Map<Direction, ConnectorEnum> connectorsDoubleEngine = new HashMap<>();
        connectorsDoubleEngine.put(Direction.RIGHT, ConnectorEnum.D);
        connectorsDoubleEngine.put(Direction.LEFT, ConnectorEnum.S);
        connectorsDoubleEngine.put(Direction.UP, ConnectorEnum.U);
        connectorsDoubleEngine.put(Direction.DOWN, ConnectorEnum.S);
        doubleEngine.setConnectors(connectorsDoubleEngine);

        Map<Direction, ConnectorEnum> connectorsCabin1 = new HashMap<>();
        connectorsCabin1.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCabin1.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCabin1.put(Direction.UP, ConnectorEnum.S);
        connectorsCabin1.put(Direction.DOWN, ConnectorEnum.S);
        cabin1.setConnectors(connectorsCabin1);

        Map<Direction, ConnectorEnum> connectorsCabin2 = new HashMap<>();
        connectorsCabin2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsCabin2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsCabin2.put(Direction.UP, ConnectorEnum.S);
        connectorsCabin2.put(Direction.DOWN, ConnectorEnum.S);
        cabin2.setConnectors(connectorsCabin2);

        Map<Direction, ConnectorEnum> connectorsDownCannon = new HashMap<>();
        connectorsDownCannon.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsDownCannon.put(Direction.LEFT, ConnectorEnum.S);
        connectorsDownCannon.put(Direction.UP, ConnectorEnum.D);
        connectorsDownCannon.put(Direction.DOWN, ConnectorEnum.ZERO);
        downCannon.setConnectors(connectorsDownCannon);

        Map<Direction, ConnectorEnum> connectorsUpCannon = new HashMap<>();
        connectorsUpCannon.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsUpCannon.put(Direction.LEFT, ConnectorEnum.S);
        connectorsUpCannon.put(Direction.UP, ConnectorEnum.ZERO);
        connectorsUpCannon.put(Direction.DOWN, ConnectorEnum.U);
        upCannon.setConnectors(connectorsUpCannon);

        Map<Direction, ConnectorEnum> connectorsStartingCabin = new HashMap<>();
        connectorsStartingCabin.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start = (StartingCabin) ship.getComponentAt(2, 3);
        start.setConnectors(connectorsStartingCabin);

        Map<Direction, ConnectorEnum> connectorsStartingCabin2 = new HashMap<>();
        connectorsStartingCabin2.put(Direction.RIGHT, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.LEFT, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.UP, ConnectorEnum.S);
        connectorsStartingCabin2.put(Direction.DOWN, ConnectorEnum.D);
        StartingCabin start2 = (StartingCabin) ship2.getComponentAt(2, 3);
        start2.setConnectors(connectorsStartingCabin2);

        Map<Direction, ConnectorEnum> connectorBattery3 = new HashMap<>();
        connectorBattery3.put(Direction.RIGHT, ConnectorEnum.S);
        connectorBattery3.put(Direction.LEFT, ConnectorEnum.ZERO);
        connectorBattery3.put(Direction.UP, ConnectorEnum.ZERO);
        connectorBattery3.put(Direction.DOWN, ConnectorEnum.ZERO);
        Battery battery3 = new Battery();
        battery3.setConnectors(connectorBattery3);
        try {
            ship2.addComponent(battery3, 1, 1);
        } catch (InvalidTileException e){
            fail("Exception should not be thrown");
        }

        try {
            ship.loadCargo(CargoColor.GREEN, cargoHold);
            ship.loadCargo(CargoColor.BLUE, cargoHold);
            ship2.loadCargo(CargoColor.GREEN, cargoHold2);
            ship2.loadCargo(CargoColor.BLUE, cargoHold2);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        ship.initAstronauts();
        ship2.initAstronauts();
    }

    @Test
    void defaultConstructor() {
        assertNotNull(gameModel);
        assertNull(gameModel.getGame());
    }

    @Test
    void setLevel(){
        gameModel.setLevel(level);
        assertEquals(level, gameModel.getLevel());
    }

    @Test
    void setGame (){
        Game game = new Game();
        gameModel.setGame(game);
        assertEquals(game, gameModel.getGame());
    }

    @Test
    void setActiveCard() {
        AdventureCard card = new AdventureCard();
        gameModel.setActiveCard(card);
        assertNotNull(gameModel.getActiveCard());
        assertEquals(card, gameModel.getActiveCard());
    }


    @Test
    void startGame() {
        gameModel.startGame(level, players, gameId);
        assertNotNull(gameModel.getGame());
        assertEquals(level, gameModel.getLevel());
        assertEquals(gameId, gameModel.getGame().getID());
        assertNotNull(gameModel.getGame().getBoard());
        for (Player player : gameModel.getGame().getPlayers()) {
            assertNotNull(player.getShip());
            assertEquals(NormalShip.class, player.getShip().getClass());
        }
        assertNotNull(gameModel.getGame().getPile());
        assertFalse(gameModel.getGame().getPile().getUnviewed().isEmpty());
        assertEquals(152, gameModel.getGame().getPile().getUnviewed().size());
    }

    @Test
    void assembling() {
        // init game
        gameModel.startGame(level, players, gameId);

        Player player = gameModel.getGame().getPlayers().getFirst();
        assertNotNull(player);

        Component component = gameModel.getGame().getPile().getUnviewed().getFirst();
        assertNotNull(component);

        try {
            gameModel.componentFromUnviewed(component);
            assertFalse(gameModel.getGame().getPile().getUnviewed().contains(component));

            gameModel.componentToViewed(component);
            assertTrue(gameModel.getGame().getPile().getViewed().contains(component));

            gameModel.RotateClockwise(component);

            gameModel.RotateCounterclockwise(component);


            gameModel.componentFromViewed(component);
            assertFalse(gameModel.getGame().getPile().getViewed().contains(component));

            gameModel.componentToBooked(component, player);
            assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

            gameModel.componentFromBooked(component, player);
            assertFalse(((NormalShip)player.getShip()).getBooked().contains(component));


            gameModel.addToShip(component, player, 1, 1);
            assertEquals(component, player.getShip().getComponentAt(1, 1));

            gameModel.stopAssembling(player, 2);
            assertEquals(3, player.getPosition());
            assertFalse(player.isLeader());

            Player secondPlayer = gameModel.getGame().getPlayers().get(1);
            gameModel.stopAssembling(secondPlayer, 1);
            assertEquals(6, secondPlayer.getPosition());
            assertTrue(secondPlayer.isLeader());

        } catch (Exception e) {
            fail("exception should not be thrown " + e.getMessage());
        }
    }

    @Test
    void calculateScore() {
        gameModel.startGame(level, players, gameId);


        gameModel.getGame().getPlayers().forEach(player -> {
            if(player.getUsername().equals("player1")) {
                player.setShip(ship);
                player.setPosition(10);
                int waste = ship.getWaste().size();
                assertEquals(0, waste);
                assertTrue(gameModel.shipValidating(player));
            }
            if(player.getUsername().equals("player2")) {
                player.setShip(ship2);
                player.setPosition(20);
                int waste2 = ship2.getWaste().size();
                assertEquals(0, waste2);
                assertFalse(gameModel.shipValidating(player));
            }
        });

        Map<Player, Integer> score = gameModel.calculateScore();
        assertEquals(13, score.get(gameModel.getGame().getPlayers().get(1)));
        assertEquals(11, score.get(gameModel.getGame().getPlayers().get(0)));

        Battery bat = (Battery) ship2.getComponentAt(1,1);
        try {
            ship2.killComponent(bat);
        } catch (ComponentNotFoundException e){
            fail("Exception should not be thrown");
        }
        Player player1 = gameModel.getGame().getPlayers().get(0);
        Player player2;
        if(player1.getUsername().equals("player1")) {
            player2 = gameModel.getGame().getPlayers().get(1);
        }
        else{
            player2 = gameModel.getGame().getPlayers().get(0);
            player1=gameModel.getGame().getPlayers().get(1);
        }

        score = gameModel.calculateScore();
        assertEquals(13, score.get(player1));
        assertEquals(10, score.get(player2));
    }

    @Test
    void testMovePlayer() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setPosition(5);

        gameModel.movePlayer(player, 3);
        assertEquals(8, player.getPosition());
    }

    @Test
    void testAddCredits() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        int initialCredits = player.getCredits();

        gameModel.addCredits(player, 10);
        assertEquals(initialCredits + 10, player.getCredits());
    }

    @Test
    void testGetCrew() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        int crew = gameModel.getCrew(player);
        assertEquals(ship.crew(), crew);
    }

    @Test
    void testGetAstronauts() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        int astronauts = gameModel.getAstronauts(player);
        assertEquals(ship.getAstronauts(), astronauts);
    }

    @Test
    void testGiveUp() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        assertTrue(player.isInGame());

        gameModel.giveUp(player);
        assertFalse(player.isInGame());
    }

    @Test
    void testFirePower() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        batteries.add((Battery)ship.getComponentAt(2, 2));

        Cannon cannon = (Cannon)ship.getComponentAt(3, 3);
        Set<Cannon> cannons = new HashSet<>();
        cannons.add(cannon);

        try {
            float power = gameModel.FirePower(player, cannons, batteries);
            assertEquals(ship.firePower(cannons, batteries.size()), power);
            assertEquals(1, ((Battery)ship.getComponentAt(2, 2)).getAvailableEnergy());
        } catch (Exception e) {
            fail("Exceptions should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testEnginePower() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        batteries.add((Battery)ship.getComponentAt(2, 2));

        try {
            int power = gameModel.EnginePower(player, 1, batteries);
            assertEquals(ship.enginePower(1), power);
            assertEquals(1, ((Battery)ship.getComponentAt(2, 2)).getAvailableEnergy());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetInGamePlayers() {
        gameModel.startGame(level, players, gameId);
        Player player1 = gameModel.getGame().getPlayers().getFirst();
        Player player2 = gameModel.getGame().getPlayers().get(1);

        List<Player> inGamePlayers = gameModel.getInGamePlayers();
        assertEquals(2, inGamePlayers.size());

        gameModel.giveUp(player1);
        inGamePlayers = gameModel.getInGamePlayers();
        assertEquals(1, inGamePlayers.size());
        assertEquals(player2, inGamePlayers.getFirst());
    }

    @Test
    void testAutoValidation() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        assertDoesNotThrow(() -> gameModel.autoValidation(player));
    }

    @Test
    void testHeavyMeteorCannon() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Projectile meteor = new Projectile();
        meteor.setFireType(FireType.HEAVY_METEOR);
        meteor.setDirection(Direction.UP);

        List<Cannon> cannons = gameModel.heavyMeteorCannon(player, 7, meteor);

        assertFalse(cannons.isEmpty());
    }

    @Test
    void testMoveCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        CargoHold cargoHold1 = (CargoHold) ship.getComponentAt(1, 2);
        CargoHold cargoHold2;

        try {
            cargoHold2 = new CargoHold();
            cargoHold2.setSlots(3);
            Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
            connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.UP, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.S);
            cargoHold2.setConnectors(connectorsCargoHold2);
            ship.addComponent(cargoHold2, 1, 4);

            assertNotNull(ship.getCargo().get(CargoColor.GREEN));
            assertEquals(1, ship.getCargo().get(CargoColor.GREEN));

            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold1, cargoHold2);
            assertEquals(1, ship.getCargo().get(CargoColor.GREEN));

            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold2, null);
            assertEquals(0, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

        } catch (Exception e) {
            fail("exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testAddCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);


        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);

        try {

            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold, null);
            gameModel.MoveCargo(player, CargoColor.BLUE, cargoHold, null);


            gameModel.addCargo(player, CargoColor.YELLOW, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));

            gameModel.addCargo(player, CargoColor.GREEN, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

            gameModel.addCargo(player, CargoColor.BLUE, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));


            assertThrows(CargoFullException.class, () ->
                    gameModel.addCargo(player, CargoColor.BLUE, cargoHold)
            );
        } catch (Exception e) {
            fail("exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testMoveCargoExceptions() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);


        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);


        assertThrows(InvalidCargoException.class, () -> gameModel.MoveCargo(player, CargoColor.RED, cargoHold, null));

        try {

            CargoHold smallCargoHold = new CargoHold();
            smallCargoHold.setSlots(1);
            Map<Direction, ConnectorEnum> connectors = new HashMap<>();
            connectors.put(Direction.RIGHT, ConnectorEnum.S);
            connectors.put(Direction.LEFT, ConnectorEnum.S);
            connectors.put(Direction.UP, ConnectorEnum.S);
            connectors.put(Direction.DOWN, ConnectorEnum.S);
            smallCargoHold.setConnectors(connectors);
            ship.addComponent(smallCargoHold, 3, 1);


            gameModel.addCargo(player, CargoColor.BLUE, smallCargoHold);


            assertThrows(CargoFullException.class, () -> gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold, smallCargoHold));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testViewDeck() {
        gameModel.startGame(level, players, gameId);
        try {

            List<AdventureCard> cards = gameModel.viewDeck(1);
            assertNotNull(cards);
            assertFalse(cards.isEmpty());

            assertThrows(InvalidIndexException.class, () -> gameModel.viewDeck(4));
        } catch (InvalidIndexException e) {
            fail("InvalidIndexException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testRemoveComponent() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        try {

            gameModel.removeComponent(1, 3, player);


            assertNull(player.getShip().getComponentAt(1, 3));
        } catch (ComponentNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testSetAlien() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);

        try {

            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);


            assertEquals(AlienColor.PURPLE, cabin.getAlienColor());


            assertThrows(InvalidAlienPlacement.class, () ->
                    gameModel.setAlien(AlienColor.BROWN, cabin, player)
            );
        } catch (InvalidAlienPlacement e) {
            fail("InvalidAlienPlacement should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testAddPieces() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Component component = new Battery();
        try {
            gameModel.componentToBooked(component, player);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

        gameModel.addPieces(player);

        assertTrue(player.getShip().getWaste().contains(component));

        assertNotEquals(0, player.getShip().getAstronauts());
    }

    @Test
    void testCreateDeck() {
        gameModel.startGame(level, players, gameId);

        gameModel.createDeck();

        assertFalse(gameModel.getGame().getBoard().getDeck().isEmpty());
    }

    @Test
    void testDrawCard() {
        gameModel.startGame(level, players, gameId);
        Player player1 = gameModel.getGame().getPlayers().getFirst();
        Player player2 = gameModel.getGame().getPlayers().get(1);

        player1.setPosition(20);
        player2.setPosition(2);

        try {

            AdventureCard card = gameModel.drawCard();

            assertNotNull(card);
            assertEquals(card, gameModel.getActiveCard());

            if (player1.getPosition() - player2.getPosition() >= gameModel.getGame().getBoard().getSpaces()) {
                assertFalse(player2.isInGame());
            }
        } catch (EmptyDeckException e) {
            fail("EmptyDeckException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testLoseCrew() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
        try {
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);

            assertTrue(cabin.getAlien());

            List<Cabin> cabins = new ArrayList<>();
            cabins.add(cabin);

            gameModel.loseCrew(player, cabins);

            assertEquals(AlienColor.NONE, cabin.getAlienColor());

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testUseShield() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        Battery battery = (Battery) ship.getComponentAt(2, 2);
        int initialEnergy = battery.getAvailableEnergy();

        try {

            gameModel.useShield(player, battery);

            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            while (battery.getAvailableEnergy() > 0) {
                gameModel.useShield(player, battery);
            }

            assertThrows(EnergyException.class, () -> gameModel.useShield(player, battery));
        } catch (EnergyException e) {
            fail("EnergyException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testFire() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        try {

            Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);


            Projectile heavyProjectile = new Projectile();
            heavyProjectile.setDirection(Direction.UP);
            heavyProjectile.setFireType(FireType.HEAVY_FIRE);


            gameModel.Fire(player, 8, heavyProjectile);
            assertFalse(cabin.getAlien());

        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testRemoveEnergy() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        List<Battery> batteries = new ArrayList<>();
        Battery battery = (Battery) ship.getComponentAt(2, 2);
        batteries.add(battery);

        int initialEnergy = battery.getAvailableEnergy();

        try {

            gameModel.removeEnergy(player, batteries);

            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            while (battery.getAvailableEnergy() > 0) {
                gameModel.removeEnergy(player, batteries);
            }

            assertThrows(EnergyException.class, () ->
                    gameModel.removeEnergy(player, batteries));
        } catch (EnergyException e) {
            fail("EnergyException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testHourglass1 () throws InterruptedException {
        gameModel.startGame(level, players, gameId);
        NormalBoard board= (NormalBoard) gameModel.getGame().getBoard();
        board.hourglass.setPeriod(5);
        gameModel.initCountdown();
        assertEquals(5, gameModel.getRemainingTime());
        Thread.sleep(3000);
        assertEquals(2, gameModel.getRemainingTime());
        assertEquals(12, gameModel.getTotalRemainingTime());
        Thread.sleep(2000);
        try {
            gameModel.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(5, gameModel.getRemainingTime());
    }

    @Test
    void testHourglass2 () throws InterruptedException {
        gameModel.startGame(level, players, gameId);
        NormalBoard board= (NormalBoard) gameModel.getGame().getBoard();
        board.hourglass.setPeriod(5);
        gameModel.initCountdown();
        assertThrows(HourglassException.class, () -> gameModel.turnHourglass());
        Thread.sleep(5100);
        try {
            gameModel.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown"+ e.getMessage());
        }
        Thread.sleep(5100);
        try {
            gameModel.turnHourglass();
        } catch (HourglassException e) {
            fail("Exception should not be thrown");
        }
        Thread.sleep(5100);
        assertEquals(0, gameModel.getRemainingTime());
        assertEquals(2, gameModel.getTurnedHourglass());
        assertThrows(HourglassException.class, () -> gameModel.turnHourglass());
    }
}