package it.polimi.ingsw.gc20.server.model.player;

import it.polimi.ingsw.gc20.server.model.ship.Ship;

import java.io.Serializable;

/**
 * Represents a player in the game with attributes such as color, username, ship,
 * credits, leadership status, position on the board, and in-game status.
 */
public class Player implements Serializable {

    private PlayerColor color;
    private String username;
    private Ship ship;
    private int credits;
    private boolean leader;
    private int posInBoard;
    private boolean inGame;

    /**
     * Default constructor
     */
    public Player() {
        this.credits = 0;
        this.color = null;
        this.ship = null;
        this.username = "";
        this.posInBoard = 0;
        this.leader = false;
        this.inGame = false;
    }

    /**
     * Creates a new Player instance populated with a subset of the current Player's data for public access.
     * Sensitive fields such as credits are reset to default values, ensuring only non-critical information is shared.
     *
     * @return a Player instance containing publicly accessible fields such as color, username, ship, 
     *         leader status, position on the board, and game status, while credits are set to 0.
     */
    public Player getPublicData() {
        Player p = new Player();
        p.setColor(this.color);
        p.setUsername(this.username);
        p.setShip(this.ship);
        p.leader = this.leader;
        p.credits = 0;
        p.posInBoard = this.posInBoard;
        p.inGame = this.inGame;
        return p;
    }

    /**
     * Determines whether the player is the leader.
     *
     * @return {@code true} if the player is the leader; {@code false} otherwise.
     */
    public boolean isLeader () {
        return this.leader;
    }

    /**
     * Sets the color of the player.
     *
     * @param c the color to be assigned to the player
     */
    public void setColor(PlayerColor c) {
        this.color = c;
    }

    /**
     * Retrieves the color associated with the player.
     *
     * @return the player's color as a {@code PlayerColor}
     */
    public PlayerColor getColor() {
        return this.color;
    }

    /**
     * Retrieves the ship associated with the player.
     *
     * @return the player's ship as a {@code Ship} object
     */
    public Ship getShip() {
        return this.ship;
    }

    /**
     * Assigns a {@code Ship} object to the player.
     *
     * @param s the {@code Ship} to be assigned to the player
     */
    public void setShip(Ship s) {
       this.ship = s;
    }

    /**
     * Retrieves the username associated with the player.
     *
     * @return the player's username as a String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username for the player.
     *
     * @param s the username to be assigned to the player
     */
    public void setUsername(String s) {
        this.username = s;
    }

    /**
     * Sets the player's leader status to {@code true}.
     * Indicates that the player is designated as the leader.
     */
    public void setLeader() {
        this.leader = true;
    }


    /**
     * Adds the specified number of credits to the player's account.
     *
     * @param c the number of credits to be added to the player's account
     */
    public void addCredits(int c) {
        this.credits += c;
    }

    /**
     * Removes the specified number of credits from the player. If the amount to be removed exceeds 
     * the player's current credits, the player's credits are set to 0 and the excess amount 
     * (the deficit) is returned.
     *
     * @param c the number of credits to remove from the player
     * @return the deficit amount if the player's credits go below 0, or 0 if the player had sufficient credits
     */
    public int removeCredits(int c){
        int debit = 0;
        this.credits -= c;

        //if the player doesn't have enough credits, the credits are set to 0 and the difference is returned
        if (this.credits < 0) {
            debit = - this.credits;
            this.credits = 0;
        }
        return debit;
    }

    /**
     * Retrieves the number of credits currently available to the player.
     *
     * @return the player's current credits as an integer
     */
    public int getCredits() {
        return this.credits;
    }


    /**
     * Retrieves the player's current position on the board.
     *
     * @return the position of the player on the board as an integer
     */
    public int getPosition() {
        return this.posInBoard;
    }

    /**
     * Sets the position of the player on the board.
     *
     * @param p the position to be assigned to the player on the board
     */
    public void setPosition(int p) {
        this.posInBoard = p;
    }

    /**
     * Determines whether the player is currently in the game.
     *
     * @return {@code true} if the player is in the game; {@code false} otherwise.
     */
    public boolean isInGame() {
        return this.inGame;
    }

    /**
     * Sets the game status of the player.
     *
     * @param b a {@code boolean} value indicating the player's game status. 
     *          {@code true} if the player is currently in the game, 
     *          {@code false} otherwise.
     */
    public void setGameStatus(boolean b) {
        this.inGame = b;
    }
}