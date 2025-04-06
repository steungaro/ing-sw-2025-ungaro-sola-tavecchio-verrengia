package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

/**
 * This class represents the state of the game when an AbandonedShip card has been drawn.
 * The player can:
 * - lose crew (accepting card) and gain the rewards (credits and lost days)
 * - end the move (discard card)
 *    + if all players have ended their moves without accepting the card, a new card is drawn
 */
public class AbandonedShipState extends PlayingState {
    private final int lostCrew;
    private final int credits;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShipState(GameController controller, GameModel model, int lostCrew, int credits, int lostDays) {
        super(model, controller);
        this.lostCrew = lostCrew;
        this.credits = credits;
        this.lostDays = lostDays;
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
    public void loseCrew(Player player, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (cabins.size() < lostCrew) {
            throw new IllegalStateException("You don't have enough crew to lose");
        }

        //TODO getModel().loseCrew(player, cabins);
        //TODO getModel().addCredits(player, credits);
        //TODO getModel().move(player, -lostDays);
        getController().getActiveCard().playCard();
        getController().drawCard();
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        nextPlayer();
        if (getCurrentPlayer() == null) {
            getController().drawCard();
        }
    }
}
