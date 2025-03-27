package it.polimi.ingsw.gc20.model.cards;

import it.polimi.ingsw.gc20.model.ship.Ship;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.Game;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.ship.NormalShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipTest{

    private AbandonedShip card;
    private Player player1;
    private Game game;
    private Cabin cabin;
    private Cabin cabin2;
    private NormalShip ship;
    @BeforeEach
    void setUp(){
        // Create a new NormalShip
        ship = new NormalShip();


        cabin = new Cabin();
        cabin.setColor(AlienColor.NONE);

        cabin2 = new Cabin();
        cabin2.setColor(AlienColor.BROWN);
        // Add components to ship at valid positions
        ship.addComponent(cabin, 2, 4);
        ship.addComponent(cabin2, 3, 4);

        ship.setColorHostable(AlienColor.BROWN);
        ship.addAlien(AlienColor.BROWN, cabin2);
        ship.initAstronauts();
        player1 = new Player();
        player1.setPosition(4);
        player1.setShip(ship);

        game = new Game();
        card = new AbandonedShip();
        card.setCredits(3);
        card.setLostCrew(3);
        card.setLostDays(1);
    }

    @Test
    void effect() {
        assertEquals(1, card.getLostDays());
        assertEquals(3, card.getLostCrew());
        assertEquals(3, card.getCredits());
        assertEquals(2, cabin.getOccupants());
        assertEquals(player1.getShip().crew(), 5);
        List<Cabin> cabins = new ArrayList<>();
        cabins.add(cabin);
        cabins.add(cabin2);
        cabins.add(cabin);
        card.Effect(player1, game, cabins);
        assertEquals(3, player1.getCredits());
        assertEquals(3, player1.getPosition());
    }
}