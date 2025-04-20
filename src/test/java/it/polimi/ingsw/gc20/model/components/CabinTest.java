package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.exceptions.DeadAlienException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CabinTest {
    private Cabin cabin;

    @BeforeEach
    void setUp() {
        cabin = new Cabin();
    }

    @Test
    void testSetAndGetAstronauts() {
        cabin.setAstronauts(1);
        assertEquals(1, cabin.getAstronauts());
        cabin.unloadAstronaut();
        assertEquals(0, cabin.getAstronauts());
    }

    @Test
    void testSetAndGetAlien(){
        cabin.setColor(AlienColor.BROWN);
        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
        cabin.setAlien(AlienColor.BROWN);
        assertTrue(cabin.getAlien());
        assertEquals(AlienColor.BROWN, cabin.getAlienColor());
        cabin.unloadAlien();
        assertFalse(cabin.getAlien());
        cabin.setAstronauts(2);
        assertThrows(IllegalArgumentException.class, ()-> cabin.setAlien(AlienColor.BROWN));
        cabin.unloadAstronaut();
        cabin.unloadAstronaut();
        assertThrows(IllegalArgumentException.class, ()-> cabin.setAlien(AlienColor.PURPLE));
        cabin.setColor(AlienColor.PURPLE);
        assertEquals(AlienColor.BOTH, cabin.getCabinColor());
    }

    @Test
    void testAddAndRemoveSupport(){
        LifeSupport lifeSupport = new LifeSupport();
        lifeSupport.setColor(AlienColor.BROWN);
        cabin.addSupport(lifeSupport);
        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
        cabin.setAlien(AlienColor.BROWN);
        assertThrows(DeadAlienException.class, ()-> cabin.removeSupport(lifeSupport));
        assertFalse(cabin.getAlien());
        LifeSupport lifeSupport2 = new LifeSupport();
        lifeSupport2.setColor(AlienColor.PURPLE);
        cabin.addSupport(lifeSupport2);
        cabin.addSupport(lifeSupport);
        assertEquals(AlienColor.BOTH, cabin.getCabinColor());
        try {
            cabin.removeSupport(lifeSupport);
        } catch (DeadAlienException e) {

        }
        assertEquals (AlienColor.PURPLE, cabin.getCabinColor());
    }


}
