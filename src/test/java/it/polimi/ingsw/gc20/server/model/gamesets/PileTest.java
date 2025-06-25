package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.DuplicateComponentException;
import it.polimi.ingsw.gc20.server.model.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PileTest {
    private Pile pile;
    private Component component1;
    private Component component2;
    private Component component3;

    /**
     * Sets up the test environment before each test is executed.
     * Initializes a new instance of the Pile class and assigns example components
     * to the component1, component2, and component3 fields.
     * This ensures a consistent starting state for all test methods in the test class.
     */
    @BeforeEach
    void setUp() {
        pile = new Pile();

        component1 = new Cabin();
        component2 = new Cannon();
        component3 = new Pipes();
    }

    /**
     * Tests the default constructor of the {@code Pile} class.
     * This method verifies that creating a new instance of {@code Pile}
     * initializes the viewed and unviewed lists to empty, non-null lists.
     * It asserts the following:
     * - The {@code getViewed()} method returns a non-null list.
     * - The {@code getUnviewed()} method returns a non-null list.
     * - The returned viewed list is empty.
     * - The returned unviewed list is empty.
     */
    @Test
    void defaultConstructor (){
        Pile newPile = new Pile();
        assertNotNull(newPile.getViewed());
        assertNotNull(newPile.getUnviewed());
        assertTrue(newPile.getUnviewed().isEmpty());
        assertTrue(newPile.getViewed().isEmpty());
    }

    /**
     * Tests the addition, retrieval, and removal of components from the unviewed component list in the {@code Pile} class.
     * <p>
     * This method verifies the following functionalities:
     * - Adding a collection of components to the unviewed list using {@code addUnviewed()}.
     * - Retrieving the unviewed list using {@code getUnviewed()}, ensuring the added components are correctly returned.
     * - Successfully removing a component from the unviewed list using {@code removeUnviewed()}, ensuring the component is no longer present in the list.
     * - Throwing a {@code ComponentNotFoundException} when attempting to remove a component not in the unviewed list.
     * - Throwing a {@code ComponentNotFoundException} when attempting to remove a component not in the viewed list.
     * - Successfully adding a new component to the viewed list without exceptions using {@code addViewed()}.
     * - Throwing a {@code DuplicateComponentException} when attempting to add a duplicate component to the viewed list.
     * <p>
     * The method ensures proper exception handling and validates that components are managed correctly
     * across the unviewed and viewed lists in the {@code Pile} implementation.
     */
    @Test
    void testAddAndGetAndRemoveUnviewed (){
        List<Component> components = new ArrayList<>();
        components.add(component1);
        components.add(component2);
        pile.addUnviewed(components);
        assertEquals(components, pile.getUnviewed());
        try {
            pile.removeUnviewed(component1);
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
        assertFalse(pile.getUnviewed().contains(component1));
        Component newComponent = new Cabin();
        assertThrows(ComponentNotFoundException.class, () -> pile.removeUnviewed(newComponent));
        assertThrows(ComponentNotFoundException.class, () -> pile.removeViewed(newComponent));
        try {
            pile.addViewed(newComponent);
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
        assertThrows(DuplicateComponentException.class, () -> pile.addViewed(newComponent));

    }

    /**
     * Tests the addition, retrieval, and removal of components from the viewed component list in the {@code Pile} class.
     * <p>
     * This method ensures the following functionalities:
     * - Adding components to the viewed list using {@code addViewed()}, where the components
     *   are correctly added and no exceptions are thrown if they are not duplicates.
     * - Retrieving the viewed list using {@code getViewed()}, verifying that components added
     *   are correctly contained in the list.
     * - Removing components from the viewed list using {@code removeViewed()}, ensuring that the
     *   components are successfully removed.
     * - Verifying that the viewed list is empty after all added components are removed.
     * <p>
     * The test ensures that the viewed list in the {@code Pile} class behaves as expected
     * when components are added, retrieved, and removed. Any exceptions are caught to prevent
     * test failures due to unexpected issues.
     */
    @Test
    void testGetAndAddAndRemoveViewed(){
        try {
            pile.addViewed(component3);
            pile.addViewed(component1);
            assertTrue(pile.getViewed().contains(component1));
            assertTrue(pile.getViewed().contains(component3));
            pile.removeViewed(component1);
            pile.removeViewed(component3);
            assertTrue(pile.getViewed().isEmpty());
        } catch (Exception _) {
            fail("Exception should not be thrown");
        }
    }
}
