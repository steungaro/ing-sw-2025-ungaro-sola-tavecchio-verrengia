package it.polimi.ingsw.gc20.server.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents a learner-level game board that extends the general functionality
 * of the Board class. A LearnerBoard is specifically tailored for beginner-level
 * gameplay by providing a simplified deck of adventure cards and a fixed number
 * of spaces.
 */
public class LearnerBoard extends Board {

    /**
     * Default constructor
     */
    public LearnerBoard() {
        super();
        this.setSpaces(18);
    }

    /**
     * Creates a shuffled deck of learner-level adventure cards by loading data from a JSON file.
     * The method reads a JSON file named "cards.json" containing adventure card definitions, filters
     * the cards to include only those with a level of 0, shuffles the filtered cards, and sets the
     * resulting shuffled list as the current deck of this LearnerBoard instance.
     * <p>
     * The method uses the Jackson library to deserialize the JSON file into a list of AdventureCard objects.
     * It ensures case-insensitive property matching and ignores unknown properties during deserialization.
     * If an error occurs while reading or parsing the file, it is logged as a severe issue.
     * <p>
     * Filters the cards based on their level to ensure only learner-level relevant cards are included.
     */
    @Override
    public void createDeck() {
        List<AdventureCard> learnerCards = new ArrayList<>();
        List<AdventureCard> cards = new ArrayList<>();
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        try {

            cards = Arrays.asList(mapper.readValue(getClass().getResourceAsStream("/cards.json"), AdventureCard[].class));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading cards.json", e);
        }
        for (AdventureCard card : cards) {
            if (card.getLevel() == 0) {
                learnerCards.add(card);
            }
        }
        Collections.shuffle(learnerCards);
        this.setDeck(learnerCards);
    }

    @Override
    public boolean isLearner() {
        return true;
    }
}