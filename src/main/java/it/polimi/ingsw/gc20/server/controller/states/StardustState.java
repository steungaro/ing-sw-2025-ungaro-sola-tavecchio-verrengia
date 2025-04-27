package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") // dynamically created by Cards
public class StardustState extends PlayingState {

    /**
     * Default constructor
     */
    public StardustState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                automaticAction();
            } finally {
                scheduler.shutdown();
            }
        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return "StardustState";
    }

    @Override
    public void automaticAction() {
        getController().getInGameConnectedPlayers().stream()
                .map(p ->getController().getPlayerByID(p))
                .forEach(player -> getModel().movePlayer(player, -player.getShip().getAllExposed()));
        getController().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
