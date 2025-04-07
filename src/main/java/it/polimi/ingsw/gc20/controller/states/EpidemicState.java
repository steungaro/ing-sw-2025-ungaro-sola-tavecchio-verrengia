package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

public class EpidemicState extends PlayingState {
    /**
     * Default constructor
     */
    public EpidemicState(GameController gameController, GameModel gameModel) {
        super(gameModel, gameController);
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
                .forEach(p -> {p.getShip().epidemic();});
    }
}
