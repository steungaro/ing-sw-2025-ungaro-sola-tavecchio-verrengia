package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Planet;
import it.polimi.ingsw.gc20.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LearnerBoardTest {
    private Board board;

    @BeforeEach
    void setUp(){
        board= new LearnerBoard();
    }

    @Test
    void testDefaultConstructor (){
        assertNotNull (board.getDeck());
        assertEquals (18, board.getSpaces());
        assertNotNull (board.getStallBox());
    }

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
    }


    @Test
    void testSetAndGetSpaces (){
        board.setSpaces(2);
        assertEquals(2, board.getSpaces());
    }

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
}
