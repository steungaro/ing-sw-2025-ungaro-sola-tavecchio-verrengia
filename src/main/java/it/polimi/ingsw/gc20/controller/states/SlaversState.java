package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
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
    public SlaversState(GameController gc, GameModel gm, int firePower, int lostMembers, int reward, int lostDays) {
        super(gm, gc);
        this.firePower = firePower;
        this.lostMembers = lostMembers;
        this.reward = reward;
        this.lostDays = lostDays;
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
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
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
        //TODO getModel().loseCrew(player, cabins);
        //TODO getModel().addCredits(player, credits);
        //TODO getModel().move(player, -lostDays);
        nextPlayer();
        if (getCurrentPlayer() == null) {
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
        //TODO getModel().move(player, -lostDays);
        //TODO getModel().addCredits(player, reward);
        getController().drawCard();
    }
}
