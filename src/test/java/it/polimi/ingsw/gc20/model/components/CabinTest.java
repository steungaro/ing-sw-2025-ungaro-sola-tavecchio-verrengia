package it.polimi.ingsw.gc20.model.components;

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
    }

    @Test
    void testAddAndRemoveSupport(){
        LifeSupport lifeSupport = new LifeSupport();
        lifeSupport.setColor(AlienColor.BROWN);
        cabin.addSupport(lifeSupport);
        assertEquals(AlienColor.BROWN, cabin.getCabinColor());
        cabin.setAlien(AlienColor.BROWN);
        try {
            cabin.removeSupport(lifeSupport);
        }catch (Exception e){

        }
        assertFalse(cabin.getAlien());
    }


}
