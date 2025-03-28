package it.polimi.ingsw.gc20.model.gamesets;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        try {

            cards = Arrays.asList(mapper.readValue(getClass().getResourceAsStream("/cards.json"), AdventureCard[].class));
        } catch (Exception e) {
            e.printStackTrace();
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