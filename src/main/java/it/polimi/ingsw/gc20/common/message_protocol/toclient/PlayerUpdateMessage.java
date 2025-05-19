package it.polimi.ingsw.gc20.common.message_protocol.toclient;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

//TODO wait for the view structure to be defined
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
        //TODO ristampo anche la board, modifico la posizione del player in modulo
        ClientGameModel model = ClientGameModel.getInstance();
        ViewPlayer targetPlayer = model.getPlayers().stream()
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
