package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.util.Arrays;

/**
 * This message is sent to the client to update the player's information.
 * It contains the username of the player, the number of credits added,
 * whether the player is in-game, their color, and their position on the board.
 */
public record PlayerUpdateMessage(
        String username, // The username of the receiving client (broadcast to all clients)
        int creditsAdded,
        Boolean inGame,
        PlayerColor color,
        int posInBoard //needs to be adapted to the board space (position % spaces)
) implements Message {
    @Override
    public String toString() {
        return username + " has been updated: " +
                "creditsAdded=" + creditsAdded +
                ", inGame=" + inGame +
                ", color=" + color +
                ", posInBoard=" + posInBoard;
    }

    @Override
    public void handleMessage() {
        // Handle the player update message (client side)
        ClientGameModel model = ClientGameModel.getInstance();
        ViewPlayer targetPlayer = Arrays.stream(model.getPlayers())
                .filter(player -> player.username.equals(username))
                .findFirst()
                .orElse(null);

        if (targetPlayer != null) {
            // Update the player's information in the game model
            targetPlayer.credits = targetPlayer.credits + creditsAdded;
            targetPlayer.inGame = inGame;
            targetPlayer.playerColor = color;
            targetPlayer.position = posInBoard;

        }
        else{
            // If the player is not found, message error
            System.out.println("Player " + username + " not found in the game model.");
        }

    }
}
