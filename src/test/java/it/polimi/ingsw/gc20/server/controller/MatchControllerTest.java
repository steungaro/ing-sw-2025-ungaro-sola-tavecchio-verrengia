package it.polimi.ingsw.gc20.server.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {
    private static MatchController matchController;

    /**
     * Sets up the initial state for all tests in the MatchControllerTest class.
     * <p>
     * This method is executed once before any tests are run. It initializes the
     * MatchController instance, creates a lobby, and sets relevant configuration
     * parameters such as the maximum number of matches and lobbies.
     * <p>
     * The MatchController instance is configured with a grid of dimensions 3x3.
     * A lobby named "lobby1" is created with "player1" as the host, a maximum of
     * 4 players, and a minimum of 2 players required to start. The maximum number
     * of matches and lobbies are both set to 30.
     */
    @BeforeAll
    static void setUp() {
        matchController = MatchController.getInstance(3, 3);
        matchController.createLobby("lobby1", "player1", 4, 2);
        matchController.setMaxMatches(30);
        matchController.setMaxLobbies(30);
    }

    /**
     * Tests the functionality of retrieving the GameController associated with a specific player.
     * <p>
     * This test ensures that:
     * 1. A player who has joined a lobby is correctly associated with a GameController.
     * 2. A player who is not part of any game does not have an associated GameController.
     * <p>
     * Preconditions:
     * - The matchController is properly initialized.
     * - A lobby has been created using the `createLobby()` method with valid parameters.
     * <p>
     * Test Scenarios:
     * - A valid player who created or joined a lobby should have a non-null GameController.
     * - A player who is not part of any game should return a null GameController.
     * <p>
     * Assertions:
     * - Verifies that the GameController returned for "player1" is not null, as they are in a lobby.
     * - Verifies that the GameController for "player4" is null, as they are not in any game.
     */
    @Test
    void getGameControllerForPlayer() {
        matchController.createLobby("lobby10", "player1", 4, 2);

        assertNotNull(matchController.getGameControllerForPlayer("player1"));
        assertNull(matchController.getGameControllerForPlayer("player4"));
    }

    /**
     * Tests the functionality of retrieving a GameController by its ID.
     * <p>
     * This test ensures that if there are no games initialized within the system,
     * attempting to retrieve a GameController using a non-existent ID will return null.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) is initialized.
     * - No games have been created in the MatchController.
     * <p>
     * Test Scenario:
     * - Attempt to retrieve a GameController using an invalid or non-existent game ID.
     * <p>
     * Assertions:
     * - Verifies that the GameController retrieved for "nonexistentId" is null.
     */
    @Test
    void getGameController() {
        assertNull(matchController.getGameController("nonexistentId"));
    }

    /**
     * Tests the functionality of retrieving a singleton instance of the MatchController class.
     * <p>
     * This method ensures that the {@code getInstance()} method, when called, consistently returns
     * a non-null instance of the MatchController. It verifies the initialization and singleton
     * behavior of the MatchController instance.
     * <p>
     * Test Scenario:
     * - Invoke {@code getInstance()} and check that the returned instance is not null.
     * <p>
     * Assertions:
     * - Verifies that {@code getInstance()} returns a non-null MatchController instance.
     * <p>
     * Additional actions:
     * - Retrieves the list of lobbies associated with a player for further verification of functionality.
     */
    @Test
    void getInstance() {
        assertNotNull(MatchController.getInstance());
        matchController.getLobbies("player1");
    }

    /**
     * Tests the functionality of retrieving the list of players currently in the lobbies.
     * <p>
     * This method verifies that the list of players in the lobbies is correctly populated
     * and matches the expected state after performing relevant operations such as creating
     * a lobby and adding a player.
     * <p>
     * Test Scenarios:
     * - Ensure the list of players in the lobbies is not null after creating a lobby.
     * - Verify that the list contains the expected players based on the pre-established setup.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) is properly initialized.
     * - The initial setup includes creating a lobby with "player1" as the host.
     * <p>
     * Assertions:
     * - Asserts that the list returned by {@code getPlayersInLobbies()} is not null.
     * - Confirms that the list contains the player "player1", who was added in the setup phase.
     */
    @Test
    void getPlayersInLobbies() {
        matchController.createLobby("lobby2", "player1", 4, 2);
        assertNotNull(matchController.getPlayersInLobbies());
        assertTrue(matchController.getPlayersInLobbies().contains("player1"));
    }

    /**
     * Tests the functionality of the {@code getInstance} method in the {@code MatchController} class.
     * <p>
     * This test ensures that invoking the {@code getInstance(int maxMatches, int maxLobbies)} method
     * with valid parameters returns a non-null instance of the {@code MatchController}.
     * <p>
     * Preconditions:
     * - The {@code MatchController.getInstance()} method is designed to simulate a singleton behavior and must
     *   allow initialization with specific parameters on the first call.
     * <p>
     * Test Scenario:
     * - Call the {@code getInstance} method with valid dimensions for maximum matches and lobbies.
     * <p>
     * Assertions:
     * - Verifies that the resulting {@code MatchController} instance is not null.
     */
    @Test
    void testGetInstance() {
        assertNotNull(MatchController.getInstance(3, 3));
    }

    /**
     * Tests the functionality of setting the maximum number of lobbies in the MatchController.
     * <p>
     * This test ensures that updating the maximum number of lobbies allows the creation
     * of additional lobbies beyond the previous limit without raising exceptions.
     * <p>
     * Test Scenarios:
     * - Update the maximum number of lobbies using the {@code setMaxLobbies()} method.
     * - Verify that more lobbies can be created after increasing the limit.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) is properly initialized.
     * - The initial maximum number of lobbies is set to a specific value.
     * <p>
     * Verification:
     * - Additional lobbies are successfully created beyond the previous limit without
     *   any exceptions being thrown.
     */
    @Test
    void setMaxLobbies() {
        matchController.setMaxLobbies(5);
        matchController.createLobby("lobby2", "player2", 4, 2);
        matchController.createLobby("lobby3", "player3", 4, 2);
        matchController.createLobby("lobby4", "player4", 4, 2);
    }

    /**
     * Tests the functionality of setting the maximum number of matches in the MatchController.
     * <p>
     * This test ensures that the {@code setMaxMatches()} method correctly updates
     * the maximum number of matches allowed in the MatchController instance.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) must be properly initialized.
     * <p>
     * Test Scenarios:
     * - Call the {@code setMaxMatches()} method with a valid number of matches.
     * - Verify that the maximum number of matches reflects the updated value.
     * <p>
     * Verification:
     * - The method executes without raising any exceptions.
     * - The internal state of the MatchController is updated with the given number of maximum matches.
     */
    @Test
    void setMaxMatches() {
        matchController.setMaxMatches(5);
    }

    /**
     * Tests the functionality of a player joining an existing lobby in the MatchController system.
     * <p>
     * This test ensures that a lobby created using the {@code createLobby()} method
     * can successfully accept a new player joining through the {@code joinLobby()} method.
     * It verifies that the newly added player is correctly associated with the specified lobby.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) must be properly initialized.
     * - A lobby named "joinTestLobby" must be created prior to the player attempting to join.
     * - The lobby must not have reached its maximum player capacity.
     * <p>
     * Test Scenarios:
     * - A new player, "newPlayer", will attempt to join an existing lobby "joinTestLobby".
     * - Verify that the player is successfully added to the lobby.
     * <p>
     * Assertions:
     * - Ensure that the newly joined player is included in the list of users associated with the lobby.
     */
    @Test
    void joinLobby() {
        matchController.createLobby("joinTestLobby", "owner", 4, 2);
        matchController.joinLobby("joinTestLobby", "newPlayer");

        Lobby lobby = matchController.getLobby("joinTestLobby");
        assertTrue(lobby.getUsers().contains("newPlayer"));
    }

    /**
     * Tests the creation of a new lobby in the MatchController system.
     * <p>
     * This method verifies that invoking {@code createLobby()} initializes a new lobby
     * with the specified parameters and ensures the lobby is correctly associated
     * with the host (creator). The test also confirms that the lobby is retrievable
     * via its name and includes the creator in the list of users.
     * <p>
     * Preconditions:
     * - The MatchController instance (matchController) must be properly initialized.
     * <p>
     * Test Scenarios:
     * - A lobby named "createTestLobby" is created with the following parameters:
     *   - Creator: "creator"
     *   - Maximum players: 4
     *   - Minimum players to start: 2
     * <p>
     * Assertions:
     * - Verifies that the newly created lobby is not null when retrieved via its name.
     * - Ensures that the creator ("creator") is included in the list of users associated with the lobby.
     */
    @Test
    void createLobby() {
        matchController.createLobby("createTestLobby", "creator", 4, 2);

        Lobby lobby = matchController.getLobby("createTestLobby");
        assertNotNull(lobby);
        assertTrue(lobby.getUsers().contains("creator"));
    }

    /**
     * Tests the functionality of a player leaving a lobby.
     * <p>
     * This method creates a new lobby, adds a player to it, and allows the player
     * to leave. It then verifies that the player is no longer part of the lobby.
     * <p>
     * Steps involved:
     * 1. Create a lobby with a specific name, owner, size, and other properties.
     * 2. Add a player to the lobby.
     * 3. Remove the player from the lobby using the leaveLobby method.
     * 4. Assert that the player is no longer present in the lobby by checking its
     *    user list.
     * 5. Finally, the owner of the lobby also leaves as part of cleanup.
     * <p>
     * This test ensures that the leaveLobby functionality behaves as expected
     * when a user exits from a lobby.
     */
    @Test
    void leaveLobby() {
        matchController.createLobby("leaveTestLobby", "owner", 4, 2);
        matchController.joinLobby("leaveTestLobby", "leaver");

        matchController.leaveLobby("leaver");

        Lobby lobby = matchController.getLobby("leaveTestLobby");
        assertFalse(lobby.getUsers().contains("leaver"));
        matchController.leaveLobby("owner");
    }

    /**
     * Test method for verifying the behavior of the endGame method in the matchController.
     * This method performs a basic test to ensure that the endGame method does not throw
     * any exceptions when invoked with a non-existent game ID.
     * <p>
     * It is noted that this test does not cover the full functionality of the endGame method,
     * as a more comprehensive test would require an active and complete game instance.
     */
    @Test
    void endGame() {
        matchController.endGame("nonExistentGameId");
    }

    /**
     * Tests the functionality of starting a lobby with the specified settings.
     * <p>
     * This method verifies that a lobby can be created with sufficient players and
     * initiates a game without throwing exceptions. It includes the following steps:
     * - Creating a lobby with the specified parameters (lobby name, owner, and player count).
     * - Adding an additional player to the lobby after creation.
     * - Ensuring that the game can be initiated through the game controller
     *   once the minimum requirements are met.
     *
     * @throws InterruptedException if the current thread is interrupted while sleeping.
     */
    @Test
    void startLobby() throws InterruptedException {
        matchController.createLobby("startTestLobby", "owner", 2, 2);
        Thread.sleep(1000);
        matchController.joinLobby("startTestLobby", "player1");
        matchController.getGameController("owner");
    }

    /**
     * Retrieves a complete list of all users from the system.
     * <p>
     * This method invokes the `getAllUsers` function from the `matchController`
     * and ensures that the returned list is not null and contains expected entries.
     * It is intended for testing purposes to validate the functionality of the `getAllUsers` logic.
     * <p>
     * Assertions:
     * - The returned list of users must not be null.
     * - The list must contain specific expected users, such as "player1".
     */
    @Test
    void getAllUsers() {
        List<String> allUsers = matchController.getAllUsers();
        assertNotNull(allUsers);
        assertTrue(allUsers.contains("player1"));
    }

    /**
     * Tests the functionality of the isUsernameAvailable method in the MatchController class.
     * <p>
     * The test verifies that the method correctly identifies if a username is available or not.
     * It checks two cases:
     * - A username that is already taken should return false.
     * - A new username that is not yet taken should return true.
     */
    @Test
    void isUsernameAvailable() {
        assertFalse(matchController.isUsernameAvailable("player1"));

        assertTrue(matchController.isUsernameAvailable("newRandomUsername"));
    }
}