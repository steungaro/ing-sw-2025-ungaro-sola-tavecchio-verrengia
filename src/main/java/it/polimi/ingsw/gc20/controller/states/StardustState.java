package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

public class StardustState extends PlayingState {
    /**
     * Default constructor
     */
    public StardustState(GameController controller, GameModel model) {
        super(model, controller);
    }

    @Override
    public String toString() {
        return "StardustState";
    }

    @Override
    public void automaticAction() {
//TODO        getController().getInGameConnectedPlayers().stream()
//TODO                .map(p ->getController().getPlayerByID(p))
//TODO                .forEach(player -> getModel().move(-player.getShip().getAllExposed()));
        getController().getActiveCard().playCard();
        getController().drawCard();
    }
}
