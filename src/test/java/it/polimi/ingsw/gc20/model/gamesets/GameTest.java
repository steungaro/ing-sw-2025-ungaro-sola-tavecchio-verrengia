package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.Player;
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

    @Test
    void testDefaultConstructor (){
        assertNotNull (game.getPlayers());
        assertNotNull (game.getBoard());
        assertNull (game.getID());
        assertNull (game.getPile());
    }

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


    @Test
    void testSetAndGetID (){
        String gameID = "ciao";
        game.setID(gameID);
        assertEquals(gameID, game.getID());
    }

    @Test
    void testSetAndGetPile (){
        Pile pile = new Pile();
        game.setPile(pile);
        assertEquals(pile, game.getPile());
    }

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
        assertEquals(player4.getPosition(), 11);
        game.sortPlayerByPosition();
        assertEquals(game.getPlayers().get(0), player4);
    }

    @Test
    void testMove2 (){
        player1.setPosition(22);
        player2.setPosition(0);
        player1.setGameStatus(true);
        player2.setGameStatus(true);

        game.addPlayer(player2);
        game.addPlayer(player1);
        game.move(player1, 2);
        assertEquals(player1.getPosition(), 25);
    }

    @Test
    void testDice (){
        int value = 0;
        for (int i = 0; i<100; i++) {
            value = game.rollDice();
            assertTrue(value >= 0 && value <= 12);
        }
        assertEquals (value, game.lastRolled());
    }
}
