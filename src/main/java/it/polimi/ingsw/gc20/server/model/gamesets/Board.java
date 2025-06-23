package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents an abstract game board that contains a deck of adventure cards, a stall box for players,
 * and game spaces. It provides the foundation for creating specific board types in subclasses
 * by defining common functionalities such as managing the deck, spaces, and stall box.
 */
public abstract class Board {
    protected List<AdventureCard> deck;
    protected int spaces;
    protected final List<Player> stallBox;
    protected final Logger logger = Logger.getLogger(Board.class.getName());

    /**
     * Default constructor for the Board class.
     * Initializes an empty deck of adventure cards, sets the number of spaces to zero,
     * and creates an empty stall box for players.
     */
    public Board() {
        this.deck = new ArrayList<>();
        this.spaces=0;
        this.stallBox = new ArrayList<>();
    }

    /**
     * Draws the top card from the deck of adventure cards. If the deck is empty,
     * an EmptyDeckException is thrown.
     *
     * @return the top AdventureCard from the deck
     * @throws EmptyDeckException if the deck is empty
     */
    public AdventureCard drawCard() throws EmptyDeckException {
        if (!this.deck.isEmpty()) {
            return this.deck.removeFirst();
        } else { // the deck is empty
            throw new EmptyDeckException("Deck is empty");
        }
    }

    /**
     * Sets the number of spaces on the board.
     *
     * @param spaces the number of spaces to set on the board
     */
    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    /**
     * Abstract method to create and initialize the deck of adventure cards on the board.
     * This method must be implemented by subclasses of the Board class to define their
     * specific logic for generating or preparing the deck of cards to be used in the game.
     */
    public abstract void createDeck();

    /**
     * Retrieves the number of spaces on the board.
     *
     * @return the number of spaces as an int
     */
    public int getSpaces() {
        return this.spaces;
    }

    /**
     * Sets the deck of adventure cards for the board.
     *
     * @param deck the list of AdventureCard objects to set as the deck
     */
    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
    }

    /**
     * Retrieves the current deck of adventure cards for the board.
     *
     * @return a List of AdventureCard objects representing the deck
     */
    public List<AdventureCard> getDeck() {
        return this.deck;
    }
    /**
     * Adds a player to the stall box on the board.
     *
     * @param p the player to be added to the stall box
     */
    public void addPlayer (Player p){
        stallBox.add(p);
    }
    /**
     * Removes a player from the stall box on the board.
     *
     * @param p the player to be removed from the stall box
     */
    public void removePlayer (Player p){
        stallBox.remove(p);
    }
    /**
     * Retrieves the current list of players in the stall box.
     *
     * @return a List of Player objects representing the players currently in the stall box
     */
    public List<Player> getStallBox() {
        return stallBox;
    }

    /**
     * Merges multiple decks of adventure cards into a single deck on the board.
     * This method consolidates cards from different sources, ensuring they are all
     * included in a unified deck ready for use during gameplay. The specific behavior
     * of this method, including how duplicate or shuffled cards are handled, is determined
     * by the implementation in the subclass. The default implementation does nothing.
     */
    public void mergeDecks(){
        //default implementation
    }

    /**
     * Determines if the board is learner level.
     *
     * @return true if the board is at learner level, false otherwise
     */
    public boolean isLearner(){
        //default implementation
        return false;
    }
}