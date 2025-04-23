package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

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
    public SlaversState(GameModel model, GameController controller, AdventureCard card) {
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
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException, EmptyDeckException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        float firePower = getModel().FirePower(player, new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)), Translator.getComponentAt(player, batteries, Battery.class));
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
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws IllegalStateException, InvalidTurnException, EmptyCabinException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (defeated) {
            throw new IllegalStateException("You don't have to lose crew");
        }
        if (cabins.size() < lostMembers) {
            throw new IllegalStateException("You don't have enough crew to lose");
        }
        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        getModel().addCredits(player, reward);
        getModel().movePlayer(player, -lostDays);
        nextPlayer();
        if (getCurrentPlayer() == null) {
            getController().getActiveCard().playCard();
            getController().drawCard();
        }
    }

    @Override
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException, EmptyDeckException {
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
