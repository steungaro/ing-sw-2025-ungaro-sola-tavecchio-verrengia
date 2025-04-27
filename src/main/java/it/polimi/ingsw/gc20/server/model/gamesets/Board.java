package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author GC20
 */
public abstract class Board {
    protected List<AdventureCard> deck;
    protected Integer spaces;
    protected final List<Player> stallBox;
    protected final Logger logger = Logger.getLogger(Board.class.getName());
    /**
     * Default constructor
     */
    public Board() {
        this.deck = new ArrayList<>();
        this.spaces=0;
        this.stallBox = new ArrayList<>();
    }

    /** function that draw the first card from the deck
     * @exception  NoSuchElementException if the deck is empty
     * @return AdventureCard
     */
    public AdventureCard drawCard() throws EmptyDeckException {
        if (!this.deck.isEmpty()) {
            return this.deck.removeFirst();
        } else { // deck is empty
            throw new EmptyDeckException("Deck is empty");
        }
    }

    /** set function for spaces
     * @param spaces number of spaces
     */
    public void setSpaces(Integer spaces) {
        this.spaces = spaces;
    }

    /** function that creates the deck
     */
    public abstract void createDeck();

    /** get function for spaces
     * @return Integer
     */
    public Integer getSpaces() {
        return this.spaces;
    }

    /** set function for deck
     * @param deck list of AdventureCard
     */
    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
    }

    /** get function for deck
     * @return List<AdventureCard>
     */
    public List<AdventureCard> getDeck() {
        return this.deck;
    }
    /** add function for the stall box
     * @param p player to add
     */
    public void addPlayer (Player p){
        stallBox.add(p);
    }
    /** remove function for the stall box
     * @param p player to remove
     */
    public void removePlayer (Player p){
        stallBox.remove(p);
    }
    /** get function for the stall box
     * @return List<Player>
     */
    public List<Player> getStallBox() {
        return stallBox;
    }

    public void mergeDecks(){
        //default implementation
    }
}