package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") // dynamically created by Cards
public class StardustState extends PlayingState {

    /**
     * Default constructor
     */
    public StardustState(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(this::automaticAction, 5, TimeUnit.SECONDS);
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
        getController().drawCard();
    }
}
