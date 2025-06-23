package it.polimi.ingsw.gc20.server.model.lobby;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.FullLobbyException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.LobbyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {
    private Lobby lobby;

    /**
     * Sets up the test environment by initializing a new instance of the Lobby class.
     * This method ensures that a consistent state is established before each test is executed.
     * <p>
     * The lobby is initialized with the following details:
     * - ID: "lobby1"
     * - Name: "Test Lobby"
     * - Owner: "owner"
     * - Maximum players: 4
     * - Level: 1
     */
    @BeforeEach
    public void setUp() {
        lobby = new Lobby("lobby1", "Test Lobby", "owner", 4, 1);
    }

    /**
     * Tests the default constructor of the Lobby class.
     * Verifies that the lobby is correctly initialized with the expected default values.
     * Assertions include:
     * - The lobby ID is correctly set to the expected value.
     * - The lobby name matches the expected value.
     * - The owner username is correctly assigned.
     * - The maximum number of players allowed is set correctly.
     * - The lobby level is initialized as expected.
     * - The initial list of users contains the owner.
     */
    @Test
    void defaultConstructor (){
        assertEquals("lobby1", lobby.getId());
        assertEquals("Test Lobby", lobby.getName());
        assertEquals("owner", lobby.getOwnerUsername());
        assertEquals(4, lobby.getMaxPlayers());
        assertEquals(1, lobby.getLevel());
        assertTrue(lobby.getUsers().contains("owner"));
    }

    /**
     * Tests the functionality of updating the maximum number of players allowed in the lobby.
     * This method verifies that the `setMaxPlayers` method in the Lobby class correctly updates
     * the maximum player limit and that the change is accurately retrievable through the
     * `getMaxPlayers` method.
     * <p>
     * The test ensures:
     * - The maximum number of players is updated to the new specified value.
     * - The updated value matches the expected output when retrieved.
     */
    @Test
    void testSetMaxPlayers() {
        lobby.setMaxPlayers(6);
        assertEquals(6, lobby.getMaxPlayers());
    }

    /**
     * Tests the `setOwnerUsername` method of the `Lobby` class.
     * Verifies that the owner's username is updated correctly when
     * the method is called with a new username. Ensures the updated
     * username is correctly retrieved using the `getOwnerUsername` method.
     * <p>
     * Assertions:
     * - The retrieved owner's username matches the newly set username.
     */
    @Test
    void testSetOwnerUsername() {
        lobby.setOwnerUsername("newOwner");
        assertEquals("newOwner", lobby.getOwnerUsername());
    }


    /**
     * Tests the `setName` method of the `Lobby` class.
     * <p>
     * Verifies that the lobby's name is correctly updated when a new name
     * is set using the `setName` method. Ensures that the new name can
     * be accurately retrieved using the `getName` method.
     * <p>
     * Assertions:
     * - The updated name matches the expected new name.
     */
    @Test
    void testSetName() {
        lobby.setName("New Lobby Name");
        assertEquals("New Lobby Name", lobby.getName());
    }

    /**
     * Tests the `setId` method of the `Lobby` class.
     * <p>
     * Verifies that the lobby's unique identifier is correctly updated
     * when a new value is set using the `setId` method. Ensures that the
     * updated identifier can be accurately retrieved using the `getId` method.
     * <p>
     * Assertions:
     * - The retrieved ID matches the newly set value.
     */
    @Test
    void setId() {
        lobby.setId("newId");
        assertEquals("newId", lobby.getId());
    }

    /**
     * Tests the `addPlayer` and `removePlayer` functionality in the `Lobby` class,
     * including exceptions that may occur during these operations.
     * <p>
     * The method performs the following verifications:
     * - Adds multiple players to the lobby and validates their presence using the `containsUser` method.
     * - Ensures that adding a player beyond the maximum capacity throws a `FullLobbyException`.
     * - Verifies that removing the owner of the lobby throws a `LobbyException` if there are other players in the lobby.
     * - Successfully removes a non-owner player and confirms they are no longer in the lobby's user list.
     * <p>
     * Assertions:
     * - The players that were added are correctly present in the lobby.
     * - Adding a player beyond the lobby's capacity results in a `FullLobbyException`.
     * - Attempting to remove the lobby owner throws a `LobbyException`.
     * - Players who are removed are no longer part of the lobby's user list.
     */
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

    /**
     * Tests the creation of a GameController from the lobby.
     * <p>
     * This method verifies:
     * - A player is successfully added to the lobby without exceeding the maximum capacity.
     * - The `createGameController` method generates a valid GameController instance.
     * <p>
     * Assertions:
     * - Adding a player does not throw a `FullLobbyException`.
     * - The created GameController instance is not null.
     * - No `InvalidStateException` is thrown during the creation of the GameController.
     */
    @Test
    void createController (){
        try {
            lobby.addPlayer("player1");
        } catch (FullLobbyException e) {
            fail("Lobby should not be full");
        }
        try {
            GameController gameController = lobby.createGameController();
            assertNotNull(gameController);
        } catch (InvalidStateException e) {
            fail();
        }

    }
}
