package it.polimi.ingsw.gc20.model.cards;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.Game;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    @Test
    void effect() {
        // Create an instance of OpenSpace
        OpenSpace openSpace = new OpenSpace();

        // Create a mock player and game
        Player player = new Player();
        Game game = new Game();

        // Set the firepower
        int power = 5;

        int initPos = player.getPosition();

        // Call the effect method
        openSpace.Effect(player, game, power);

        // Verify that the player's position has been updated correctly
        assertEquals(power, player.getPosition()-initPos);
    }
}