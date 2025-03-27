package it.polimi.ingsw.gc20.model.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CargoHoldTest {
    private CargoHold cargoHold;
    @BeforeEach
    void setUp() {
        cargoHold = new CargoHold();
    }

    @Test
    void testSetGetAdnAvailableSlots(){
        cargoHold.setSlots(5);
        assertEquals(5, cargoHold.getSlots());
        assertEquals(5, cargoHold.getAvailableSlots());
    }


    @Test
    void testSetAndGetCargoHeld(){
        cargoHold.setSlots(5);
        cargoHold.setCargoHeld();
    }

}
