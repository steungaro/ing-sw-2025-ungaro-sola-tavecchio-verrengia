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
        phase = StatePhase.AUTOMATIC_ACTION;
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


    /**
     * This method is called when the player has to perform an automatic action.
     * In this case, the action is to infect all players in the game and if necessary, to remove crew members
     */
    @Override
    public void automaticAction() {
        //apply the epidemic effect to all players in the game
        getModel().getInGamePlayers().stream()
                .filter(p -> getController().getInGameConnectedPlayers().contains(p.getUsername()))
                .forEach(p -> p.getShip().epidemic());
        //effect ended, draw a new card
        phase = StatePhase.STANDBY_PHASE;
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
