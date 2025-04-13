package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class OpenSpaceState extends EnginesState {

    /**
     * Default constructor
     */
    public OpenSpaceState(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
    }

    @Override
    public String toString() {
        return "OpenSpaceState";
    }

    @Override
    public void finalEngineAction(Map<Player, Integer> declaredEngines) {
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
