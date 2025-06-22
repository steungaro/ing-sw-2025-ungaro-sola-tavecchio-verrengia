package it.polimi.ingsw.gc20.server.model.player;


import it.polimi.ingsw.gc20.server.model.ship.LearnerShip;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
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
    void getPublicData(){
        player.setColor(PlayerColor.GREEN);
        player.setUsername("player");
        Ship ship = new NormalShip();
        player.setShip(ship);
        player.setLeader();
        player.addCredits(10);
        player.setPosition(5);
        player.setGameStatus(true);
        Player publicPlayer = player.getPublicData();
        assertEquals(PlayerColor.GREEN, publicPlayer.getColor());
        assertEquals("player", publicPlayer.getUsername());
        assertEquals(ship, publicPlayer.getShip());
        assertTrue(publicPlayer.isLeader());
        assertEquals(0, publicPlayer.getCredits());
        assertEquals(5, publicPlayer.getPosition());
        assertTrue(publicPlayer.isInGame());
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
        String username = "BigNose";
        player.setUsername(username);
        assertEquals(username, player.getUsername());
    }

    @Test
    public void testSetAndGetPosition () {
        player.setPosition(11);
        assertEquals(11, player.getPosition());
    }

    @Test
    public void testAddAndRemoveCredits1 () {
        player.addCredits(10);
        player.removeCredits(5);
        assertEquals(5, player.getCredits());
    }

    @Test
    public void testAddAndRemoveCredits2 () {
        player.addCredits(10);
        assertEquals(5, player.removeCredits(15));
        assertEquals(0, player.getCredits());
    }

    @Test
    public void testSetAndGetGameStatus (){
        player.setGameStatus(true);
        assertTrue (player.isInGame());
    }

    @Test
    public void testColors() {
        assertNotNull(PlayerColor.values());
        assertEquals("\u001B[34m" + "P1" + "\u001B[0m", PlayerColor.BLUE.TUIPrint());
        assertEquals("\u001B[31m" + "P2" + "\u001B[0m", PlayerColor.RED.TUIPrint());
        assertEquals("\u001B[32m" + "P3" + "\u001B[0m", PlayerColor.GREEN.TUIPrint());
        assertEquals("\u001B[33m" + "P4" + "\u001B[0m", PlayerColor.YELLOW.TUIPrint());
    }
}
