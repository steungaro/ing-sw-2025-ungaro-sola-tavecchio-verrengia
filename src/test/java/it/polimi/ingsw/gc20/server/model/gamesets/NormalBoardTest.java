package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.HourglassException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidIndexException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NormalBoardTest {
    private NormalBoard board;

    @BeforeEach
    void setUp() {
        board = new NormalBoard();
    }

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
    @Test
    //not testable until we have the method to create the four deck
    void testMergeDecks (){
        board.createDeck();
        board.mergeDecks();
        assertEquals(12, board.getDeck().size());
    }

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
