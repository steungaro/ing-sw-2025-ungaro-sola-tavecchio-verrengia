package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused") // dynamically created by Cards
public class MeteorSwarmState extends PlayingState {
    private final List<Projectile> meteors;
    private Map<Player, FireManager> fireManagerMap;
    private Map<Player, StatePhase> phaseMap;
    /**
     * Default constructor
     */
    public MeteorSwarmState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.meteors = card.getProjectiles();
        //init the map
        for (String username : getController().getInGameConnectedPlayers()) {
            fireManagerMap.put(getController().getPlayerByID(username), new FireManager(model, meteors, getController().getPlayerByID(username)));
            if (username.equals(getCurrentPlayer())) {
                phaseMap.put(getController().getPlayerByID(username), StatePhase.ROLL_DICE_PHASE);
            } else {
                phaseMap.put(getController().getPlayerByID(username), StatePhase.STANDBY_PHASE);
            }
        }
    }

    @Override
    public String toString() {
        return "MeteorSwarmState{" +
                "meteors=" + meteors +
                '}';
    }

    /**
     * This method is called to activate the cannons that the player has selected
     * @param player the player who is activating the cannons
     * @param cannons the cannons that the player has selected
     * @param batteries the batteries that the player has selected
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidStateException if the player is not in the correct state
     * @throws EnergyException if the player does not have enough energy to activate the cannons
     */
    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phaseMap.get(player) != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("cannot activate cannons in this phase");
        }
        try {
            // activate the cannons of the current player
            fireManagerMap.get(player).activateCannon(Translator.getComponentAt(player, cannons.getFirst(), Cannon.class), Translator.getComponentAt(player, batteries.getFirst(), Battery.class));
            try {
                //fire the projectile
                fireManagerMap.get(player).fire();
                //go to the next player
                nextPlayer();
                //if all the player has received this projectile
                if (getCurrentPlayer() == null) {
                    //verify if the first player has received all the meteors
                    setCurrentPlayer(getController().getFirstOnlinePlayer());
                    if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                        //if we finished the projectile, we draw a new card
                        for (String p : getController().getInGameConnectedPlayers()) {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                        getModel().getActiveCard().playCard();
                        getController().setState(new PreDrawState(getController()));
                    } else {
                        //if there are still projectiles to shoot, the leader is set to the roll dice phase
                        for (String p : getController().getInGameConnectedPlayers()) {
                            if (p.equals(getCurrentPlayer())) {
                                phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                            } else {
                                phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                            }
                        }

                    }
                } else {
                    //if there is a next player,
                    // we set the phase of the current player to standby and the next player to cannon phase
                    phaseMap.put(player, StatePhase.STANDBY_PHASE);
                    phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.CANNONS_PHASE);
                }
            } catch (InvalidShipException e) {
                phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
                //phase is used to store the previous phase of the player
                phase = StatePhase.CANNONS_PHASE;
            } catch (DieNotRolledException _) {
                //cannot happen
            }
        } catch (InvalidShipException e){
            phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
            //phase is used to store the previous phase of the player
            phase = StatePhase.CANNONS_PHASE;
        }
    }

    /**
     * This method is called to roll the dice
     * @param player the player who is rolling the dice
     * @return the value of the dice rolled
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidStateException if the player is not in the correct state
     */
    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (fireManagerMap.get(player).finished()) {
            throw new InvalidStateException("Cannot roll dice when not firing");
        }
        if (phaseMap.get(player) != StatePhase.ROLL_DICE_PHASE){
            throw new InvalidStateException("Cannot roll dice in this phase");
        }
        //roll the dice
        int result = getModel().getGame().rollDice();
        //check the type of the first projectile
        switch (fireManagerMap.get(player).getFirstProjectile()) {
            case LIGHT_METEOR:
                //set the phase of the current player to the select shield phase, and other players to standby
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.SELECT_SHIELD);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
                break;
            case HEAVY_METEOR:
                //set the phase of the current player to the cannon phase, and other players to standby
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.CANNONS_PHASE);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
                break;
        }
        return result;
    }

    /**
     * this method activates the shield of the player
     * @param player the player who is activating the shield
     * @param shield the shield that the player has selected
     * @param battery the battery that the player has selected
     * @throws InvalidStateException if the player is not in the correct state
     * @throws InvalidTurnException if the player is not the current player
     * @throws EnergyException if the player does not have enough energy to activate the shield
     */
    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidTurnException, InvalidStateException, EnergyException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the correct state
        if (!phaseMap.get(player).equals(StatePhase.SELECT_SHIELD)) {
            throw new InvalidStateException("cannot activate shield in this phase");
        }

        try {
            //activate the shield
            fireManagerMap.get(player).activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            try {
                fireManagerMap.get(player).fire();
                nextPlayer();
                //if all the player has received this projectile
                if (getCurrentPlayer() == null) {
                    //verify if the first player has received all the meteors
                    setCurrentPlayer(getController().getFirstOnlinePlayer());
                    //check if we finished shooting the meteors
                    if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                        //if we finished the projectiles, we draw a new card
                        for (String p : getController().getInGameConnectedPlayers()) {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                        getModel().getActiveCard().playCard();
                        getController().setState(new PreDrawState(getController()));
                    } else {
                        //else the leader rolls again the dice
                        for (String p : getController().getInGameConnectedPlayers()) {
                            if (p.equals(getCurrentPlayer())) {
                                phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                            } else {
                                phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                            }
                        }

                    }
                } else {
                    //if there is a next player,
                    // we set the phase of the current player to standby and the next player to shield phase
                    phaseMap.put(player, StatePhase.STANDBY_PHASE);
                    phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.SELECT_SHIELD);
                }
            } catch (InvalidShipException e) {
                phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
                //phase is used to store the previous phase of the player
                phase = StatePhase.SELECT_SHIELD;
            } catch (DieNotRolledException _){
                //cannot happen
            }
        } catch (InvalidShipException e) {
            phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
            //phase is used to store the previous phase of the player
            phase = StatePhase.SELECT_SHIELD;
        }
    }

    /**
     * this method is called when the player chooses a branch
     * @param player the player who is choosing the branch
     * @param coordinates the coordinates of the branch
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidStateException if the player is not in the correct state
     */
    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the correct state
        if (phaseMap.get(player) != StatePhase.VALIDATE_SHIP_PHASE){
            throw new InvalidStateException("cannot choose branch in this phase");
        }
        //choose the branch selected by the player
        fireManagerMap.get(player).chooseBranch(player, coordinates);
        //after the player chose the branch, we check if there is a next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //if there is no next player, we set the current player to the first player
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            //we verify if we shot all the projectiles
            if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()){
                //we set all the player phase to standby, and we can go to the next card
                for (String p : getController().getInGameConnectedPlayers()) {
                    phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                }
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                //if is not finished we set the first player to the roll dice phase and the others to the standby phase
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())){
                        phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
            }
        } else {
            //if there is a next player, we modify the state to the correct phase, memorized in the phase attribute
            phaseMap.put(player, StatePhase.STANDBY_PHASE);
            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), phase);
        }
    }

    /**
     * this method is called when the player quits the game
     * @param player the player who is quitting the game
     */
    @Override
    public void currentQuit(Player player) {
        //check if the player is validating is ship
        if (phaseMap.get(player) == StatePhase.VALIDATE_SHIP_PHASE) {
            //if the player is validating his ship, we automatically choose the branch
            try {
                chooseBranch(player, new Pair<>(-1, -1));
            } catch (InvalidTurnException | InvalidStateException e) {
                //ignore
            }
        } else {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //if there is no next player, we set the current player to the first player
                setCurrentPlayer(getController().getFirstOnlinePlayer());
                //we verify if we shot all the projectiles
                if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                    //we set all the player phase to standby, and we can go to the next card
                    for (String p : getController().getInGameConnectedPlayers()) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    //if is not finished we set the first player to the roll dice phase and the others to the standby phase
                    for (String p : getController().getInGameConnectedPlayers()) {
                        if (p.equals(getCurrentPlayer())) {
                            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                        } else {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                    }
                }
            } else {
                //if there is a next player, we modify the state to the correct phase, memorized in the phase attribute
                phaseMap.put(player, StatePhase.STANDBY_PHASE);
                phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), phase);
            }
        }
    }
}
