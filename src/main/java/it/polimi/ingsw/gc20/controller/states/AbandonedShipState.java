package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.List;

/**
 * This class represents the state of the game when an AbandonedShip card has been drawn.
 * The player can:
 * - lose crew (accepting card) and gain the rewards (credits and lost days)
 * - end the move (discard card)
 *    + if all players have ended their moves without accepting the card, a new card is drawn
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class AbandonedShipState extends PlayingState {
    private final int lostCrew;
    private final int credits;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShipState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostCrew = card.getCrew();
        this.credits = card.getCredits();
        this.lostDays = card.getLostDays();
    }

    @Override
    public String toString() {
        return "AbandonedShipState{ " +
                "lostCrew=" + lostCrew +
                ", credits=" + credits +
                ", lostDays=" + lostDays +
                '}';
    }

    /**
     * This method is used to accept the card and lose crew
     * @param player the player who is losing crew
     * @param cabins the cabins to get the crew from
     * @throws IllegalStateException if the player doesn't have enough crew to lose
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws IllegalStateException, InvalidTurnException, EmptyCabinException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (cabins.size() < lostCrew) {
            throw new IllegalStateException("You don't have enough crew to lose");
        }
        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        getModel().addCredits(player, credits);
        getModel().movePlayer(player, -lostDays);
        getController().getActiveCard().playCard();
        getController().drawCard();
    }

    /**
     * This method is used to end the move and discard the card
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException, EmptyDeckException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        nextPlayer();
        if (getCurrentPlayer() == null) {
            getController().drawCard();
        }
    }
}
