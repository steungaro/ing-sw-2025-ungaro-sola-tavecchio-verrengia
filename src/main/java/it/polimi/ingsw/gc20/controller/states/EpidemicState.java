package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

@SuppressWarnings("unused") // dynamically created by Cards
public class EpidemicState extends PlayingState {
    /**
     * Default constructor
     */
    public EpidemicState(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.automaticAction();
    }

    @Override
    public String toString() {
        return "EpidemicState";
    }

    public void epidemic(){
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
