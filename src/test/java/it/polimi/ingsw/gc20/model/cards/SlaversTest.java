package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import it.polimi.ingsw.gc20.model.ship.Ship;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {

    @Test
    void setFirePower() {
        Slavers slavers = new Slavers();
        int firePower = 5;
        slavers.setFirePower(firePower);
        assertEquals(firePower, slavers.getFirePower());
    }

    @Test
    void getFirePower() {
        Slavers slavers = new Slavers();
        int firePower = 5;
        slavers.setFirePower(firePower);
        assertEquals(firePower, slavers.getFirePower());
    }

    @Test
    void setLostMembers() {
        Slavers slavers = new Slavers();
        int lostMembers = 3;
        slavers.setLostMembers(lostMembers);
        assertEquals(lostMembers, slavers.getLostMembers());
    }

    @Test
    void getLostMembers() {
        Slavers slavers = new Slavers();
        int lostMembers = 3;
        slavers.setLostMembers(lostMembers);
        assertEquals(lostMembers, slavers.getLostMembers());
    }

    @Test
    void setReward() {
        Slavers slavers = new Slavers();
        int reward = 10;
        slavers.setReward(reward);
        assertEquals(reward, slavers.getReward());
    }

    @Test
    void getReward() {
        Slavers slavers = new Slavers();
        int reward = 10;
        slavers.setReward(reward);
        assertEquals(reward, slavers.getReward());
    }

    @Test
    void setLostDays() {
        Slavers slavers = new Slavers();
        int lostDays = 2;
        slavers.setLostDays(lostDays);
        assertEquals(lostDays, slavers.getLostDays());
    }

    @Test
    void getLostDays() {
        Slavers slavers = new Slavers();
        int lostDays = 2;
        slavers.setLostDays(lostDays);
        assertEquals(lostDays, slavers.getLostDays());
    }

    @Test
    void effectFailure() {
        // Test the effectFailure method
        Slavers slavers = new Slavers();
        Player player = new Player();
        Ship ship = new NormalShip();
        List<Cabin> cabinList = new ArrayList<>();
        Cabin cabin = new Cabin();
        cabin.setAstronauts(2);
        Cabin cabin2 = new Cabin();
        cabin2.setAstronauts(2);
        cabinList.add(cabin);
        cabinList.add(cabin2);
        player.setShip(ship);

        slavers.EffectFailure(player, cabinList);

        assertEquals(1, cabin.getAstronauts());
        assertEquals(1, cabin2.getAstronauts());
    }

    @Test
    void effectSuccess() {
        Slavers slavers = new Slavers();
        Player player = new Player();
        Game game = new Game();
        slavers.setLostDays(3);
        slavers.setReward(10);

        float initialCredits = player.getCredits();
        player.setPosition(10);

        slavers.EffectSuccess(player, game);

        assertEquals(7, player.getPosition());
        assertEquals(10, player.getCredits());
    }
}