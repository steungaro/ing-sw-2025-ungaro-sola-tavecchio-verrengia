package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.cards.AbandonedShip;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
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
        assertEquals (board.getSpaces(), 24);
        assertNotNull (board.getStallBox());
        //testiamo anche i metodi peekDeck e getReaminingTime per testare il costruttore
        // non verrano fatti test specifici per questi metodi
        assertNotNull (board.peekDeck(1));
        assertNotNull (board.peekDeck(2));
        assertNotNull (board.peekDeck(3));
        assertEquals (board.getRemainingTime(), 5);
    }

    @Test
    void testDeck (){
        board.createDeck();
        for (int i = 0; i< 3; i++){
            assertNotNull (board.getDeck(1).get(i));
            assertNotNull (board.getDeck(2).get(i));
            assertNotNull (board.getDeck(3).get(i));
            assertNotNull (board.getDeck(4).get(i));
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
        board.initCountdown();
        assertEquals(5, board.getRemainingTime());
        Thread.sleep(2900);
        assertEquals(2, board.getRemainingTime());
        assertEquals(12, board.getTotalRemainingTime());
        Thread.sleep(2000);
        board.turnHourglass();
        assertEquals(5, board.getRemainingTime());
        board.stopHourglass();
    }

    @Test
    void testHourglass2 () throws InterruptedException {
        board.initCountdown();
        assertThrows(IllegalArgumentException.class, () -> board.turnHourglass());
        Thread.sleep(5100);
        board.turnHourglass();
        Thread.sleep(5100);
        board.turnHourglass();
        Thread.sleep(5100);
        assertEquals(0, board.getRemainingTime());
        assertEquals(2, board.getTurnedHourglass());
        assertThrows(IllegalArgumentException.class, () -> board.turnHourglass());
    }
}
