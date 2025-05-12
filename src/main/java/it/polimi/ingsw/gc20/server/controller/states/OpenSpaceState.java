package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.EnergyException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidEngineException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
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
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidEngineException, EnergyException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }

        List<Battery> energy = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class) != null)
            energy.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        declaredEngines.put(player, getModel().EnginePower(player, engines.size(), energy));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            endTurn();
        }
    }

    @Override
    public void endTurn(){
        declaredEngines.forEach((key, value) -> {
            getModel().movePlayer(key, value);
            if (value == 0) {
                getController().defeated(key.getUsername());
            }
        });
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player) {
            nextPlayer();
            if(getCurrentPlayer() == null) {
                endTurn();
            }
    }
}
