package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") // dynamically created by Cards
public class EpidemicState extends PlayingState {
    /**
     * Default constructor
     */
    public EpidemicState(GameModel model, GameController controller, AdventureCard card) {
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
        return "EpidemicState";
    }


    @Override
    public void automaticAction() {
        getModel().getInGamePlayers().stream()
                .filter(p -> getController().isPlayerDisconnected(p.getUsername()))
                .forEach(p -> p.getShip().epidemic());
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
