package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.cards.AdventureCard;

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

    //funzione che unisce i 4 deck e mischia il mazzo spostando una carta di livello 2 in cima
    public void mergeDecks() {
        List <AdventureCard> deck = this.getDeck();
        deck.addAll(this.firstVisible);
        deck.addAll(this.secondVisible);
        deck.addAll(this.thirdVisible);
        deck.addAll(this.invisible);

        Collections.shuffle(deck);
        for (int i=0; i<deck.size(); i++){
            if (deck.get(i).getLevel() == 2){
                Collections.swap(deck, 0, i);
                break;
            }
        }
    }

    //prende in input un intero e guarda il deck corrispondente, lancia eccezione se l'intero non è valido
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

    //funzione che crea un deck di carte avventura
    public void createDeck(Integer n) {
        // TODO implement here
        return null;
    }

}