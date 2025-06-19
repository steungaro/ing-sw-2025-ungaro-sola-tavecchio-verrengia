package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.player.Player;

/**
 * The Validator class provides functionality to manage and validate
 * the state of a branching action within a gameplay context.
 */
public class Validator {
    private boolean isSplit;

    /**
     * Marks the current state as split by setting the split flag to true.
     * This method is typically used to indicate that a branching action
     * has occurred, altering the state to reflect this change.
     */
    public void setSplit() {
        isSplit = true;
    }

    /**
     * Validates and selects a branch at the specified row and column for the player's ship
     * if the current state allows branching. Throws an exception if the state is invalid for branching.
     *
     * @param p the player whose ship is being updated
     * @param row the row index to validate for the branch
     * @param col the column index to validate for the branch
     * @throws InvalidStateException if the current state does not allow branching
     */
    public void chooseBranch(Player p, Integer row, Integer col) throws InvalidStateException{
        if(!isSplit){
            throw new InvalidStateException("Ship is valid.");
        }
        p.getShip().findValid(row, col);
        isSplit = false;
    }

    /**
     * Checks whether the current state is marked as split.
     *
     * @return true if the current state is split, false otherwise.
     */
    public boolean isSplit() {
        return isSplit;
    }
}
