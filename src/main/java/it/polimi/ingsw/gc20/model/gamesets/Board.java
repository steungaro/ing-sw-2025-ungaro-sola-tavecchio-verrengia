package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.player.*;

import java.util.*;

/**
 * @author GC20
 */
public abstract class Board {
    private List<AdventureCard> deck;
    private Integer spaces;
    private List<Player> stallBox;
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
    public AdventureCard drawCard() throws NoSuchElementException{
        if (!this.deck.isEmpty()) {
            return this.deck.removeFirst();
        } else { // deck is empty
            throw new NoSuchElementException("Deck is empty");
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
}