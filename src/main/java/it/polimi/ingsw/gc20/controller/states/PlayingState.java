package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

public abstract class PlayingState extends State {
    GameController controller;
    GameModel model;
    String currentPlayer;

    @Override
    public void addModelController(GameModel model, GameController controller) {
        this.controller = controller;
        this.model = model;
        this.currentPlayer = controller.getCurrentPlayer();
    }
}
