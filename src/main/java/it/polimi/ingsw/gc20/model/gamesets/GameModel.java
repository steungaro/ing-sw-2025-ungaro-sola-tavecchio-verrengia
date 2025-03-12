package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.ship.*;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

public class GameModel {
    private Game game;
    private AdventureCard activeCard;

    /**
     * Default Constructor
     */
    public void GameModel (){
        this.game = null;
        this.activeCard = null;
    }

    /** set function for the game
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /** get function for the game
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    /** set function for the active card
     * @param activeCard
     */
    public void setActiveCard(AdventureCard activeCard) {
        this.activeCard = activeCard;
    }

    /** get function for the active card
     * @return activeCard
     */
    public AdventureCard getActiveCard() {
        return this.activeCard;
    }

    /** function that starts the game
     * @param numberOfPlayers number of players
     * @param livello level of the game
     * @param username username of the player
     */
    public void startGame(int numberOfPlayers, int livello, String username) {
        Game game = new Game();
        Pile pile = new Pile();
        Board board;
        //creating the board based on the level
        if (livello==2) {
            board = new NormalBoard();
            //TODO create and setting the four deck
        } else {
            board = new LearnerBoard();
            //TODO create and setting the deck
        }

        //creating the players and setting the first player
        for (int i=0; i<numberOfPlayers; i++) {
            Player player = new Player();
            if (i==0) {
                //setting username and status of the first player
                player.setUsername(username);
                player.setGameStatus(true);
            }
            player.setColor(PlayerColor.values()[i]);
            //setting the ship based on the level
            if (livello==2){
                player.setShip (new NormalShip());
            } else {
                player.setShip (new LearnerShip());
            }
            board.addPlayer(player);
            game.addPlayer(player);
        }
        game.addBoard(board);
        pile.addUnviewed(/*all components*/);
        game.setPile(pile);

        this.setGame(game);
    }

    /** function that allows the player to join the game
     * @param username username of the player
     */
    public void joinGame (String username){
        //searching for the first player not in game
        for (Player player: game.getPlayers()) {
            if (!player.isInGame()) {
                player.setUsername(username);
                player.setGameStatus(true);
                return;
            }
        }
    }

    /** function when a component is taken from the unviewed list
     * @param c component to remove
     * @param p player that takes the component
     * @throws NoSuchElementException if the component is not present in the unviewed list
     */
    public void componentFromUnviewed (Component c, Player p )  throws NoSuchElementException {
        game.getPile().removeUnviewed(c);
    }

    /** function when a component is taken from the viewed list
     * @param c component to remove
     * @param p player that takes the component
     * @throws NoSuchElementException if the component is not present in the viewed list
     */
    public void componentFromViewed (Component c, Player p )  throws NoSuchElementException {
        game.getPile().removeViewed(c);
    }

    /** function when a component is taken from the booked list
     * @param c component to remove
     * @param p player that takes the component
     * @throws IllegalArgumentException if the component is not present in the booked list
     */
    public void componentFromBooked (Component c, Player p )  throws IllegalArgumentException {
        Ship s = p.getShip();
        ((NormalShip) s).removeBooked(c);
    }
    /** method that allows the player to view the deck only called in level 2 games
     * @param numDeck the deck to view
     * @throws IllegalArgumentException if numDeck is not 1, 2 or 3
     */
    public void viewDeck (int numDeck) throws IllegalArgumentException {
        Board b = game.getBoard();
        ((NormalBoard) b).peekDeck(numDeck);
    }

    /** function that allows the player to rotate the component taken clockwise
     * @param c component to rotate
     * @param p player that rotates the component
     */
    public void RotateClockwise (Component c, Player p){
        c.rotateClockwise();
    }

    /** function that allows the player to rotate the component taken counterclockwise
     * @param c component to rotate
     * @param p player that rotates the component
     */
    public void RotateCounterclockwise(Component c, Player p){
        c.rotateCounterclockwise();
    }

    /**function to add a component to the booked list only called in level 2 games
     * @param c component to add
     * @param p player that adds the component
     */
    public void componentToBooked (Component c, Player p){
        Ship s = p.getShip();
        ((NormalShip) s).addBooked(c);
    }

    /** function to add a component to the viewed list
     * @param c component to add
     * @param p player that adds the component
     */
    public void componentToViewed (Component c, Player p){
        game.getPile().addViewed(c);
    }

    /** function to add a component to the ship
     * @param c component to add
     * @param p player that adds the component
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     */
    public void addToShip (Component c, Player p, int x, int y)
    {
        Ship s = p.getShip();
        s.addComponent(c, x, y);
    }

    /** function when a player stop assembling
     * @param p player that stops assembling
     * @param position position chosen by the player (1-4)
     * @throws IllegalArgumentException if the position is already occupied
     */
    public void stopAssembling (Player p, int position) throws IllegalArgumentException{
        game.getBoard().removePlayer(p);
        if (game.isOccupied(position)){
            throw new IllegalArgumentException("Position already occupied");
        } else {
            for (Player player : game.getPlayers()) {
                if (player == p) {
                    if (position == 1){
                        player.setLeader();
                    }
                    player.setPosition(position);
                }
            }
        }

    }

    /** function that validates the ship
     * @param p player that validates the ship
     * @return boolean true if the ship is valid, false otherwise
     */
    public boolean shipValidating (Player p){
        Ship s = p.getShip();
        return s.isValid();
    }

    /** function that sets the alien in the ship
     * @param a color of the alien
     * @param c cabin for the alien
     * @param p player that adds the alien
     * @throws IllegalArgumentException if the component is not a cabin
     * @throws InvalidParameterException if the cabin cannot host this type of alien
     */
    public void setAlien (AlienColor a, Component c, Player p) throws IllegalArgumentException, InvalidParameterException {
        Ship s = p.getShip();
        Alien alien = new Alien();
        alien.setColor(a);
        alien.setCabin(c);
        ((NormalShip) s).addAlien(alien, c);
    }

    /** function that sets the astronaut in the ship
     * @param p player that adds the astronaut
     * @throws IllegalArgumentException if the component is not a cabin
     * @throws InvalidParameterException if the cabin cannot host this type of astronaut
     */
    public void addPieces (Player p){
        p.getShip().addAllAstronauts();
    }

    /** function that creates the deck for the game
     * it is called only in second level games
     */
    public void createDeck () {
        Board b = game.getBoard();
        ((NormalBoard) b).mergeDecks();
    }


}
