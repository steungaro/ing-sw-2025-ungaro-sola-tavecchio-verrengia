package it.polimi.ingsw.gc20.model.player;

import it.polimi.ingsw.gc20.model.ship.Ship;

/**
 * @author GC20
 */
public class Player {

    private PlayerColor color;
    private String username;
    private Ship ship;
    private Integer credits;
    private boolean leader;
    private Integer posInBoard;
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



    /** Function that return true if the player is the leader
     * @return Boolean leader
     */
    public Boolean isLeader () {
        return this.leader;
    }

    /** set function for the color of the player
     * @param c  color of the player
     */
    public void setColor(PlayerColor c) {
        this.color = c;
    }

    /** get function for the color of the player
     * @return PlayerColor color
     */
    public PlayerColor getColor() {
        return this.color;
    }

    /** get function for the ship of the player
     * @return Ship ship
     */
    public Ship getShip() {
        return this.ship;
    }

    /** set function for the ship of the player
     * @param s ship of the player
     */
    public void setShip(Ship s) {
       this.ship = s;
    }

    /**get function for the username of the player
     * @return String username
     */
    public String getUsername() {
        return this.username;
    }

    /** set function for the username of the player
     * @param s username
    */
    public void setUsername(String s) {
        this.username = s;
    }

    /** set function if player is the leader
     */
    public void setLeader() {
        this.leader = true;
    }


    /** function to add credits to the player
     * @param c number of credits to add
     */
    public void addCredits(Integer c) {
        this.credits += c;
    }

    /** function to remove credits to the player
     * @param c number of credits to remove
     * @return Integer debit (the difference between the credits to remove and the credits of the player)
     */
    public Integer removeCredits(Integer c) throws IllegalArgumentException {
        Integer debit = 0;
        this.credits -= c;

        //if the player doesn't have enough credits, the credits are set to 0 and the difference is returned
        if (this.credits < 0) {
            debit = - this.credits;
            this.credits = 0;
        }
        return debit;
    }

    /** get function for the credits of the player
     * @return Integer credits
     */
    public Integer getCredits() {
        return this.credits;
    }


    /** getfunction for the position of the player in the board
     * @return Integer position
     */
    public Integer getPosition() {
        return this.posInBoard;
    }

    /** set function for the position of the player in the board
     * @param p position
     */
    public void setPosition(Integer p) {
        this.posInBoard = p;
    }

    /** function to determine if the player is in the game
     * @return Boolean inGame
     */
    public Boolean isInGame() {
        return this.inGame;
    }

    /** function to set if the player is in the game
     * @param b game status
     */
    public void setGameStatus(Boolean b) {
        this.inGame = b;
    }
}