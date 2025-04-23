package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.FireType;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
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
    void defaultContrsuctor() {
        assertNotNull(gameModel);
        assertNull(gameModel.getGame());
    }

    @Test
    void setlevel(){
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
        assertInstanceOf(Battery.class, gameModel.getGame().getPile().getUnviewed().getFirst());
        assertEquals(152, gameModel.getGame().getPile().getUnviewed().size());
    }

    @Test
    void assembling() {
        // Inizializza il gioco
        gameModel.startGame(level, players, gameId);

        // Ottieni il primo player e verifica che sia stato creato correttamente
        Player player = gameModel.getGame().getPlayers().getFirst();
        assertNotNull(player);

        // Ottieni un componente dalla lista unviewed
        Component component = gameModel.getGame().getPile().getUnviewed().getFirst();
        assertNotNull(component);

        try {
            // Test componentFromUnviewed
            gameModel.componentFromUnviewed(component);
            assertFalse(gameModel.getGame().getPile().getUnviewed().contains(component));

            // Test componentToViewed
            gameModel.componentToViewed(component);
            assertTrue(gameModel.getGame().getPile().getViewed().contains(component));

            // Test RotateClockwise
            gameModel.RotateClockwise(component);

            gameModel.RotateCounterclockwise(component);

            // Test componentFromViewed
            gameModel.componentFromViewed(component);
            assertFalse(gameModel.getGame().getPile().getViewed().contains(component));

            // Test componentToBooked (solo per NormalShip)
            gameModel.componentToBooked(component, player);
            assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

            // Test componentFromBooked
            gameModel.componentFromBooked(component, player);
            assertFalse(((NormalShip)player.getShip()).getBooked().contains(component));

            // Test addToShip - aggiungi il componente in una posizione libera (ad esempio 1,1)
            gameModel.addToShip(component, player, 1, 1);
            assertEquals(component, player.getShip().getComponentAt(1, 1));

            // Test stopAssembling - posizione 2
            gameModel.stopAssembling(player, 2);
            assertEquals(2, player.getPosition());
            assertFalse(player.isLeader());

            // Testa anche la posizione leader (1)
            Player secondPlayer = gameModel.getGame().getPlayers().get(1);
            gameModel.stopAssembling(secondPlayer, 1);
            assertEquals(1, secondPlayer.getPosition());
            assertTrue(secondPlayer.isLeader());

        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
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

        // Ottieni batterie e cannoni dalla nave
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
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
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
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
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

        // Testa che l'autovalidation non lanci eccezioni
        assertDoesNotThrow(() -> gameModel.autoValidation(player));
    }

    @Test
    void testHeavyMeteorCannon() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Crea un proiettile di tipo heavy meteor
        Projectile meteor = new Projectile();
        meteor.setFireType(FireType.HEAVY_METEOR);
        meteor.setDirection(Direction.UP);

        List<Cannon> cannons = gameModel.heavyMeteorCannon(player, 7, meteor);

        // Verifica che ci sia almeno un cannone puntato nella direzione del meteorite
        assertFalse(cannons.isEmpty());
    }

    @Test
    void testMoveCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni i CargoHold dalla nave
        CargoHold cargoHold1 = (CargoHold) ship.getComponentAt(1, 2);
        CargoHold cargoHold2 = null;

        // Cerca un altro CargoHold sulla nave o creane uno
        try {
            // Aggiungi un nuovo CargoHold alla nave
            cargoHold2 = new CargoHold();
            cargoHold2.setSlots(3);
            Map<Direction, ConnectorEnum> connectorsCargoHold2 = new HashMap<>();
            connectorsCargoHold2.put(Direction.RIGHT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.LEFT, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.UP, ConnectorEnum.S);
            connectorsCargoHold2.put(Direction.DOWN, ConnectorEnum.S);
            cargoHold2.setConnectors(connectorsCargoHold2);
            ship.addComponent(cargoHold2, 1, 4);

            // Verifica presenza di cargo (già caricati nel setUp)
            assertNotNull(ship.getCargo().get(CargoColor.GREEN));
            assertEquals(1, ship.getCargo().get(CargoColor.GREEN));

            // Test MoveCargo tra due CargoHold
            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold1, cargoHold2);
            assertEquals(2, ship.getCargo().get(CargoColor.GREEN));

            // Test rimozione cargo
            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold2, null);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testAddCargo() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni un CargoHold
        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);

        try {
            // Rimuovi i cargo esistenti per un test pulito
            gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold, null);
            gameModel.MoveCargo(player, CargoColor.BLUE, cargoHold, null);

            // Aggiungi cargo
            gameModel.addCargo(player, CargoColor.YELLOW, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));

            gameModel.addCargo(player, CargoColor.GREEN, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.GREEN, 0));

            gameModel.addCargo(player, CargoColor.BLUE, cargoHold);
            assertEquals(1, ship.getCargo().getOrDefault(CargoColor.YELLOW, 0));

            // Verifica che il CargoHold sia pieno (ha 3 slot)
            assertThrows(CargoFullException.class, () ->
                    gameModel.addCargo(player, CargoColor.BLUE, cargoHold)
            );
        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testMoveCargoExceptions() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni un CargoHold dalla nave
        CargoHold cargoHold = (CargoHold) ship.getComponentAt(1, 2);

        // Test eccezione per cargo inesistente
        assertThrows(InvalidCargoException.class, () -> {
            gameModel.MoveCargo(player, CargoColor.RED, cargoHold, null);
        });

        try {
            // Crea un CargoHold con un solo slot
            CargoHold smallCargoHold = new CargoHold();
            smallCargoHold.setSlots(1);
            Map<Direction, ConnectorEnum> connectors = new HashMap<>();
            connectors.put(Direction.RIGHT, ConnectorEnum.S);
            connectors.put(Direction.LEFT, ConnectorEnum.S);
            connectors.put(Direction.UP, ConnectorEnum.S);
            connectors.put(Direction.DOWN, ConnectorEnum.S);
            smallCargoHold.setConnectors(connectors);
            ship.addComponent(smallCargoHold, 3, 1);

            // Riempi il CargoHold piccolo
            gameModel.addCargo(player, CargoColor.BLUE, smallCargoHold);

            // Test eccezione per CargoHold pieno
            assertThrows(CargoFullException.class, () -> {
                gameModel.MoveCargo(player, CargoColor.GREEN, cargoHold, smallCargoHold);
            });
        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni durante la preparazione: " + e.getMessage());
        }
    }

    @Test
    void testViewDeck() {
        gameModel.startGame(level, players, gameId);
        try {
            // Verifica che il metodo restituisca una lista di carte
            List<AdventureCard> cards = gameModel.viewDeck(1);
            assertNotNull(cards);
            assertFalse(cards.isEmpty());

            // Verifica eccezione con indice invalido
            assertThrows(InvalidIndexException.class, () -> gameModel.viewDeck(4));
        } catch (InvalidIndexException e) {
            fail("Non dovrebbe lanciare eccezioni con indice valido");
        }
    }

    @Test
    void testRemoveComponent() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni un componente dalla nave (scegli un componente che non è una cabina con alieni)
        Component component = ship.getComponentAt(1, 3); // Cannone
        try {
            // Rimuovi il componente dalla nave
            gameModel.removeComponent(component, player);

            // Verifica che il componente sia stato rimosso
            assertNull(player.getShip().getComponentAt(1, 3));
        } catch (ComponentNotFoundException e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testSetAlien() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni una cabina dalla nave
        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);

        try {
            // Aggiungi un alieno alla cabina
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);

            // Verifica che l'alieno sia stato aggiunto
            assertEquals(AlienColor.PURPLE, cabin.getAlienColor());

            // Verifica eccezione quando si prova ad aggiungere un altro alieno
            assertThrows(InvalidAlienPlacement.class, () ->
                    gameModel.setAlien(AlienColor.BROWN, cabin, player)
            );
        } catch (InvalidAlienPlacement e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testAddPieces() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Aggiungi un componente alla lista booked
        Component component = new Battery();
        try {
            gameModel.componentToBooked(component, player);
        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }

        // Verifica che la lista booked contenga il componente
        assertTrue(((NormalShip)player.getShip()).getBooked().contains(component));

        // Chiama il metodo addPieces
        gameModel.addPieces(player);

        // Verifica che la lista booked sia vuota e che il componente sia passato a waste
        assertTrue(player.getShip().getWaste().contains(component));

        // Verifica che gli astronauti siano stati inizializzati
        assertNotEquals(0, player.getShip().getAstronauts());
    }

    @Test
    void testCreateDeck() {
        gameModel.startGame(level, players, gameId);

        // Chiama il metodo createDeck
        gameModel.createDeck();

        // Verifica che il mazzo contenga delle carte
        assertFalse(gameModel.getGame().getBoard().getDeck().isEmpty());
    }

    @Test
    void testDrawCard() {
        gameModel.startGame(level, players, gameId);
        Player player1 = gameModel.getGame().getPlayers().getFirst();
        Player player2 = gameModel.getGame().getPlayers().get(1);

        // Imposta le posizioni dei giocatori
        player1.setPosition(20);
        player2.setPosition(2);

        try {
            // Pesca una carta
            AdventureCard card = gameModel.drawCard();

            // Verifica che la carta sia stata pescata e impostata come attiva
            assertNotNull(card);
            assertEquals(card, gameModel.getActiveCard());

            // Verifica che il giocatore molto indietro sia stato escluso
            if (player1.getPosition() - player2.getPosition() >= gameModel.getGame().getBoard().getSpaces()) {
                assertFalse(player2.isInGame());
            }
        } catch (EmptyDeckException e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testLoseCrew() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Imposta un alieno in una cabina
        Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
        try {
            cabin.setColor(AlienColor.PURPLE);
            gameModel.setAlien(AlienColor.PURPLE, cabin, player);

            // Verifica che l'alieno sia stato aggiunto
            assertTrue(cabin.getAlien());

            // Crea lista con la cabina
            List<Cabin> cabins = new ArrayList<>();
            cabins.add(cabin);

            // Chiama il metodo loseCrew
            gameModel.loseCrew(player, cabins);

            // Verifica che l'alieno sia stato rimosso
            assertEquals(AlienColor.NONE, cabin.getAlienColor());

        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni: " + e.getMessage());
        }
    }

    @Test
    void testUseShield() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni una batteria dalla nave
        Battery battery = (Battery) ship.getComponentAt(2, 2);
        int initialEnergy = battery.getAvailableEnergy();

        try {
            // Usa lo scudo
            gameModel.useShield(player, battery);

            // Verifica che l'energia sia stata consumata
            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            // Consuma tutta l'energia rimanente
            while (battery.getAvailableEnergy() > 0) {
                gameModel.useShield(player, battery);
            }

            // Verifica che venga lanciata un'eccezione quando l'energia è esaurita
            assertThrows(EnergyException.class, () -> gameModel.useShield(player, battery));
        } catch (EnergyException e) {
            fail("Non dovrebbe lanciare eccezioni con energia sufficiente: " + e.getMessage());
        }
    }

    @Test
    void testFire() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        try {
            // Imposta un alieno in una cabina
            Cabin cabin = (Cabin) ship.getComponentAt(2, 4);
            cabin.setColor(AlienColor.PURPLE);

            // Crea un proiettile direzionato verso la cabina
            Projectile heavyProjectile = new Projectile();
            heavyProjectile.setDirection(Direction.UP);
            heavyProjectile.setFireType(FireType.HEAVY_FIRE);

            // Il test potrebbe lanciare DeadAlienException se l'alieno muore
            gameModel.Fire(player, 10, heavyProjectile);
            assertFalse(cabin.getAlien());

        } catch (Exception e) {
            fail("Non dovrebbe lanciare eccezioni inattese: " + e.getMessage());
        }
    }

    @Test
    void testRemoveEnergy() {
        gameModel.startGame(level, players, gameId);
        Player player = gameModel.getGame().getPlayers().getFirst();
        player.setShip(ship);

        // Ottieni batterie dalla nave
        List<Battery> batteries = new ArrayList<>();
        Battery battery = (Battery) ship.getComponentAt(2, 2);
        batteries.add(battery);

        int initialEnergy = battery.getAvailableEnergy();

        try {
            // Rimuovi energia
            gameModel.removeEnergy(player, batteries);

            // Verifica che l'energia sia stata rimossa
            assertEquals(initialEnergy - 1, battery.getAvailableEnergy());

            // Consuma tutta l'energia rimanente
            while (battery.getAvailableEnergy() > 0) {
                gameModel.removeEnergy(player, batteries);
            }

            // Verifica che venga lanciata un'eccezione quando l'energia è esaurita
            assertThrows(EnergyException.class, () ->
                    gameModel.removeEnergy(player, batteries));
        } catch (EnergyException e) {
            fail("Non dovrebbe lanciare eccezioni con energia sufficiente: " + e.getMessage());
        }
    }
}