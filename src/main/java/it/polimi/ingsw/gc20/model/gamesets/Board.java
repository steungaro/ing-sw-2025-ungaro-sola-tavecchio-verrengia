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

    //metodo che persca la prima carta del deck, eccezione se il deck è vuoto
    public AdventureCard drawCard() throws NoSuchElementException{
        if (this.deck.size() > 0) {
            return this.deck.remove(0);
        } else { //deck vuoto
            throw new NoSuchElementException("Deck is empty");
        }
    }

    //metodo che setta il numero di spazi della board
    public void setSpaces(Integer spaces) {
        this.spaces = spaces;
    }

    //funzione che crea un deck
    public void createDeck() {
        // TODO implement here
    }

    //funzione che restituisce gli spazi della board
    public Integer getSpaces() {
        return this.spaces;
    }

    //funzione che setta il deck
    public void setDeck(List<AdventureCard> deck) {
        this.deck = deck;
    }

    //funzione che restituisce il deck
    public List<AdventureCard> getDeck() {
        return this.deck;
    }

}