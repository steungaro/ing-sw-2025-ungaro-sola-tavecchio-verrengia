package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.gamesets.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.model.player.Player;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {

    @Test
    void getCannonFire() {
        Pirates pirates = new Pirates();
        List<Projectile> cannonFire = new ArrayList<>();
        Projectile cannonFire1 = new Projectile();
        cannonFire.add(cannonFire1);
        pirates.setCannonFire(cannonFire);

        assertEquals(cannonFire, pirates.getCannonFire());
    }

    @Test
    void getFirePower() {
        Pirates pirates = new Pirates();
        int firePower = 5;
        pirates.setFirePower(firePower);

        assertEquals(firePower, pirates.getFirePower());
    }

    @Test
    void getCredits() {
        Pirates pirates = new Pirates();
        int credits = 10;
        pirates.setCredits(credits);

        assertEquals(credits, pirates.getCredits());
    }

    @Test
    void getLostDays() {
        Pirates pirates = new Pirates();
        int lostDays = 3;
        pirates.setLostDays(lostDays);

        assertEquals(lostDays, pirates.getLostDays());
    }

    @Test
    void effectSuccess() {
        Pirates pirates = new Pirates();
        Player player = new Player();
        Game g = new Game();
        player.setPosition(10);
        List<Projectile> cannonFire = new ArrayList<>();
        Projectile cannonFire1 = new Projectile();
        cannonFire.add(cannonFire1);
        pirates.setCannonFire(cannonFire);
        pirates.setFirePower(5);
        pirates.setCredits(10);
        pirates.setLostDays(3);

        // Assuming effectSuccess method is implemented
        pirates.EffectSuccess(player, g);
        assertEquals(7, player.getPosition());
        assertEquals(10, player.getCredits());

        // Add assertions to verify the expected behavior
    }

    @Test
    void effectFailure() {
        Pirates pirates = new Pirates();
        List<Projectile> cannonFire = new ArrayList<>();
        Projectile cannonFire1 = new Projectile();
        cannonFire.add(cannonFire1);
        pirates.setCannonFire(cannonFire);

        assertEquals(cannonFire, pirates.EffectFailure());
    }
}