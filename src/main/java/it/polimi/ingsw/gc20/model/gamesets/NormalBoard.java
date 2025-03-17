package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.bank.Alien;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.AlienColor;
import it.polimi.ingsw.gc20.model.components.Cabin;

import java.util.*;

/**
 * @author GC20
 */
public class NormalBoard extends Board {
    private List<AdventureCard> firstVisible;
    private List<AdventureCard> secondVisible;
    private List<AdventureCard> thirdVisible;
    private List<AdventureCard> invisible;

    /**
     * Default constructor
     */
    public NormalBoard() {
        super();
        this.firstVisible = new ArrayList<AdventureCard>();
        this.secondVisible = new ArrayList<AdventureCard>();
        this.thirdVisible = new ArrayList <AdventureCard>();
        this.invisible = new ArrayList<AdventureCard>();
        this.setSpaces(24);
    }

    /** function that merges the decks and shuffles them
     */
    public void mergeDecks() {
        // deck merging
        List <AdventureCard> deck = this.getDeck();
        deck.addAll(this.firstVisible);
        deck.addAll(this.secondVisible);
        deck.addAll(this.thirdVisible);
        deck.addAll(this.invisible);

        //shuffling
        Collections.shuffle(deck);
        //first card is always a level 2 card
        for (int i=0; i<deck.size(); i++){
            if (deck.get(i).getLevel() == 2){
                Collections.swap(deck, 0, i);
                break;
            }
        }
    }

    /** function that peek the numDeck deck and returns it
     * @param numDeck the deck to peek
     * @return List<AdventureCard>
     * @throws IllegalArgumentException if numDeck is not 1, 2 or 3
     */
    public List<AdventureCard> peekDeck(Integer numDeck) throws IllegalArgumentException {
        switch (numDeck){
            case 1:
                return this.firstVisible;
            case 2:
                return this.secondVisible;
            case 3:
                return this.thirdVisible;
            default:
                throw new IllegalArgumentException("Invalid numDeck");
        }
    }

    /** function that creates the decks
     * @param n number of deck to create
     */
    public void createDeck(Integer n) {
        // TODO implement here
    }

}