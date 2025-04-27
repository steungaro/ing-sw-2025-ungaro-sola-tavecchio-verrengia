package it.polimi.ingsw.gc20.model.components;

import it.polimi.ingsw.gc20.server.model.components.LifeSupport;
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
