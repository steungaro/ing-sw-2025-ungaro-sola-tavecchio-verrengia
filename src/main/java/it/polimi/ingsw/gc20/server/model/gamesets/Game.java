package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.DieNotRolledException;
import it.polimi.ingsw.gc20.server.model.player.Player;


import java.util.*;

/**
 * Represents a game with players, a game board, a pile of components, and a pair of dice.
 * This class manages players, the game state, and various game-related operations,
 * such as rolling dice and moving players on the board.
 */
public class Game {

    private final List<Player> players;
    private Board board;
    private String gameID;
    private Pile pile;
    private final Die[] dice;

    /**
     * Default constructor for the Game class.
     * Initializes a new instance of the Game, setting up its basic components and initializing the necessary fields.
     * The game is initialized with an empty list of players, a null board, a null game ID, and a null pile.
     * It also initializes a pair of six-sided dice, ready to be rolled during the game.
     */
    public Game() {
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.board = null;
        this.gameID = null;
        this.pile = null;
        dice = new Die[2];
        this.dice[0]= new Die();
        this.dice[1] = new Die();
    }
    /**
     * Adds a new player to the game.
     *
     * @param p the Player object to be added to the game's list of players
     */
    public void addPlayer(Player p){
        players.add(p);
    }

    /**
     * Retrieves the list of players currently in the game.
     *
     * @return a List of Player objects representing the players in the game
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Removes a player from the game.
     *
     * @param p the Player object to be removed from the game's list of players
     */
    public void removePlayer(Player p){
        players.remove(p);
    }

    /**
     * Sets the board for the game by assigning it to the board field.
     *
     * @param board the Board object to be associated with the game
     */
    public void addBoard(Board board){
        this.board = board;
    }

    /**
     * Retrieves the current board associated with the game.
     *
     * @return the Board object representing the game board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Retrieves the unique identifier (ID) of the game.
     *
     * @return a String representing the unique game ID
     */
    public String getID() {
        return this.gameID;
    }

    /**
     * Sets the unique identifier (ID) for the game.
     *
     * @param id the unique ID to be assigned to the game
     */
    public void setID(String id) {
        this.gameID = id;
    }

    /**
     * Retrieves the pile associated with the game.
     *
     * @return the Pile object containing the game's components
     */
    public Pile getPile() {
        return this.pile;
    }

    /**
     * Sets the pile associated with the game. The pile contains the components
     * required during gameplay. This method assigns the provided Pile object
     * to the game's pile field, replacing any previously set pile.
     *
     * @param pile the Pile object to be associated with the game
     */
    public void setPile(Pile pile) {
        this.pile = pile;
    }

    /**
     * Checks if any player currently occupies a given position on the board.
     *
     * @param position the position on the board to check
     * @return true if the position is occupied by a player currently in the game, false otherwise
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

    /**
     * Moves a player a specific number of spaces on the board, either forward or backward.
     * The method ensures that the player stops at the next unoccupied space while moving.
     * If the desired number of spaces to move is negative, the player moves backward.
     * If the desired number of spaces to move is positive, the player moves forward.
     *
     * @param p the Player object to be moved
     * @param n the number of spaces to move; can be positive for forward movement or negative for backward movement
     */
    public void move (Player p, Integer n) {
        int spaceMoved = 0;
        //while the player has not moved n (can be negative) spaces
        while (spaceMoved < Math.abs(n)) {
            //moving forward
            if (n > 0) {
                //if the next space is not occupied
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

    /**
     * Sorts the list of players in the game based on their positions in descending order.
     * This method ensures thread safety by synchronizing on the players' list, preventing
     * concurrent modifications during the sorting process.
     */
    public void sortPlayerByPosition() {
        synchronized (players) {
            players.sort((p1, p2) -> p2.getPosition() - p1.getPosition());
        }
    }

    /**
     * Rolls two six-sided dice and returns the sum of their values.
     * This method uses the rollDie method of each die in the dice array,
     * generates random numbers for both dice, and computes their sum.
     *
     * @return the sum of the values rolled by the two dice
     */
    public int rollDice() {
        return dice[0].rollDie() + dice[1].rollDie();
    }

    /**
     * Retrieves the sum of the last rolled values of the two dice in the game.
     * This method invokes the getLastRolled method on both dice objects to compute the total.
     * If either die has not been rolled yet, a DieNotRolledException is thrown.
     *
     * @return the sum of the last rolled values of the two dice
     * @throws DieNotRolledException if either die has not been rolled yet
     */
    public int lastRolled() throws DieNotRolledException {
        return dice[0].getLastRolled() + dice[1].getLastRolled();
    }
}