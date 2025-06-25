package it.polimi.ingsw.gc20.client.view.common.localmodel;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.io.Serializable;

/**
 * This class represents a player in the local model of the client view.
 * It contains the player's username, color, position, credits, and whether they are in the game.
 */
public class ViewPlayer  implements Serializable  {
    public int credits;
    public boolean inGame;
    public String username;
    public PlayerColor playerColor;
    public int position;

    /**
     * Constructor for ViewPlayer.
     *
     * @param player1      the username of the player
     * @param playerColor  the color of the player
     * @param i            the position of the player in the game
     */
    public ViewPlayer(String player1, PlayerColor playerColor, int i) {
        this.username = player1;
        this.playerColor = playerColor;
        this.position = i;
        this.credits = 0;
        this.inGame = true;
    }
}
