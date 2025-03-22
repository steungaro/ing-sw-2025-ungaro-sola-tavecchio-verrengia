package it.polimi.ingsw.gc20.model.player;


import it.polimi.ingsw.gc20.model.ship.LearnerShip;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import it.polimi.ingsw.gc20.model.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void defaultConstructor(){
        assertEquals(0, player.getCredits());
        assertNull(player.getColor());
        assertNull(player.getShip());
        assertEquals("", player.getUsername());
        assertEquals(0, player.getPosition());
        assertFalse(player.isLeader());
        assertFalse(player.isInGame());
    }

    @Test
    public void testSetAndGetColor (){
        PlayerColor color = PlayerColor.GREEN;
        player.setColor(color);
        assertEquals(color, player.getColor());
    }

    @Test
    public void testSetAndGetNormalShip (){
        Ship ship = new NormalShip();
        player.setShip(ship);
        assertEquals(ship, player.getShip());
    }

    @Test
    public void testSetAndGetLearnerShip (){
        Ship ship = new LearnerShip();
        player.setShip(ship);
        assertEquals(ship, player.getShip());
    }

    @Test
    public void testSetAndIsLeader (){
        player.setLeader();
        assertTrue(player.isLeader());
    }

    @Test
    public void testSetAndGetUsername (){
        String username = "Nasone";
        player.setUsername(username);
        assertEquals(username, player.getUsername());
    }

    @Test
    public void testSetAndGetPosition () {
        player.setPosition(11);
        assertEquals(11, player.getPosition());
    }

    @Test
    public void testAddAndRemoveCredits () {
        player.addCredits(10);
        player.removeCredits(5);
        assertEquals(5, player.getCredits());
    }

    @Test
    public void testSetAndGetGameStatus (){
        player.setGameStatus(true);
        assertTrue (player.isInGame());
    }
}
