package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

public class AbandonedStationState extends CargoState {
    private final int crewNeeded;
    private final List<CargoColor> reward;
    private final int lostDays;
    private boolean defeated = false;

    /**
     * Default constructor
     */
    public AbandonedStationState(int crewNeeded, List<CargoColor> reward, int lostDays) {
        super();
        this.crewNeeded = crewNeeded;
        this.reward = reward;
        this.lostDays = lostDays;
    }

    @Override
    public String toString() {
        return "AbandonedStationState";
    }

    public void landOnStation(Player player) throws InvalidTurnException {
        if(!player.getUsername().equals(currentPlayer)){
            throw new InvalidTurnException("It's not your turn");
        }
        if(player.getShip().crew() < crewNeeded){
            throw new IllegalStateException("You don't have enough crew to land on the station");
        }
        defeated = true;
    }

    /**
     * This method is used to load a cargo from the reward to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalArgumentException if the station was not boarded and if the cargo is not in the reward
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, CargoHold chTo) throws IllegalArgumentException {
        if(!defeated){
            throw new IllegalStateException("You can't load cargo unless you are on the station");
        }
        if(!reward.contains(loaded)){
            throw new IllegalStateException("You can't load this cargo, it's not in the reward");
        }
        reward.remove(loaded);
        super.loadCargo(player, loaded, chTo);
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws IllegalArgumentException if the station was not boarded
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, CargoHold ch) throws IllegalArgumentException {
        if(!defeated){
            throw new IllegalStateException("You can't unload cargo unless you are on the station");
        }
        super.unloadCargo(player, unloaded, ch);
    }

    /**
     * This method is used to move a cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param loaded the color of the cargo to be moved
     * @param chFrom the cargo hold from which the cargo is moved
     * @param chTo the cargo hold to which the cargo is moved
     * @throws IllegalArgumentException if the station was not boarded
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, CargoHold chFrom, CargoHold chTo) throws IllegalArgumentException {
        if(!defeated){
            throw new IllegalStateException("You can't move cargo unless you are on the station");
        }
        super.moveCargo(player, loaded, chFrom, chTo);
    }

    /**
     * This method is used to end the move of a player
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException {
        if(!player.getUsername().equals(currentPlayer)){
            throw new InvalidTurnException("It's not your turn");
        }
        //model.   //TODO: lose days
    }
}
