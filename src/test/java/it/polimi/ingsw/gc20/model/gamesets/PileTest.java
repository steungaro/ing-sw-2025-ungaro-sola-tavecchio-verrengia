package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.components.*;
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

    @BeforeEach
    void setUp() {
        pile = new Pile();

        component1 = new Cabin();
        component2 = new Cannon();
        component3 = new Pipes();
    }

    @Test
    void defaultConstructor (){
        Pile newPile = new Pile();
        assertNotNull(newPile.getViewed());
        assertNotNull(newPile.getUnviewed());
        assertTrue(newPile.getUnviewed().isEmpty());
        assertTrue(newPile.getViewed().isEmpty());
    }

    @Test
    void testAddAndGetAndRemoveUnviewed (){
        List<Component> components = new ArrayList<>();
        components.add(component1);
        components.add(component2);
        pile.addUnviewed(components);
        assertEquals(components, pile.getUnviewed());
        pile.removeUnviewed(component1);
        assertFalse(pile.getUnviewed().contains(component1));
    }

    @Test
    void testGetAndAddAndRemoveViewed(){
        pile.addViewed(component3);
        pile.addViewed(component1);
        assertTrue(pile.getViewed().contains(component1));
        assertTrue(pile.getViewed().contains(component3));
        pile.removeViewed(component1);
        pile.removeViewed(component3);
        assertTrue(pile.getViewed().isEmpty());
    }
}
