package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.HourglassException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidIndexException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NormalBoardTest {
    private NormalBoard board;

    /**
     * Prepares the test fixture to be used in test methods.
     * This method is executed before each test method within this class.
     * It initializes a new instance of the `NormalBoard` class and assigns it to the `board` variable.
     */
    @BeforeEach
    void setUp() {
        board = new NormalBoard();
    }

    /**
     * Tests the default constructor of the `NormalBoard` class, ensuring that all initializations
     * and default properties are correctly set and functioning as expected.
     * <p>
     * Validates the following behaviors:
     * - The deck of adventure cards is initialized and not null.
     * - The board contains the expected number of spaces (24).
     * - The stall box is initialized and not null.
     * - The `peekDeck` method allows access to the first three visible decks without throwing exceptions
     *   but throws an `InvalidIndexException` when attempting to access an invalid deck index (e.g., deck 5).
     * - The countdown timer is initialized correctly, and the remaining time is set to 90 seconds.
     * <p>
     * Assertions and exception testing are used to ensure the correctness of the constructor's implementation.
     */
    @Test
    void defaultConstructor (){
        assertNotNull (board.getDeck());
        assertEquals (24, board.getSpaces());
        assertNotNull (board.getStallBox());

        try {
            assertNotNull(board.peekDeck(1));
            assertNotNull(board.peekDeck(2));
            assertNotNull(board.peekDeck(3));
        } catch (InvalidIndexException e) {
            fail ("Exception should not be thrown");
        }
        assertThrows(InvalidIndexException.class, () -> board.peekDeck(5));
        board.initCountdown();
        assertEquals (90, board.getRemainingTime());
    }

    /**
     * Tests the creation and initialization of the game decks.
     * <p>
     * This method verifies that the `createDeck` method appropriately initializes
     * the four game decks (first visible, second visible, third visible, and invisible) with the correct structure.
     * <p>
     * For each of the three cards in the first, second, third visible decks, and the invisible deck,
     * this method ensures that the cards are not null.
     * <p>
     * Assertions:
     * - Confirms that each card in the decks returned by `getDeck(1)`, `getDeck(2)`, `getDeck(3)`, and `getDeck(4)`
     *   is properly initialized and not null.
     * <p>
     * Exceptions:
     * - Catches any `InvalidIndexException` during the deck retrieval process to ensure the test handles it gracefully.
     *   If this exception is thrown, the test fails with an appropriate error message.
     */
    @Test
    void testDeck (){
        board.createDeck();
        for (int i = 0; i< 3; i++){
            try {
                assertNotNull(board.getDeck(1).get(i));
                assertNotNull(board.getDeck(2).get(i));
                assertNotNull(board.getDeck(3).get(i));
                assertNotNull(board.getDeck(4).get(i));
            } catch (InvalidIndexException e) {
                fail("Exception should not be thrown");
            }
        }
    }

    /**
     * Tests the functionality of merging multiple decks into a single unified deck.
     * <p>
     * Verifies that the `mergeDecks` method correctly combines the four card decks
     * (`firstVisible`, `secondVisible`, `thirdVisible`, and `invisible`) into one deck and
     * shuffles it as expected. Additionally, it ensures that the size of the resulting deck
     * matches the expected number of cards (i.e., 12 cards after merging).
     * <p>
     * Assertions:
     * - Confirms that the total number of cards in the merged deck is 12.
     * <p>
     * Preconditions:
     * - The `createDeck` method must be called prior to invoking `mergeDecks` to initialize
     *   and populate the individual decks.
     */
    @Test
    void testMergeDecks (){
        board.createDeck();
        board.mergeDecks();
        assertEquals(12, board.getDeck().size());
    }

    /**
     * Tests the behavior of the hourglass in the game and validates its timing and functionality.
     * This method sets up an hourglass period, initiates the countdown, and verifies specific behaviors
     * such as time progression, total remaining time, and the ability to turn the hourglass.
     * <p>
     * Key operations tested:
     * - Setting the hourglass period to a specified duration (5 seconds).
     * - Initializing the countdown mechanism.
     * - Verifying the remaining time after a specific duration has elapsed.
     * - Checking the total remaining time after partial time usage.
     * - Ensuring that turning the hourglass succeeds at the appropriate time without throwing exceptions.
     * - Confirming that after turning the hourglass, the remaining time is reset correctly.
     * <p>
     * Assertions:
     * - The remaining time is reduced by the elapsed duration.
     * - The total remaining time is updated correctly after partial usage.
     * - No exceptions are thrown when turning the hourglass under valid conditions.
     * - The remaining time is reset to the hourglass period after turning it.
     * <p>
     * Exceptions:
     * - If an exception occurs during the `turnHourglass` method call, the test fails with an appropriate message.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted.
     */
    @Test
    void testHourglass1 () throws InterruptedException {
        board.hourglass.setPeriod(5);
        board.initCountdown();
        assertEquals(5, board.getRemainingTime());
        Thread.sleep(3000);
        assertEquals(2, board.getRemainingTime());
        assertEquals(12, board.getTotalRemainingTime());
        Thread.sleep(2000);
        try {
            board.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(5, board.getRemainingTime());
    }

    /**
     * Tests the advanced functionality of the hourglass in the game, specifically verifying
     * its behavior through multiple states, including transitions between periods, exceptions,
     * and resulting counts of turns.
     * <p>
     * This test focuses on:
     * - Setting the hourglass period to a defined duration (5 seconds).
     * - Ensuring the hourglass cannot be turned before the period elapses,
     *   leading to an exception.
     * - Allowing the hourglass to be turned once the time period is completed
     *   and validating no exception is thrown during the process.
     * - Incrementing the turn count and updating the remaining time following
     *   valid hourglass operations.
     * - Ensuring the hourglass cannot be turned more than the allowed number
     *   of times (2 turns in this case), raising an exception for any invalid
     *   attempt.
     * <p>
     * Assertions and exception handling:
     * - Validates that an HourglassException is thrown if the hourglass is
     *   turned before the period ends or after exceeding its turn limit.
     * - Verifies that no exception is thrown during valid hourglass turns.
     * - Checks the updated state of the remaining time and the turn count
     *   after valid hourglass operations.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted
     */
    @Test
    void testHourglass2 () throws InterruptedException {
        board.hourglass.setPeriod(5);
        board.initCountdown();
        assertThrows(HourglassException.class, () -> board.turnHourglass());
        Thread.sleep(5000);
        try {
            board.turnHourglass();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        Thread.sleep(5000);
        try {
            board.turnHourglass();
        } catch (HourglassException e) {
            fail("Exception should not be thrown");
        }
        Thread.sleep(5000);
        assertEquals(0, board.getRemainingTime());
        assertEquals(2, board.getTurnedHourglass());
        assertThrows(HourglassException.class, () -> board.turnHourglass());
    }
}
