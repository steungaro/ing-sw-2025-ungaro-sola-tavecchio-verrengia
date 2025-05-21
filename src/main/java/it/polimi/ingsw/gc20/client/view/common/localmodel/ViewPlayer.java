package it.polimi.ingsw.gc20.client.view.common.localmodel;

import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.io.Serializable;

public class ViewPlayer  implements Serializable  {
    public int credits;
    public boolean inGame;
    public String username;
    public PlayerColor playerColor;
    public int position;

    public ViewPlayer(String player1, PlayerColor playerColor, int i) {
        this.username = player1;
        this.playerColor = playerColor;
        this.position = i;
        this.credits = 0;
        this.inGame = true;
    }
}
