package it.polimi.ingsw.gc20.model.cards;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmTest {

    @Test
    void setMeteors() {
        // Create an instance of MeteorSwarm
        MeteorSwarm meteorSwarm = new MeteorSwarm();

        // Create a list of Projectiles
        List<Projectile> meteors = new ArrayList<>();
        meteors.add(new Projectile());
        meteors.add(new Projectile());

        // Set the meteors
        meteorSwarm.setMeteors(meteors);

        // Verify that the meteors are set correctly
        assertEquals(meteors, meteorSwarm.getMeteors());
    }

    @Test
    void getMeteors() {
        // Create an instance of MeteorSwarm
        MeteorSwarm meteorSwarm = new MeteorSwarm();

        // Create a list of Projectiles
        List<Projectile> meteors = new ArrayList<>();
        meteors.add(new Projectile());
        meteors.add(new Projectile());

        // Set the meteors
        meteorSwarm.setMeteors(meteors);

        // Verify that the meteors are retrieved correctly
        assertEquals(meteors, meteorSwarm.getMeteors());
    }

    @Test
    void effect() {
        // Create an instance of MeteorSwarm
        MeteorSwarm meteorSwarm = new MeteorSwarm();

        // Create a list of Projectiles
        List<Projectile> meteors = new ArrayList<>();
        meteors.add(new Projectile());
        meteors.add(new Projectile());

        // Set the meteors
        meteorSwarm.setMeteors(meteors);

        // Verify that the effect returns the correct list of meteors
        assertEquals(meteors, meteorSwarm.Effect());
    }
}