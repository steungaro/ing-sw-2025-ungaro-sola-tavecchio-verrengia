package it.polimi.ingsw.gc20.server.model.lobby;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.FullLobbyException;
import it.polimi.ingsw.gc20.server.exceptions.LobbyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {
    private Lobby lobby;

    @BeforeEach
    public void setUp() {
        lobby = new Lobby("lobby1", "Test Lobby", "owner", 4, 1);
    }

    @Test
    void defaultConstructor (){
        assertEquals("lobby1", lobby.getId());
        assertEquals("Test Lobby", lobby.getName());
        assertEquals("owner", lobby.getOwnerUsername());
        assertEquals(4, lobby.getMaxPlayers());
        assertEquals(1, lobby.getLevel());
        assertTrue(lobby.getUsers().contains("owner"));
    }

    @Test
    void testSetMaxPlayers() {
        lobby.setMaxPlayers(6);
        assertEquals(6, lobby.getMaxPlayers());
    }

    @Test
    void testSetOwnerUsername() {
        lobby.setOwnerUsername("newOwner");
        assertEquals("newOwner", lobby.getOwnerUsername());
    }

    @Test
    void testSetName() {
        lobby.setName("New Lobby Name");
        assertEquals("New Lobby Name", lobby.getName());
    }

    @Test
    void setId() {
        lobby.setId("newId");
        assertEquals("newId", lobby.getId());
    }

    @Test
    void testPlayer(){
        try {
            lobby.addPlayer("player1");
            lobby.addPlayer("player2");
            lobby.addPlayer("player3");
            assertTrue(lobby.containsUser("player1"));
            assertTrue(lobby.containsUser("player2"));
            assertTrue(lobby.containsUser("player3"));
        }catch (FullLobbyException e){
            fail("Lobby should not be full");
        }
        assertThrows(FullLobbyException.class, () -> lobby.addPlayer("player4"));
        assertThrows(LobbyException.class, () -> lobby.removePlayer("owner"));
        try{
            lobby.removePlayer("player1");
            assertFalse(lobby.getUsers().contains("player1"));
        }catch (LobbyException e){
            fail("Player should be removed");
        }

    }

    @Test
    void createController (){
        try {
            lobby.addPlayer("player1");
        } catch (FullLobbyException e) {
            fail("Lobby should not be full");
        }
        GameController gameController= lobby.createGameController();
        assertNotNull(gameController);

    }
}
