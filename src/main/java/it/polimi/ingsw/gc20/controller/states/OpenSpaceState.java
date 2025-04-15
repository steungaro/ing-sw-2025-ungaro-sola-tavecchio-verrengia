package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Engine;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

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
    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEngines.put(player, getModel().EnginePower(player, engines.size(), batteries));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            declaredEngines.forEach((key, value) -> {
                getModel().movePlayer(key, value);
                if (value == 0) {
                    getController().defeated(key.getUsername());
                }
            });
            getModel().getActiveCard().playCard();
            getController().drawCard();
        }
    }
}
