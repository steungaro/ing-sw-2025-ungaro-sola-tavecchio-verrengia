package it.polimi.ingsw.gc20.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * @author GC20
 */
public class LearnerBoard extends Board {

    /**
     * Default constructor
     */
    public LearnerBoard() {
        super();
        this.setSpaces(18);
    }

    public void createDeck() {
        List<AdventureCard> learnerCards = new ArrayList<>();
        List<AdventureCard> cards = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
}