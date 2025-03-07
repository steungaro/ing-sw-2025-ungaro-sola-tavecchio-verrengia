package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.cards.AdventureCard;

import java.util.*;

/**
 * @author GC20
 */
public abstract class Board {
    private List<AdventureCard> deck;
    private Integer spaces;
    /**
     * Default constructor
     */
    public Board() {
        this.deck = new ArrayList<>();
        this.spaces=0;
    }

    /** function that draw the first card from the deck
     * @exception  NoSuchElementException if the deck is empty
     * @return AdventureCard
     */
    public AdventureCard drawCard() throws NoSuchElementException{
        if (this.deck.isEmpty()) {
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
    public void createDeck() {
        // TODO implement here
    }

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

}