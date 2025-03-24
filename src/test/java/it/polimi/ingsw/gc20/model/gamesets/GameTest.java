package it.polimi.ingsw.gc20.model.gamesets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testDefaultConstructor (){
        game = new Game();
        assertNotNull (game.getPlayers());
        assertNull (game.getBoard());
        assertNull (game.getID());
        assertNull (game.getPile());
    }
}
