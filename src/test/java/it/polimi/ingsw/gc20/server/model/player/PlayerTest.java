package it.polimi.ingsw.gc20.server.model.player;


import it.polimi.ingsw.gc20.server.model.ship.LearnerShip;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    /**
     * Sets up the testing environment before each test execution.
     * Initializes the player instance to ensure that all tests start
     * with a fresh Player object in a default state.
     */
    @BeforeEach
    void setUp() {
        player = new Player();
    }

    /**
     * Tests the default constructor of the Player class.
     * Ensures that all fields of a newly constructed Player instance are initialized to their default values.
     * <p>
     * Specifically, this test verifies:
     * - The default credit balance is 0.
     * - The color is null.
     * - The ship is null.
     * - The username is an empty string.
     * - The position on the board is 0.
     * - The leader status is false.
     * - The in-game status is false.
     */
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

    /**
     * Tests the functionality of the getPublicData method in the Player class.
     * <p>
     * This test verifies that when a player's public data is retrieved:
     * - The player's color is correctly returned.
     * - The username is accurately returned.
     * - The ship instance is returned as expected.
     * - The leader status of the player is accurate.
     * - Credits are not exposed and are reset to 0.
     * - The board position of the player is correct.
     * - The in-game status is appropriately reflected.
     * <p>
     * The method ensures that sensitive or private information such as credits is not exposed
     * through the public representation of a player.
     */
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

    /**
     * Tests the functionality of setting and retrieving the player's color.
     * <p>
     * This test verifies that:
     * - The `setColor` method correctly assigns the given {@code PlayerColor} to the player.
     * - The `getColor` method accurately retrieves the assigned {@code PlayerColor}.
     * <p>
     * The test ensures that the color set using the `setColor` method is the same as the
     * color retrieved using the `getColor` method.
     */
    @Test
    public void testSetAndGetColor (){
        PlayerColor color = PlayerColor.GREEN;
        player.setColor(color);
        assertEquals(color, player.getColor());
    }

    /**
     * Tests the functionality of setting and retrieving the player's ship with a normal ship instance.
     * <p>
     * Verifies that:
     * - The `setShip` method correctly assigns a {@code NormalShip} instance to the player.
     * - The `getShip` method accurately retrieves the assigned {@code NormalShip} instance.
     * <p>
     * Ensures that the ship set using the `setShip` method is the same as the ship retrieved using the `getShip` method.
     */
    @Test
    public void testSetAndGetNormalShip (){
        Ship ship = new NormalShip();
        player.setShip(ship);
        assertEquals(ship, player.getShip());
    }

    /**
     * Tests the functionality of setting and retrieving the player's ship with a learner ship instance.
     * <p>
     * Verifies that:
     * - The `setShip` method correctly assigns a {@code LearnerShip} instance to the player.
     * - The `getShip` method accurately retrieves the assigned {@code LearnerShip} instance.
     * <p>
     * Ensures that the ship set using the `setShip` method is the same as the ship retrieved using the `getShip` method.
     */
    @Test
    public void testSetAndGetLearnerShip (){
        Ship ship = new LearnerShip();
        player.setShip(ship);
        assertEquals(ship, player.getShip());
    }

    /**
     * Tests the functionality of setting a player's leader status and verifying it.
     * <p>
     * This test ensures that:
     * - The `setLeader` method correctly assigns the leader status to the player.
     * - The `isLeader` method accurately reflects the updated leader status.
     * <p>
     * Verifies that the player is correctly designated and recognized as the leader
     * after invoking the `setLeader` method.
     */
    @Test
    public void testSetAndIsLeader (){
        player.setLeader();
        assertTrue(player.isLeader());
    }

    /**
     * Tests the functionality of the `setUsername` and `getUsername` methods in the Player class.
     * <p>
     * This test verifies that:
     * - The `setUsername` method correctly assigns the specified username to the player.
     * - The `getUsername` method accurately retrieves the assigned username.
     * <p>
     * Ensures that the username set through the `setUsername` method is the same as the username
     * retrieved using the `getUsername` method.
     */
    @Test
    public void testSetAndGetUsername (){
        String username = "BigNose";
        player.setUsername(username);
        assertEquals(username, player.getUsername());
    }

    /**
     * Tests the functionality of setting and retrieving the player's position on the board.
     * <p>
     * This test verifies that:
     * - The `setPosition` method correctly assigns the specified position to the player.
     * - The `getPosition` method accurately retrieves the assigned position.
     * <p>
     * Ensures that the position set using the `setPosition` method is the same as the
     * position retrieved using the `getPosition` method.
     */
    @Test
    public void testSetAndGetPosition () {
        player.setPosition(11);
        assertEquals(11, player.getPosition());
    }

    /**
     * Tests the addition and removal of credits for a Player instance.
     * <p>
     * This test verifies that:
     * - Credits can be successfully added to the player's account using the `addCredits` method.
     * - Credits can be successfully deducted from the player's account using the `removeCredits` method.
     * - The player's credit balance is updated correctly after performing the addition and removal operations.
     * <p>
     * Specifically:
     * - 10 credits are added to the player's account.
     * - 5 credits are removed from the player's account.
     * - The resulting credit balance is confirmed to be 5 using the `getCredits` method.
     */
    @Test
    public void testAddAndRemoveCredits1 () {
        player.addCredits(10);
        player.removeCredits(5);
        assertEquals(5, player.getCredits());
    }

    /**
     * Tests the addition and removal of credits for a Player instance when
     * credit removal exceeds the player's current credit balance.
     * <p>
     * This test verifies that:
     * - Credits are added to the player's account correctly using the `addCredits` method.
     * - When credits are removed using the `removeCredits` method and the amount exceeds
     *   the available balance, the player's credits are set to 0 and the deficit amount is returned.
     * - The player's credit balance is updated correctly and reflects the expected state after
     *   the operations.
     * <p>
     * Specifically:
     * - 10 credits are added to the player's account.
     * - An attempt is made to remove 15 credits, resulting in a deficit of 5 credits.
     * - The player's credit balance is confirmed to be 0 using the `getCredits` method.
     */
    @Test
    public void testAddAndRemoveCredits2 () {
        player.addCredits(10);
        assertEquals(5, player.removeCredits(15));
        assertEquals(0, player.getCredits());
    }

    /**
     * Tests the functionality of setting and retrieving the player's in-game status.
     * <p>
     * This test verifies that:
     * - The `setGameStatus` method correctly updates the player's in-game status.
     * - The `isInGame` method accurately reflects the updated in-game status.
     * <p>
     * Specifically:
     * - The player's in-game status is set to {@code true} using the `setGameStatus` method.
     * - The `isInGame` method is used to confirm that the player's in-game status
     *   has been updated correctly.
     */
    @Test
    public void testSetAndGetGameStatus (){
        player.setGameStatus(true);
        assertTrue (player.isInGame());
    }

    /**
     * Tests the functionality of the {@link PlayerColor} enumeration and its associated TUIPrint method.
     * <p>
     * This test validates the following:
     * - The values of the {@code PlayerColor} enumeration are not null.
     * - The {@code TUIPrint} method of each {@code PlayerColor} value returns the correct
     *   color-coded string representation for terminal display:
     *   - {@code PlayerColor.BLUE} outputs a blue-formatted string.
     *   - {@code PlayerColor.RED} outputs a red-formatted string.
     *   - {@code PlayerColor.GREEN} outputs a green-formatted string.
     *   - {@code PlayerColor.YELLOW} outputs a yellow-formatted string.
     */
    @Test
    public void testColors() {
        assertNotNull(PlayerColor.values());
        assertEquals("\u001B[34m" + "P1" + "\u001B[0m", PlayerColor.BLUE.TUIPrint());
        assertEquals("\u001B[31m" + "P2" + "\u001B[0m", PlayerColor.RED.TUIPrint());
        assertEquals("\u001B[32m" + "P3" + "\u001B[0m", PlayerColor.GREEN.TUIPrint());
        assertEquals("\u001B[33m" + "P4" + "\u001B[0m", PlayerColor.YELLOW.TUIPrint());
    }
}
