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
 * Represents a normal board for the game, extending the base {@link Board} class.
 * This class manages the decks of adventure cards, the game spaces, and the hourglass timer.
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

    /**
     * Merges multiple decks of AdventureCard objects into a single deck and performs shuffling.
     * This method combines the four card collections: firstVisible, secondVisible, thirdVisible,
     * and invisible into one unified deck. It then shuffles the resulting deck to randomize the card
     * order. Additionally, it ensures that the first card in the deck is always a level 2 card by
     * swapping the first level 2 card it encounters with the card at the top of the deck.
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

    /**
     * Returns a visible deck of adventure cards based on the deck number provided.
     * The method allows access to one of the first three visible decks.
     *
     * @param numDeck the number of the deck to peek at; it should be 1, 2, or 3
     * @return a list of AdventureCard objects representing the specified visible deck
     * @throws InvalidIndexException if the numDeck parameter is not 1, 2, or 3
     */
    public List<AdventureCard> peekDeck(int numDeck) throws InvalidIndexException {
        return switch (numDeck) {
            case 1 -> this.firstVisible;
            case 2 -> this.secondVisible;
            case 3 -> this.thirdVisible;
            default -> throw new InvalidIndexException("Invalid numDeck");
        };
    }

    /**
     * Creates and initializes the decks required for the game.
     * <p>
     * This method reads card data from a JSON file and organizes the cards into
     * two distinct groups: level 1 cards (including level 0) and level 2 cards.
     * Each group of cards is shuffled randomly. The shuffled cards are then
     * assigned to four decks: firstVisible, secondVisible, thirdVisible, and
     * invisible, ensuring each deck contains a mix of level 1 and level 2 cards.
     * <p>
     * The card distribution follows these rules:
     * - The decks each receive two level 2 cards and one level 1 card.
     * <p>
     * This method handles exceptions that may occur during file reading and logs
     * errors appropriately.
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

    /**
     * Returns a deck of adventure cards based on the specified deck number.
     * This method provides access to one of four existing decks: three visible decks (1, 2, or 3)
     * or the fourth invisible deck. An exception is thrown if an invalid deck number is provided.
     *
     * @param numDeck the number of the deck to retrieve; it should be 1, 2, 3, or 4
     * @return a list of AdventureCard objects representing the specified deck
     * @throws InvalidIndexException if the numDeck parameter is not 1, 2, 3, or 4
     */
    public List<AdventureCard> getDeck(int numDeck) throws InvalidIndexException {
        return switch (numDeck) {
            case 1 -> this.firstVisible;
            case 2 -> this.secondVisible;
            case 3 -> this.thirdVisible;
            case 4 -> this.invisible;
            default -> throw new InvalidIndexException("Invalid numDeck");
        };
    }

    /**
     * Turns the hourglass.
     * <p>
     * The hourglass can only be turned under the following conditions:
     * - The remaining time of the current countdown is 0.
     * - The hourglass has been turned fewer than 2 times.
     *
     * @throws HourglassException if the remaining time is not zero, or the hourglass
     *         has already been turned two times
     */
    public void turnHourglass() throws HourglassException {
        if (this.hourglass.getRemainingTime() == 0 && this.hourglass.getTurned() < 2) {
            this.hourglass.turn();
        } else {
            throw new HourglassException("Cannot turn the hourglass when the remaining time is not 0 or when it has already been turned 2 times.");
        }
    }

    /**
     * Returns the remaining time of the hourglass in seconds.
     * The value corresponds to the time left until the hourglass runs out.
     *
     * @return the remaining time in seconds
     */
    public int getRemainingTime() {
        return this.hourglass.getRemainingTime();
    }

    /**
     * Calculates the total remaining time for the game.
     * The value is determined based on the hourglass's total period,
     * the elapsed time, and the number of times the hourglass can be turned.
     *
     * @return the total remaining time in seconds
     */
    public int getTotalRemainingTime() {
        return 3 * this.hourglass.getPeriod() - this.hourglass.getTotalElapsed();
    }


    /**
     * Initializes the countdown for the hourglass.
     * This method delegates the initialization process to the hourglass instance,
     * setting up the internal countdown mechanism.
     */
    public void initCountdown (){
        this.hourglass.initCountdown();
    }

    /**
     * Returns the number of times the hourglass has been turned.
     *
     * @return the count of turns performed on the hourglass
     */
    public int getTurnedHourglass() {
        return this.hourglass.getTurned();
    }

    /**
     * Retrieves the timestamp associated with the hourglass object's most recent countdown start.
     *
     * @return the timestamp of the most recent countdown start, represented as a long value
     */
    public long getHourglassTimestamp() {
        return this.hourglass.getTimestamp();
    }
}