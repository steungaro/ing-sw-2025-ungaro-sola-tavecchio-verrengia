package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StartingCabinTest {
    StartingCabin cabin;

    @BeforeEach
    void setUp() {
        cabin = new StartingCabin();
    }

    @Test
    void alien(){
        try {
            cabin.setAlien(AlienColor.BROWN);
        } catch (InvalidAlienPlacement e) {
            assertEquals("Cannot set an alien in the starting cabin", e.getMessage());
        }
        assertFalse(cabin.getAlien());
        assertEquals(AlienColor.NONE, cabin.getAlienColor());
        try {
            cabin.unloadAlien();
        } catch (InvalidAlienPlacement e) {
            assertEquals("Cannot unload an alien in the starting cabin", e.getMessage());
        }
    }

    @Test
    void Support(){
        LifeSupport ls = new LifeSupport();
        ls.setColor(AlienColor.BROWN);
        cabin.addSupport(ls);
        assertEquals(AlienColor.NONE, cabin.getCabinColor());
        cabin.removeSupport(ls);
        assertEquals(AlienColor.NONE, cabin.getCabinColor());
    }


}
