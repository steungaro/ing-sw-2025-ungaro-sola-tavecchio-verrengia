package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.DieNotRolledException;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    /**
     * Sets up the initial state required for each test case in the GameTest class.
     * This method is executed before each test to ensure a clean and consistent setup.
     * <p>
     * The setup process involves:
     * - Instantiating a new Game object.
     * - Creating four Player instances, representing players partaking in the game.
     * - Creating a NormalBoard instance and associating it with the game.
     * <p>
     * This ensures that each test case begins with a properly initialized game environment.
     */
    @BeforeEach
    void setUp() {
        game = new Game();
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        player4 = new Player();
        Board board = new NormalBoard();
        game.addBoard(board);
    }

    /**
     * Tests the default constructor of the Game class.
     * <p>
     * This method ensures that the default initialization behavior of the Game
     * constructor correctly sets up the game's internal state by performing the
     * following validations:
     * <p>
     * - Verifies that the list of players is properly initialized and not null.
     * - Verifies that the game board instance is properly initialized and not null.
     * - Confirms that the game ID is not set, expecting a null value.
     * - Confirms that the pile instance is not set, expecting a null value.
     * <p>
     * The test ensures that the default Game constructor establishes a baseline
     * configuration, where critical components are instantiated or left uninitialized
     * as intended prior to further customization or gameplay.
     */
    @Test
    void testDefaultConstructor (){
        assertNotNull (game.getPlayers());
        assertNotNull (game.getBoard());
        assertNull (game.getID());
        assertNull (game.getPile());
    }

    /**
     * Tests the `addPlayer` and `getPlayers` functionalities of the `Game` class.
     * <p>
     * This method validates the following scenarios:
     * <p>
     * - Adding a single player:
     *   - Adds a player instance to the game's player list.
     *   - Verifies that the added player is present in the list of players retrieved using `getPlayers`.
     *   - Removes the player and ensures the list of players is empty after removal.
     * <p>
     * - Adding multiple players:
     *   - Adds multiple player instances to the game.
     *   - Constructs an expected list of players and verifies that the `getPlayers` method returns the same list,
     *     verifying order and contents.
     * <p>
     * The test ensures the correctness of adding, retrieving, and removing players in the game, as well as the consistency
     * of the players' list.
     */
    @Test
    void testAddAndGetPlayers (){
        game.addPlayer(player1);
        assertTrue (game.getPlayers().contains(player1));
        game.removePlayer(player1);
        assertTrue (game.getPlayers().isEmpty());
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        assertEquals(game.getPlayers(), players);
    }


    /**
     * Validates the setting and retrieving of the game ID using the setID and getID methods of the Game class.
     * <p>
     * This test performs the following steps:
     * - Sets a specific game ID using the setID method.
     * - Retrieves the game ID using the getID method.
     * - Verifies that the retrieved game ID matches the originally set ID.
     * <p>
     * Ensures that the setID and getID methods function correctly,
     * and the internal state of the game ID is properly maintained.
     */
    @Test
    void testSetAndGetID (){
        String gameID = "bbr brr patapim";
        game.setID(gameID);
        assertEquals(gameID, game.getID());
    }

    /**
     * Tests the `setPile` and `getPile` methods of the `Game` class.
     * <p>
     * This test ensures that the pile associated with the `Game` instance
     * can be set and retrieved correctly. It performs the following steps:
     * <p>
     * - Creates a new `Pile` instance.
     * - Sets the created `Pile` instance to the `Game` object using `setPile`.
     * - Retrieves the pile using `getPile` and verifies that it matches the
     *   originally set `Pile` instance.
     * <p>
     * This test ensures the integrity of the `setPile` and `getPile` methods
     * and confirms that the internal state of the pile in the `Game` instance
     * is maintained correctly.
     */
    @Test
    void testSetAndGetPile (){
        Pile pile = new Pile();
        game.setPile(pile);
        assertEquals(pile, game.getPile());
    }

    /**
     * Tests the `sortPlayerByPosition` method of the `Game` class.
     * <p>
     * This test ensures that players within the game are sorted correctly
     * based on their positions in ascending order. It performs the following actions:
     * <p>
     * - Sets specific positions for four players using the `setPosition` method.
     * - Adds these players to the game's player list in reverse order.
     * - Creates an expected list of players sorted by their positions.
     * - Calls the `sortPlayerByPosition` method to sort the players within the game.
     * - Compares the sorted players list from the game with the expected list
     *   to ensure they match.
     * <p>
     * Validates the correctness and integrity of the player sorting functionality
     * based on their positions in the game.
     */
    @Test
    void testSortPlayerByPosition(){
        player1.setPosition(10);
        player2.setPosition(9);
        player3.setPosition(8);
        player4.setPosition(7);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        game.addPlayer(player4);
        game.addPlayer(player3);
        game.addPlayer(player2);
        game.addPlayer(player1);

        game.sortPlayerByPosition();
        assertEquals(game.getPlayers(), players);
    }


    /**
     * Validates the behavior of the {@code isOccupied} method within the {@code Game} class.
     * <p>
     * This test method performs the following steps:
     * - Sets the positions of four players on the game board using the {@code setPosition} method.
     * - Assigns game status for each player as active or inactive using {@code setGameStatus}.
     * - Adds the players to the game using the {@code addPlayer} method.
     * - Verifies whether specific positions on the board are occupied:
     *   - Checks positions where players are present and expects {@code true}.
     *   - Checks positions where no players are present and expects {@code false}.
     * <p>
     * Ensures that the {@code isOccupied} method correctly identifies if any active player
     * currently occupies a given position on the game board.
     */
    @Test
    void isOccupied (){
        player1.setPosition(10);
        player2.setPosition(0);
        player3.setPosition(8);
        player4.setPosition(7);
        player4.setGameStatus(true);
        player1.setGameStatus(true);
        player2.setGameStatus(true);
        player3.setGameStatus(true);
        game.addPlayer(player4);
        game.addPlayer(player3);
        game.addPlayer(player2);
        game.addPlayer(player1);

        assertTrue (game.isOccupied(24));
        assertTrue (game.isOccupied(10));
        assertTrue (game.isOccupied(8));
        assertTrue (game.isOccupied(7));
        assertFalse (game.isOccupied(6));
    }

    /**
     * Tests the functionality of moving a player within the game and sorting players
     * by their positions after the move.
     * <p>
     * This method performs the following sequence of actions:
     * <p>
     * - Initializes the positions and game statuses of four players.
     * - Adds the players to the game in a specific order.
     * - Verifies that a specified position is occupied by a player using the `isOccupied` method.
     * - Moves one player to a new position using the `move` method and verifies the new position.
     * - Sorts players by their positions using the `sortPlayerByPosition` method.
     * - Validates that the sorted player list starts with the player in the highest position.
     * <p>
     * Ensures the correctness of player movement, position update, and sorting mechanics
     * within the game.
     */
    @Test
    void testMove1 (){
        player1.setPosition(10);
        player2.setPosition(9);
        player3.setPosition(8);
        player4.setPosition(7);
        player4.setGameStatus(true);
        player1.setGameStatus(false);
        player2.setGameStatus(true);
        player3.setGameStatus(true);
        game.addPlayer(player4);
        game.addPlayer(player3);
        game.addPlayer(player2);
        game.addPlayer(player1);


        assertTrue (game.isOccupied(8));
        game.move(player4, 2);
        assertEquals(11, player4.getPosition());
        game.sortPlayerByPosition();
        assertEquals(game.getPlayers().getFirst(), player4);
    }

    /**
     * Tests the functionality of moving a player within the game and verifying the
     * updated position after the move.
     * <p>
     * This method performs the following steps:
     * <p>
     * - Sets initial positions for two players using the `setPosition` method.
     * - Activates the game statuses of the two players using the `setGameStatus` method.
     * - Adds the two players to the game using the `addPlayer` method.
     * - Moves one player to a new position using the `move` method.
     * - Asserts that the new position of the player after the move is correctly updated.
     * <p>
     * Ensures the correctness of the `move` method in updating the player's position.
     */
    @Test
    void testMove2 (){
        player1.setPosition(22);
        player2.setPosition(0);
        player1.setGameStatus(true);
        player2.setGameStatus(true);

        game.addPlayer(player2);
        game.addPlayer(player1);
        game.move(player1, 2);
        assertEquals(25, player1.getPosition());
    }

    /**
     * Tests the functionality of rolling dice and retrieving the last rolled value in the game.
     * <p>
     * This test performs the following sequence of actions:
     * - Rolls the dice 100 times using the {@code rollDice} method of the {@code Game} class.
     * - Asserts that each roll produces a value within the expected bounds (0 to 12 inclusive).
     * - Retrieves the last rolled value using the {@code lastRolled} method and verifies it matches the most recent roll.
     * - Ensures that no {@code DieNotRolledException} is thrown during the retrieval of the last rolled value.
     * <p>
     * Validates the correctness of the dice rolling mechanism and ensures that the {@code lastRolled} method accurately
     * reflects the last rolled value without throwing exceptions under normal test conditions.
     */
    @Test
    void testDice (){
        int value = 0;
        for (int i = 0; i<100; i++) {
            value = game.rollDice();
            assertTrue(value >= 0 && value <= 12);
        }
        try {
            assertEquals(value, game.lastRolled());
        } catch (DieNotRolledException e){
            fail("DieNotRolledException should not be thrown");
        }
    }
}
