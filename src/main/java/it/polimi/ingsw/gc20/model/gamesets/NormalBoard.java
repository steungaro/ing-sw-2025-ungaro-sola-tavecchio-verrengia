package it.polimi.ingsw.gc20.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc20.model.components.Component;

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
        this.hourglass = new Hourglass(5);
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
     *
     */
    public void createDeck() {
        List<AdventureCard> level1Cards = new ArrayList<>();
        List<AdventureCard> level2Cards = new ArrayList<>();
        List<AdventureCard> cards = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {

            cards = Arrays.asList(mapper.readValue(getClass().getResourceAsStream("/cards.json"), AdventureCard[].class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AdventureCard card : cards) {
            if (card.getLevel() == 1 || card.getLevel() == 0) {
                level1Cards.add(card);
            } else {
                level2Cards.add(card);
            }
        }
        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);

        //First deck
        for (int i = 0; i < 3; i++) {
            this.firstVisible.add(level1Cards.removeFirst());
        }
        this.firstVisible.add(level2Cards.removeFirst());

        //Second deck
        for (int i = 0; i < 3; i++) {
            this.secondVisible.add(level1Cards.removeFirst());
        }
        this.secondVisible.add(level2Cards.removeFirst());

        //Third deck
        for (int i = 0; i < 3; i++) {
            this.thirdVisible.add(level1Cards.removeFirst());
        }
        this.thirdVisible.add(level2Cards.removeFirst());

        //Invisible deck
        for (int i = 0; i < 3; i++) {
            this.invisible.add(level1Cards.removeFirst());
        }
        this.invisible.add(level2Cards.removeFirst());

    }

    /** Function that turns the hourglass, to be used every time a player turns the hourglass except for the first time (which is done at the beginning of the game)
     * @throws IllegalArgumentException if the hourglass is already turned 3 times or if the remaining time is not 0
     */
    public void turnHourglass() {
        if (this.hourglass.getRemainingTime() == 0 && this.hourglass.getTurned() < 2) {
            this.hourglass.turn();
        } else {
            throw new IllegalArgumentException("Cannot turn the hourglass when the remaining time is not 0 or when it has already been turned 2 times.");
        }
    }

    /** Function that returns the remaining time
     * @return The number of seconds left of the current turn
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

    /**
     * Function that stops the hourglass
     */
    public void stopHourglass() {
        this.hourglass.stopCountdown();
    }

    /**
     * Function that starts the hourglass. This function is meant to be called only once per match, at the beginning of the game.
     */
    public void initCountdown (){
        this.hourglass.initCountdown();
    }

    public int getTurnedHourglass() {
        return this.hourglass.getTurned();
    }
}