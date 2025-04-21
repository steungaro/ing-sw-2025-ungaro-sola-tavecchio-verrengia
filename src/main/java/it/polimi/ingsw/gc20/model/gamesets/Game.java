package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.exceptions.DieNotRolledException;
import it.polimi.ingsw.gc20.model.player.Player;


import java.util.*;

/**
 * @author GC20
 */
public class Game {

    private final List<Player> players;
    private Board board;
    private String gameID;
    private Pile pile;
    private final Die[] dice;

    /**
     * Default constructor
     */
    public Game() {
        this.players = new ArrayList<>();
        this.board = null;
        this.gameID = null;
        this.pile = null;
        dice = new Die[2];
        this.dice[0]= new Die();
        this.dice[1] = new Die();
    }
    /** add function for players and add the player to the stallBox in the board
     * @param p player to add
     */
    public void addPlayer(Player p){

        players.add(p);
    }

    /** get function for players
     * @return List<Player>
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /** remove function for players
     * @param p player to remove
     */
    public void removePlayer(Player p){
        players.remove(p);
    }

    /** add function for board
     * @param board board to add
     */
    public void addBoard(Board board){
        this.board = board;
    }

    /** get function for board
     * @return Board
     */
    public Board getBoard() {
        return this.board;
    }

    /** get function for gameID
     * @return Integer
     */
    public String getID() {
        return this.gameID;
    }

    /** set function for gameID
     * @param id gameId to set
     */
    public void setID(String id) {
        this.gameID = id;
    }

    /** get function for pile
     * @return Pile
     */
    public Pile getPile() {
        return this.pile;
    }

    /** set function for pile
     * @param pile pile of components to set
     */
    public void setPile(Pile pile) {
        this.pile = pile;
    }

    /** function that checks if a position is occupied by a player
     * @param position position to check
     * @return boolean
     */
    public boolean isOccupied(int position) {
        int normalizedPosition = position % this.board.getSpaces();
        for (Player p : players) {
            if (p.isInGame()){
                if ((p.getPosition() % this.board.getSpaces()) == normalizedPosition) {
                    return true;
                }
            }
        }
        return false;
    }

    /** function that move the player in the board
     * @param p player to move, n number of spaces to move
     */
    public void move (Player p, Integer n) {
        int spaceMoved = 0;
        //while the player has not moved n (can be negative) spaces
        while (spaceMoved < Math.abs(n)) {
            //moving forward
            if (n > 0) {
                //if next space is not occupied
                if (!isOccupied(p.getPosition() + 1)) {
                    p.setPosition(p.getPosition() + 1);
                }// if next space occupied I go to the first free space
                else {
                    int i = p.getPosition() + 1;
                    while (isOccupied(i)) {
                        i++;
                    }
                    p.setPosition(i);
                }
            } else { //moving backward
                // If previous space is not occupied
                if (!isOccupied(p.getPosition() - 1)) {
                    p.setPosition(p.getPosition() - 1);
                }//if previous space occupied I go to the first free space
                else {
                    int i = p.getPosition() - 1;
                    while (isOccupied(i)) {
                        i--;
                    }
                    p.setPosition(i);
                }
            }
            spaceMoved++;

        }
    }

    /** function that sorts the players by position in the board
     * first to last
     */
    public void sortPlayerByPosition() {
        players.sort((p1, p2) -> p2.getPosition() - p1.getPosition());
    }
    /** function that rolls the dice
     * @return int sum of the two dice
     */
    public int rollDice() {
        return dice[0].rollDie() + dice[1].rollDie();
    }

    /** function that returns the last rolled value of the dice without rolling them
     * @return int sum of the two dice
     * @throws DieNotRolledException if the dice have not been rolled yet
     */
    public int lastRolled() throws DieNotRolledException {
        return dice[0].getLastRolled() + dice[1].getLastRolled();
    }
}