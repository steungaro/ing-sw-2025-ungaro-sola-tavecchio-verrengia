package it.polimi.ingsw.gc20.server.model.components;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LifeSupportTest {
    LifeSupport ls;

    @BeforeEach
    void setUp() {
        ls = new LifeSupport();
    }

    @Test
    void isLifeSupport() {
        assertTrue(ls.isLifeSupport());
    }
}
