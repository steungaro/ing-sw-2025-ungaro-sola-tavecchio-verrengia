package it.polimi.ingsw.gc20.server.model.cards;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.states.State;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCardTest {
    private AdventureCard adventureCard;

    /**
     * Sets up the AdventureCard instance before each test.
     */
    @BeforeEach
    void setUp() {
        adventureCard = new AdventureCard() {
            // Anonymous subclass for testing abstract class
        };
    }

    /**
     * Test set and get methods for the level attribute of AdventureCard.
     */
    @Test
    void testSetAndGetLevel() {
        adventureCard.setLevel(3);
        assertEquals(3, adventureCard.getLevel());
    }

    /**
     * Test set and get methods for the IDCard attribute of AdventureCard.
     */
    @Test
    void testSetAndGetIDCard() {
        adventureCard.setIDCard(42);
        assertEquals(42, adventureCard.getIDCard());
    }

    /**
     * Test set and get methods for the name attribute of AdventureCard.
     */
    @Test
    void testSetAndGetName() {
        adventureCard.setName("TestCard");
        assertEquals("TestCard", adventureCard.getName());
    }

    /**
     * Test set and get methods for the played attribute of AdventureCard.
     */
    @Test
    void testSetAndGetPlayed() {
        adventureCard.setPlayed(true);
        assertTrue(adventureCard.isPlayed());
    }

    /**
     * Test set and get methods for the crew attribute of AdventureCard.
     */
    @Test
    void testSetAndGetCrew() {
        adventureCard.setCrew(5);
        assertEquals(5, adventureCard.getCrew());
    }

    /**
     * Test set and get methods for the credit attribute of AdventureCard.
     */
    @Test
    void testSetAndGetCredits() {
        adventureCard.setCredits(100);
        assertEquals(100, adventureCard.getCredits());
    }

    /**
     * Test set and get methods for the lostDays attribute of AdventureCard.
     */
    @Test
    void testSetAndGetLostDays() {
        adventureCard.setLostDays(2);
        assertEquals(2, adventureCard.getLostDays());
    }

    /**
     * Test set and get methods for the reward attribute of AdventureCard.
     */
    @Test
    void testSetAndGetReward() {
        List<CargoColor> reward = List.of(CargoColor.RED, CargoColor.BLUE);
        adventureCard.setReward(reward);
        assertEquals(reward, adventureCard.getReward());
    }

    /**
     * Test set and get methods for the lostCargo attribute of AdventureCard.
     */
    @Test
    void testSetAndGetLostCargo() {
        adventureCard.setLostCargo(3);
        assertEquals(3, adventureCard.getLostCargo());
    }

    /**
     * Test set and get methods for the projectile attribute of AdventureCard.
     */
    @Test
    void testSetAndGetProjectiles() {
        List<Projectile> projectiles = List.of(new Projectile(), new Projectile());
        adventureCard.setProjectiles(projectiles);
        assertEquals(projectiles, adventureCard.getProjectiles());
    }

    /**
     * Test set and get methods for the firePower attribute of AdventureCard.
     */
    @Test
    void testSetAndGetFirePower() {
        adventureCard.setFirePower(10);
        assertEquals(10, adventureCard.getFirePower());
    }

    /**
     * Test set and get methods for the planet attribute of AdventureCard.
     */
    @Test
    void testSetAndGetPlanets() {
        List<Planet> planets = List.of(new Planet(), new Planet());
        adventureCard.setPlanets(planets);
        assertEquals(planets, adventureCard.getPlanets());
    }


    /**
     * Test the playCard method of AdventureCard.
     */
    @Test
    void testPlayCard() {
        adventureCard.playCard();
        assertTrue(adventureCard.isPlayed());
    }

    /**
     * Test the getCardType method of AdventureCard.
     * Used for the combatZone Card
     */
    @Test
    void testCombatType() {
        adventureCard.setCrew(5);
        assertEquals(0, adventureCard.combatType());

        adventureCard.setCrew(0);
        assertEquals(1, adventureCard.combatType());
    }

    /**
     * Test the setState method of AdventureCard.
     * This method sets the state of the game controller based on the adventure card's name.
     */
    @Test
    void testSetState() throws InvalidStateException {
        List<String> players = List.of("Player1", "Player2");
            GameController controller = new GameController("1234", "1234", players, 2);
        GameModel model = controller.getModel();

        adventureCard.setName("Slavers");

        // Ensure no exceptions are thrown
        assertDoesNotThrow(() -> adventureCard.setState(controller, model));
        assertEquals("SlaversState", controller.getState().getClass().getSimpleName());


        adventureCard.setName("Test");
        // Ensure the exception is caught internally and does not propagate
        assertDoesNotThrow(() -> adventureCard.setState(controller, model));

        assertEquals("SlaversState", controller.getState().getClass().getSimpleName());

        adventureCard.setName("Pirates");

        // Ensure no exceptions are thrown
        assertDoesNotThrow(() -> adventureCard.setState(controller, model));
        assertEquals("PiratesState", controller.getState().getClass().getSimpleName());


        adventureCard.setName("AbandonedShip");

        adventureCard.setCredits(100);
        adventureCard.setLostDays(2);
        adventureCard.setState(controller, model);
        State s = controller.getState();
        assertEquals("AbandonedShipState", s.getClass().getSimpleName());


        adventureCard.setName("CombatZone");

        adventureCard.setFirePower(3);
        adventureCard.setCrew(5);
        adventureCard.setState(controller, model);
        s = controller.getState();
        assertEquals("CombatZone0State", s.getClass().getSimpleName());

        adventureCard.setName("CombatZone");
        adventureCard.setCrew(0);
        adventureCard.setState(controller, model);
        s = controller.getState();
        assertEquals("CombatZone1State", s.getClass().getSimpleName());
    }
}
