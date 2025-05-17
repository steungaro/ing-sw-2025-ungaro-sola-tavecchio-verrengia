package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.player.Player;

public class Validator {
    private boolean isSplit;

    public void setSplit() {
        isSplit = true;
    }

    public void chooseBranch(Player p, Integer row, Integer col) throws InvalidStateException{
        if(!isSplit){
            throw new InvalidStateException("Ship is valid.");
        }
        p.getShip().findValid(row, col);
        isSplit = false;
    }

    public boolean isSplit() {
        return isSplit;
    }
}
