package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

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
        scheduler.schedule(this::automaticAction, 5, TimeUnit.SECONDS);
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
        getController().drawCard();
    }
}
