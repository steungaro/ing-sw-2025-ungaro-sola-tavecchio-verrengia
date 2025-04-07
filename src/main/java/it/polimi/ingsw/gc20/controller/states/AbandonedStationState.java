package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.CargoException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

/**
 * This class represents the state of the game when an Abandoned Station card is played
 * The player can:
 * - accept the card
 *      + load cargo from the reward to the player's cargo hold
 *      + unload cargo from the player's cargo hold
 *      + move cargo from one cargo hold to another
 *      + end the move
 * - discard the card (end the move)
 * if the card is not accepted by any player, or if the card has been played, a new card is drawn
 */
public class AbandonedStationState extends CargoState {
    private final int crewNeeded;
    private final List<CargoColor> reward;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedStationState(GameController gc, GameModel gm, int crewNeeded, List<CargoColor> reward, int lostDays) {
        super(gc, gm);
        this.crewNeeded = crewNeeded;
        this.reward = reward;
        this.lostDays = lostDays;
    }

    @Override
    public String toString() {
        return "AbandonedStationState{ " +
                "crewNeeded=" + crewNeeded +
                ", reward=" + reward +
                ", lostDays=" + lostDays +
                '}';
    }

    @Override
    public void acceptCard(Player player) throws InvalidTurnException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn");
        }
        if(player.getShip().crew() < crewNeeded){
            throw new IllegalStateException("You don't have enough crew to land on the station");
        }
        getController().getActiveCard().playCard();
    }

    /**
     * This method is used to load a cargo from the reward to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalArgumentException if the station was not boarded and if the cargo is not in the reward
     * @throws CargoException if the cargo is not in the reward
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, CargoHold chTo) throws IllegalArgumentException, CargoException, InvalidTurnException {
        if(getController().getActiveCard().isPlayed()){
            throw new IllegalStateException("You can't load cargo unless you are on the station");
        }
        if(!reward.contains(loaded)){
            throw new CargoException("You can't load this cargo, it's not in the reward");
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
     * @throws CargoException if the cargo is not in the player's cargo hold
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, CargoHold ch) throws IllegalArgumentException, InvalidTurnException, CargoException {
        if(getController().getActiveCard().isPlayed()){
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
     * @throws CargoException if the cargo is not in the player's cargo hold
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, CargoHold chFrom, CargoHold chTo) throws IllegalArgumentException, InvalidTurnException, CargoException {
        if(getController().getActiveCard().isPlayed()){
            throw new IllegalStateException("You can't move cargo unless you are on the station");
        }
        super.moveCargo(player, loaded, chFrom, chTo);
    }

    /**
     * This method is used to end the move of a player
     * If the card has been played or if nobody has accepted the card, a new card is drawn
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (getController().getActiveCard().isPlayed()) {
            // TODO getModel().move(player, -lostDays);
            getController().drawCard();
        } else {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().drawCard();
            }
        }
    }
}
