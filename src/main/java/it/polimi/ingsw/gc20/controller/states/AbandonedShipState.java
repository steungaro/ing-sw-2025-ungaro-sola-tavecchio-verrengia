package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

/**
 * This class represents the state of the game when an AbandonedShip card has been drawn.
 * The player can:
 * - lose crew (accepting card) and gain the rewards (credits and lost days)
 * - end the move (discard card)
 *    + if all players have ended their moves without accepting the card, a new card is drawn
 */
public class AbandonedShipState extends PlayingState{
    private final int lostCrew;
    private final int credits;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShipState(int lostCrew, int credits, int lostDays) {
        super();
        this.lostCrew = lostCrew;
        this.credits = credits;
        this.lostDays = lostDays;
    }

    @Override
    public String toString() {
        return "AbandonedShipState";
    }

    @Override
    public void loseCrew(Player player, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(currentPlayer)) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (cabins.size() < lostCrew) {
            throw new IllegalStateException("You don't have enough crew to lose");
        }
        // TODO: model new methods
        // model.loseCrew(player, cabins);
        // model.addCredits(player, credits);
        // model.move(player, -lostDays);
        controller.getActiveCard().playCard();
        controller.drawCard();
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        if (controller.nextTurn()) controller.drawCard();
    }
}
