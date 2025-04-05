package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.CargoException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;

public abstract class CargoState extends PlayingState{

    public CargoState(){
        super();
    }

    /**
     * This method is used to load a cargo from a cargo hold to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalArgumentException if it's not the player's turn
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, CargoHold chTo) throws IllegalStateException, InvalidTurnException, CargoException{
        if(!player.getUsername().equals(currentPlayer)){
            throw new IllegalArgumentException("It's not your turn");
        }
        model.addCargo(player, loaded, chTo);
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws IllegalArgumentException if it's not the player's turn
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException {
        if(!player.getUsername().equals(currentPlayer)){
            throw new IllegalArgumentException("It's not your turn");
        }
        model.MoveCargo(player, unloaded, ch, null);
    }

    /**
     * This method is used to move a cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param loaded the color of the cargo to be moved
     * @param chFrom the cargo hold from which the cargo is moved
     * @param chTo the cargo hold to which the cargo is moved
     * @throws IllegalArgumentException if it's not the player's turn
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, CargoHold chFrom, CargoHold chTo)throws IllegalStateException, InvalidTurnException, CargoException{
        if(!player.getUsername().equals(currentPlayer)){
            throw new IllegalArgumentException("It's not your turn");
        }
        model.MoveCargo(player, loaded, chFrom, chTo);
    }
}
