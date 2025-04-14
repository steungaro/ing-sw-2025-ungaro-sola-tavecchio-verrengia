package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashMap;

public class ValidateShipState extends PlayingState{

    protected boolean isSplit = false;

    public ValidateShipState(GameModel model, GameController controller) {
        super(model, controller);
    }

    public void chooseBranch(Player p, Integer row, Integer col){
        if(!isSplit || !getCurrentPlayer().equals(p.getUsername())){
            throw new IllegalStateException("Cannot choose branch now");
        }
        p.getShip().findValid(row, col);
    }

}
