package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class ValidatingShipState extends State {
    private final Map<Player, Boolean> validShips = new HashMap<>();
    private final Map<Player, Boolean> readyToFly = new HashMap<>();
    private final Map<Player, StatePhase> phaseMap = new HashMap<>();
    /**
     * Default constructor
     */
    public ValidatingShipState(GameModel model) {
        super(model);
        for (Player player : model.getInGamePlayers()) {
            validShips.put(player, false);
            readyToFly.put(player, model.getLevel() == 0); // if level 0, alien is considered added
            phaseMap.put (player, StatePhase.VALIDATE_SHIP_PHASE); //set all the phase to validate ship phase
        }
    }

    @Override
    public String toString() {
        return "ValidatingShipState";
    }

    /**
     * this method is called to validate the ship of the player
     * @param player the player that is validating the ship
     * @return true if the ship is valid, false otherwise
     * @throws InvalidStateException if the game is not in the validate ship phase
     */
    @Override
    public boolean isShipValid(Player player) throws InvalidStateException{
        if (phaseMap.get(player) != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot validate ship in this phase");
        }
        if (getModel().shipValidating(player)) {
            //if the ship is valid, he can add the aliens
            validShips.put(player, true);
            if (getModel().getLevel() == 0) {
                phaseMap.put(player, StatePhase.STANDBY_PHASE);
                readyToFly.put(player, true);
            } else {
                phaseMap.put(player, StatePhase.ADD_ALIEN_PHASE);
            }
            return true;
        }
        return false;
    }

    /**
     * this method is called to remove a component from the ship of the player
     * @param player the player that is removing the component
     * @param coordinates the coordinates of the component to remove
     * @throws ComponentNotFoundException if the component is not found
     * @throws InvalidStateException if the game is not in the validate ship phase
     */
    @Override
    public void removeComp(Player player, Pair<Integer, Integer> coordinates) throws ComponentNotFoundException, InvalidStateException {
        if (phaseMap.get(player)!=StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot remove component from valid ship");
        }
        //remove the component from the ship
        getModel().removeComponent(coordinates.getValue0(), coordinates.getValue1(), player);
    }

    @Override
    public boolean allShipsReadyToFly() {
        return validShips.values().stream().allMatch(Boolean::booleanValue) && readyToFly.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public boolean allShipsValidated() {
        return validShips.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * this method is used to add an alien to the ship of the player
     * @param player the player that is adding the alien
     * @param color the color of the alien
     * @param cabin the cabin where the alien is added
     * @throws InvalidAlienPlacement if the alien cannot be placed in the cabin
     * @throws InvalidStateException if the game is not in the add alien phase
     */
    @Override
    public void addAlien(Player player, AlienColor color, Pair<Integer, Integer> cabin) throws InvalidAlienPlacement, InvalidStateException {
        if (phaseMap.get(player) != StatePhase.ADD_ALIEN_PHASE) {
            throw new InvalidStateException("Cannot add alien in this phase");
        }
        //add the alien to the ship
        getModel().setAlien(color, Translator.getComponentAt(player, cabin, Cabin.class), player);
    }

    /**
     * this method is called to set the ship of the player ready to fly
     * @param player the player that is setting the ship ready to fly
     * @throws IllegalArgumentException if the ship is not valid
     */
    @Override
    public void readyToFly(Player player) throws InvalidShipException {
        if (!validShips.get(player)) {
            throw new InvalidShipException("Cannot fly with invalid ship");
        }
        readyToFly.put(player, true);
        phaseMap.put(player, StatePhase.STANDBY_PHASE);
    }

    /**
     * this method is called if all the ships are ready to fly
     * @throws InvalidStateException if some ships are not ready to fly
     */
    @Override
    public void initAllShips() throws InvalidStateException {
        if (!allShipsReadyToFly()) {
            throw new InvalidStateException("Cannot initialize ships: some ships are not ready to fly");
        }
        for (Player player : getModel().getInGamePlayers()) {
            getModel().addPieces(player);
        }
    }

    /**
     * this method is called to end the placing aline phase
     * @param player the player that is ending the placing aline phase
     * @throws InvalidStateException if the game is not in the add alien phase
     */
    @Override
    public void endMove (Player player) throws InvalidStateException {
        if (phaseMap.get(player) != StatePhase.ADD_ALIEN_PHASE) {
            throw new InvalidStateException("Cannot end move in this phase");
        }
        readyToFly.put(player, true);
        phaseMap.put(player, StatePhase.STANDBY_PHASE);
    }
}
