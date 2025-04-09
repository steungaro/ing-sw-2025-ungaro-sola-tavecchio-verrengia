package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashSet;
import java.util.List;

public class SlaversState extends PlayingState {
    private final int firePower;
    private final int lostMembers;
    private final int reward;
    private final int lostDays;
    private boolean defeated;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public SlaversState(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
        this.firePower = card.getFirePower();
        this.lostMembers = card.getCrew();
        this.reward = card.getCredits();
        this.lostDays = card.getLostDays();
        this.defeated = false;
    }

    @Override
    public String toString() {
        return "SlaversState{" +
                "firePower=" + firePower +
                ", lostMembers=" + lostMembers +
                ", reward=" + reward +
                ", lostDays=" + lostDays +
                '}';
    }

    @Override
    public int shootEnemy(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        float firePower = getModel().FirePower(player, new HashSet<>(cannons), batteries);
        if (firePower > this.firePower) {
            getController().getActiveCard().playCard();
            defeated = true;
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().getActiveCard().playCard();
                getController().drawCard();
            }
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void loseCrew(Player player, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (defeated) {
            throw new IllegalStateException("You don't have to lose crew");
        }
        if (cabins.size() < lostMembers) {
            throw new IllegalStateException("You don't have enough crew to lose");
        }
        getModel().loseCrew(player, cabins);
        getModel().addCredits(player, reward);
        getModel().movePlayer(player, -lostDays);
        nextPlayer();
        if (getCurrentPlayer() == null) {
            getController().getActiveCard().playCard();
            getController().drawCard();
        }
    }

    @Override
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!defeated) {
            throw new IllegalStateException("Card not defeated");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, reward);
        getController().getActiveCard().playCard();
        getController().drawCard();
    }
}
