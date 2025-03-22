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
    //not testable until we have the method to create the four deck
    void testMergeDecks (){
    }

    @Test
    void testHourglass () throws InterruptedException {
        board.initCountdown();
        assertEquals(board.getRemainingTime(), 5);
        Thread.sleep(3000);
        assertTrue(board.getRemainingTime()<=2);
        assertTrue(board.getTotalRemainingTime()<=17);
        Thread.sleep(2000);
        board.turnHourglass();
        assertTrue(board.getRemainingTime()<=5);


    }


}
