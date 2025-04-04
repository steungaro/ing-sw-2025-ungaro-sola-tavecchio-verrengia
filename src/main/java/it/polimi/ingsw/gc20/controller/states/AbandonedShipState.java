package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

public class AbandonedShipState extends PlayingState{
    private final int lostCrew;
    private final int credits;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedShipState(int lostCrew, int credits, int lostDays) {
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
        model.
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {

    }
}
