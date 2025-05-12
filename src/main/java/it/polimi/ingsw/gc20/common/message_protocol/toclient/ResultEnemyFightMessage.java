package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

public record ResultEnemyFightMessage(
        String username, // The username of the receiving client (broadcast to all clients)
        String enemyName, // The name of the enemy
        String result // The result of the fight (e.g., "victory", "defeat", "draw")
) implements Message {
    @Override
    public String toString() {
        return username + " fought against " + enemyName + " and the result is: " + result;
    }

    @Override
    public void handleMessage() {
        // Handle the result of the enemy fight message (client side)
    }

}
