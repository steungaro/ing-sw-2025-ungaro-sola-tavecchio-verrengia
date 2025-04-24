package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.exceptions.EnergyException;
import it.polimi.ingsw.gc20.exceptions.InvalidEngineException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class OpenSpaceState extends PlayingState {
    private final Map<Player, Integer> declaredEngines = new HashMap<>();

    /**
     * Default constructor
     */
    public OpenSpaceState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
    }

    @Override
    public String toString() {
        return "OpenSpaceState";
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidEngineException, EnergyException, EmptyDeckException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEngines.put(player, getModel().EnginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class)));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            declaredEngines.forEach((key, value) -> {
                getModel().movePlayer(key, value);
                if (value == 0) {
                    getController().defeated(key.getUsername());
                }
            });
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
    }
}
