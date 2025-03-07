package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

/**
 * @author GC20
 */
public class Game {

    private List<Player> players;
    private Board board;
    private Integer gameID;
    private Pile pile;
    private Map<Cargo, Integer> cargoAvailable;

    /**
     * Default constructor
     */
    public Game() {
        this.players = new ArrayList<Player>();
        this.board = null;
        this.gameID = null;
        this.pile = null;
        this.cargoAvailable = new HashMap<Cargo, Integer>();
    }
    /** add function for players
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
    public Integer getID() {
        return this.gameID;
    }

    /** set function for gameID
     * @param id gameId to set
     */
    public void setID(Integer id) {
        this.gameID = id;
    }

    /** get function for cargoAvailable
     * @param c cargo color to get availability
     * @return Integer
     */
    public Integer getCargoAvailable(Cargo c) {
        // TODO implement here
        return null;
    }

    /** set function for cargoAvailable
     * @param c color of the cargo to set, i number of cargo to set as available
     */
    public void setCargoAvailable(Cargo c, Integer i) {
        // TODO implement here
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
    private boolean isOccupied(int position) {
        for (Player p : players) {
            if (p.getPosition() == position) {
                return true;
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
                    spaceMoved++;
                }// if next space occupied I go to the first free space
                else {
                    int i = p.getPosition() + 1;
                    while (isOccupied(i)) {
                        i++;
                    }
                    p.setPosition(i);
                    spaceMoved++;
                }
            } else { //moving backward
                // If previous space is not occupied
                if (!isOccupied(p.getPosition() - 1)) {
                    p.setPosition(p.getPosition() - 1);
                    spaceMoved++;
                }//if previous space occupied I go to the first free space
                else {
                    int i = p.getPosition() - 1;
                    while (isOccupied(i)) {
                        i--;
                    }
                    p.setPosition(i);
                    spaceMoved++;
                }
            }

        }
    }
}