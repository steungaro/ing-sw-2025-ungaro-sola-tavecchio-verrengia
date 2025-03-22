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
    private final Hourglass hourglass;

    /**
     * Default constructor
     */
    public NormalBoard() {
        super();
        this.firstVisible = new ArrayList<>();
        this.secondVisible = new ArrayList<>();
        this.thirdVisible = new ArrayList<>();
        this.invisible = new ArrayList<>();
        this.setSpaces(24);
        this.hourglass = new Hourglass(90);
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
        return switch (numDeck) {
            case 1 -> this.firstVisible;
            case 2 -> this.secondVisible;
            case 3 -> this.thirdVisible;
            default -> throw new IllegalArgumentException("Invalid numDeck");
        };
    }

    /** function that creates the decks
     * @param n number of deck to create
     */
    public void createDeck(Integer n) {
        // TODO implement here
    }

    /** Function that turns the hourglass
     * @throws IllegalArgumentException if the hourglass is already turned 3 times or if the remaining time is not 0
     */
    public void turnHourglass() {
        if (this.hourglass.getRemainingTime() == 0 && this.hourglass.getTurned() < 3) {
            this.hourglass.turn();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Function that returns the remaining time
     * @return int is the number of seconds left of the current turn
     */
    public int getRemainingTime() {
        return this.hourglass.getRemainingTime();
    }

    /** Function that returns the total remaining time
     * @return int is the number of seconds left
     */
    public int getTotalRemainingTime() {
        return 3 * this.hourglass.getPeriod() - this.hourglass.getTotalElapsed();
    }

    public void stopHourglass() {
        this.hourglass.stopCountdown();
    }

    public void initCountdown (){
        this.hourglass.initCountdown();
    }
}