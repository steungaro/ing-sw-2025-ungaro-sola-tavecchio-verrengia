package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LearnerBoardTest {
    private Board board;

    /**
     * Sets up the test environment by initializing the `board` instance variable
     * to a new `LearnerBoard` object before each test execution.
     * <p>
     * This method ensures that a fresh instance of `LearnerBoard` is used for
     * every test, providing a consistent and isolated testing environment.
     */
    @BeforeEach
    void setUp(){
        board= new LearnerBoard();
    }

    /**
     * Tests the correct initialization of the `LearnerBoard` object using the default constructor.
     * <p>
     * This test validates that:
     * - The deck of `AdventureCard` objects is initialized and not null.
     * - The number of spaces on the board is correctly set to 18.
     * - The stall box containing players is initialized and not null.
     */
    @Test
    void testDefaultConstructor (){
        assertNotNull (board.getDeck());
        assertEquals (18, board.getSpaces());
        assertNotNull (board.getStallBox());
    }


    /**
     * Tests the deck initialization and properties of the adventure cards in the deck.
     * <p>
     * Verifies the following:
     * - The deck is created and is not null after the `createDeck` method is invoked.
     * - The deck contains exactly 8 `AdventureCard` objects.
     * - Each `AdventureCard` in the deck is not null.
     * - If a card's name is "Planets", all associated `Planet` objects are:
     *   - Not null.
     *   - Have a reward that is not null.
     *   - Marked as available.
     * - All cards have their level set to 0.
     */
    @Test
    void testDeck (){
        board.createDeck();
        assertNotNull(board.getDeck());
        assertEquals (8, board.getDeck().size());
        for (AdventureCard card : board.getDeck()){
            assertNotNull(card);
            if (card.getName().equals("Planets")){
                for (Planet planet : (card.getPlanets())){
                    assertNotNull(planet);
                    assertNotNull(planet.getReward());
                    assertTrue (planet.getAvailable());
                }
            }
            assertEquals(0, card.getLevel());
        }
    }

    /**
     * Tests the draw card functionality and the methods for setting and getting the deck in the `LearnerBoard` class.
     * <p>
     * Validates the following:
     * - A single `AdventureCard` can be added to the deck and retrieved correctly using `setDeck` and `getDeck` methods.
     * - The first card in the deck matches the expected card after using the `getDeck` method.
     * - The `drawCard` method correctly returns the card from the deck without throwing an exception.
     * - An `EmptyDeckException` is thrown when attempting to draw a card from an empty deck.
     * - The `mergeDecks` method can be invoked without errors.
     */
    @Test
    void testDrawCardAndSetAndGetDeck (){
        AdventureCard card = new AdventureCard();
        List<AdventureCard> deck = new ArrayList<>();
        deck.add(card);
        board.setDeck(deck);
        assertEquals(board.getDeck().getFirst(), card);
        assertEquals(deck, board.getDeck());
        try {
            assertEquals(board.drawCard(), card);
        } catch (Exception e){
            fail("Exception should not be thrown");
        }
        assertThrows(EmptyDeckException.class, ()->board.drawCard());
        board.mergeDecks();
    }


    /**
     * Tests the setting and retrieval of the number of spaces on the board.
     * <p>
     * This method verifies the following:
     * - The `setSpaces` method correctly updates the number of spaces on the board.
     * - The `getSpaces` method accurately retrieves the updated number of spaces.
     * <p>
     * By asserting that the value retrieved through `getSpaces` matches the value
     * set through `setSpaces`, the test ensures the integrity of these methods.
     */
    @Test
    void testSetAndGetSpaces (){
        board.setSpaces(2);
        assertEquals(2, board.getSpaces());
    }

    /**
     * Tests the functionality of adding and removing players from the stall box on the board
     * and verifies the correctness of the stall box contents after each operation.
     * <p>
     * This method checks the following behavior:
     * - Players can be added to the stall box using the `addPlayer` method.
     * - The `getStallBox` method correctly retrieves the list of players in the stall box.
     * - Players can be removed from the stall box using the `removePlayer` method.
     * - The stall box reflects the correct state after each add/remove operation.
     * <p>
     * Assertions:
     * - The list of players retrieved through `getStallBox` matches the expected list after players are added.
     * - The list of players retrieved through `getStallBox` matches the expected list after a player is removed.
     */
    @Test
    void testAddAndRemovePlayerAndGetStallBox (){
        Player player1 = new Player();
        Player player2 = new Player();
        board.addPlayer(player1);
        board.addPlayer(player2);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        assertEquals(board.getStallBox(), players);
        board.removePlayer(player1);
        players.remove(player1);
        assertEquals(board.getStallBox(), players);
    }

    /**
     * Tests the {@code isLearner} method of the {@code LearnerBoard} class.
     * <p>
     * This test validates the behavior of the {@code isLearner} method to ensure that:
     * - It returns {@code true} when the board is at the learner level.
     * <p>
     * The test uses an assertion to confirm the correct return value from the method.
     */
    @Test
    void testIsLearnerBoard() {
        assertTrue(board.isLearner());
    }
}
