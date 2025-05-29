package it.polimi.ingsw.gc20.server.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.polimi.ingsw.gc20.server.exceptions.HourglassException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidIndexException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.logging.Level;

/**
 * @author GC20
 */
public class NormalBoard extends Board {
    private final List<AdventureCard> firstVisible;
    private final List<AdventureCard> secondVisible;
    private final List<AdventureCard> thirdVisible;
    private final List<AdventureCard> invisible;
    protected final Hourglass hourglass;

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

    /** Function that merges the decks and shuffles them
     */
    @Override
    public void mergeDecks() {
        // deck merging
        List <AdventureCard> deck = new ArrayList<>();
        deck.addAll(this.firstVisible);
        deck.addAll(this.secondVisible);
        deck.addAll(this.thirdVisible);
        deck.addAll(this.invisible);

        //shuffling
        Collections.shuffle(deck);
        //the first card is always a level 2 card
        for (int i=0; i<deck.size(); i++){
            if (deck.get(i).getLevel() == 2){
                Collections.swap(deck, 0, i);
                break;
            }
        }
        this.setDeck(deck);
    }

    /** Function that peeks the numDeck deck and returns it
     * @param numDeck the deck to peek
     * @return List<AdventureCard> the deck that is peeked
     * @throws InvalidIndexException if numDeck is not 1, 2 or 3
     */
    public List<AdventureCard> peekDeck(Integer numDeck) throws InvalidIndexException {
        return switch (numDeck) {
            case 1 -> this.firstVisible;
            case 2 -> this.secondVisible;
            case 3 -> this.thirdVisible;
            default -> throw new InvalidIndexException("Invalid numDeck");
        };
    }

    /** Function that creates the four decks of cards.
     * The first three decks are visible, while the fourth is invisible
     * Each deck contains 3 level 2 cards and 1 level 1 card.
     */
    public void createDeck() {
        List<AdventureCard> level1Cards = new ArrayList<>();
        List<AdventureCard> level2Cards = new ArrayList<>();
        List<AdventureCard> cards = new ArrayList<>();
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        try {
            cards = Arrays.asList(mapper.readValue(getClass().getResourceAsStream("/cards.json"), AdventureCard[].class));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while reading the cards.json file", e);
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
        for (int i = 0; i < 2; i++) {
            this.firstVisible.add(level2Cards.removeFirst());
        }
        this.firstVisible.add(level1Cards.removeFirst());

        //Second deck
        for (int i = 0; i < 2; i++) {
            this.secondVisible.add(level2Cards.removeFirst());
        }
        this.secondVisible.add(level1Cards.removeFirst());

        //Third deck
        for (int i = 0; i < 2; i++) {
            this.thirdVisible.add(level2Cards.removeFirst());
        }
        this.thirdVisible.add(level1Cards.removeFirst());

        //Invisible deck
        for (int i = 0; i < 2; i++) {
            this.invisible.add(level2Cards.removeFirst());
        }
        this.invisible.add(level1Cards.removeFirst());

    }

    /** Getter function for one of the four decks
     * @param numDeck the deck to get
     * @return List<AdventureCard> the deck that is returned
     * @throws InvalidIndexException if numDeck is not 1, 2, 3 or 4
     */
    public List<AdventureCard> getDeck(Integer numDeck) throws InvalidIndexException {
        return switch (numDeck) {
            case 1 -> this.firstVisible;
            case 2 -> this.secondVisible;
            case 3 -> this.thirdVisible;
            case 4 -> this.invisible;
            default -> throw new InvalidIndexException("Invalid numDeck");
        };
    }

    /** Function that turns the hourglass, to be used every time a player turns the hourglass except for the first time (which is done at the beginning of the game)
     * @throws HourglassException if the hourglass is already turned 3 times, or if the remaining time is not 0
     */
    public void turnHourglass() throws HourglassException {
        if (this.hourglass.getRemainingTime() == 0 && this.hourglass.getTurned() < 2) {
            this.hourglass.turn();
        } else {
            throw new HourglassException("Cannot turn the hourglass when the remaining time is not 0 or when it has already been turned 2 times.");
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
     * @deprecated This function is deprecated and should not be used. Use {@link #initCountdown()} instead.
     */
    @Deprecated
    public void stopHourglass() {
        this.hourglass.stopCountdown();
    }

    /**
     * Function that starts the hourglass. This function is meant to be called only once per match, at the beginning of the game.
     */
    public void initCountdown (){
        this.hourglass.initCountdown();
    }

    /**
     * Function that returns the number of times the hourglass has been turned
     * @return int is the number of times the hourglass has been turned
     */
    public int getTurnedHourglass() {
        return this.hourglass.getTurned();
    }

    /**
     * Function that returns the timestamp of the hourglass
     * @return long is the timestamp of the hourglass
     */
    public long getHourglassTimestamp() {
        return this.hourglass.getTimestamp();
    }
}