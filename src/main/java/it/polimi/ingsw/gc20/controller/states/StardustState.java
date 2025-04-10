package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

@SuppressWarnings("unused") // dynamically created by Cards
public class StardustState extends PlayingState {
    /**
     * Default constructor
     */
    public StardustState(GameController controller, GameModel model, AdventureCard card) {
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
