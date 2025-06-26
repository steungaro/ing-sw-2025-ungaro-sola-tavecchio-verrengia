package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
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
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the MeteorSwarmState in the game, which is a specific playing state where
 * players must navigate through a meteor swarm. This state focuses on player actions related to
 * rolling dice, activating cannons, and shields, and validating their ship after receiving meteors.
 * It is dynamically created during the game and integrates with the game model, controller,
 * and adventure card mechanics.
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class MeteorSwarmState extends PlayingState {
    private final Map<Player, FireManager> fireManagerMap = new HashMap<>();
    private int result;
    private StatePhase phaseSave; // used to save the previous phase of the player

    /**
     * Constructs a MeteorSwarmState object. This initializes the state with the provided
     * game model, controller, and adventure card, setting up the fire managers for each player.
     * The initial phase is set to ROLL_DICE_PHASE, and a standby message is configured for the
     * current player. The message manager is notified of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public MeteorSwarmState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        List<Projectile> meteors = card.getProjectiles();
        //init the map
        for (String username : getController().getInGameConnectedPlayers()) {
            fireManagerMap.put(getController().getPlayerByID(username), new FireManager(model, meteors, getController().getPlayerByID(username)));
        }
        phase = StatePhase.ROLL_DICE_PHASE;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("Cannot activate cannons in this phase.");
        }
        try {
            if (cannons == null || cannons.isEmpty() || batteries == null || batteries.isEmpty()) {
                fireManagerMap.get(player).activateCannon(null, null);
            } else {
                fireManagerMap.get(player).activateCannon(Translator.getComponentAt(player, cannons.getFirst(), Cannon.class), Translator.getComponentAt(player, batteries.getFirst(), Battery.class));
            }

            // activate the cannons of the current player
            //fire the projectile
            fireManagerMap.get(player).fire();
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated cannons"));
            //go to the next player
            nextPlayer();
            //if all the player has received this projectile
            if (getCurrentPlayer() == null) {
                //verify if the first player has received all the meteors
                RestartTurn();
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to activate the cannons.");
                NotifyDefensiveCannon();
            }
        } catch (InvalidShipException e) {
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed by a heavy meteor"));
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            phaseSave = StatePhase.CANNONS_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to validate their ship.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } catch (DieNotRolledException _) {
            //cannot happen
        }
    }

    /**
     * This method is called to notify the player that they need to activate cannons
     */
    private void NotifyDefensiveCannon() {
        phase = StatePhase.CANNONS_PHASE;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to activate the cannons.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is called to activate a round of the meteor swarm.
     */
    private void RestartTurn() {
        setCurrentPlayer(getController().getFirstOnlinePlayer());
        if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
            //if we finished the projectile, we draw a new card
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            //if there are still projectiles to shoot, the leader is set to the roll dice phase
            NotifyRollDice();

        }
    }

    /**
     * This method is called to notify the player that they need to roll the dice.
     * It sets the phase to ROLL_DICE_PHASE and notifies the message manager of the phase change.
     */
    private void NotifyRollDice() {
        phase = StatePhase.ROLL_DICE_PHASE;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (fireManagerMap.get(player).finished()) {
            throw new InvalidStateException("Cannot roll dice now, your ship has survived through the meteor swarm!");
        }
        if (phase != StatePhase.ROLL_DICE_PHASE) {
            throw new InvalidStateException("Cannot roll dice in this phase.");
        }
        //roll the dice
        result = getModel().getGame().rollDice();
        //check the type of the first projectile
        switch (fireManagerMap.get(player).getFirstProjectile()) {
            case LIGHT_METEOR:
                notifyDefensiveShield();
                break;
            case HEAVY_METEOR:
                NotifyDefensiveCannon();

                break;
        }
        return result;
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, EnergyException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the correct state
        if (phase != StatePhase.SELECT_SHIELD) {
            throw new InvalidStateException("Cannot activate shield in this phase.");
        }

        try {
            //activate the shield
            fireManagerMap.get(player).activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));

            fireManagerMap.get(player).fire();
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated shield"));
            nextPlayer();
            //if all the player has received this projectile
            if (getCurrentPlayer() == null) {
                //verify if the first player has received all the meteors
                RestartTurn();
            } else {
                notifyDefensiveShield();
            }
        } catch (InvalidShipException e) {
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed by a heavy meteor"));
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            phaseSave = StatePhase.SELECT_SHIELD;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to validate their ship.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } catch (DieNotRolledException _) {
            //cannot happen
        }
    }

    /**
     * This method is called to notify the player that they need to activate the shields
     */
    private void notifyDefensiveShield() {
        phase = StatePhase.SELECT_SHIELD;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to activate the shields.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the correct state
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot choose branch in this phase.");
        }
        //choose the branch selected by the player
        fireManagerMap.get(player).chooseBranch(player, coordinates);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose branch"));
        //after the player chose the branch, we check if there is a next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //if there is no next player, we set the current player to the first player
            RestartTurn();
        } else {
            if (phaseSave == StatePhase.CANNONS_PHASE) {
                NotifyDefensiveCannon();
            } else if (phaseSave == StatePhase.SELECT_SHIELD) {
                notifyDefensiveShield();
            }
            //if there is a next player, we modify the state to the correct phase, memorized in the phase attribute
            phase = phaseSave;
        }
    }

    @Override
    public void currentQuit(Player player) {
        //if we are in the rolling dice phase, we need to auto roll the dice for the others player
        //if we are in the cannon phase or in the shield phase, we skip the turn of the player
        //if we are in the validate ship phase we auto choose the branch
        if (phase == StatePhase.ROLL_DICE_PHASE) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else if (phase == StatePhase.CANNONS_PHASE || phase== StatePhase.SELECT_SHIELD) {
            phaseSave = phase;
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //if there is no next player, we set the current player to the first player
                setCurrentPlayer(getController().getFirstOnlinePlayer());
                //we verify if we shot all the projectiles
                if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                    phase = StatePhase.DRAW_CARD_PHASE;
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    //if is not finished we set the first player to the roll dice phase and the others to the standby phase
                    NotifyRollDice();
                }
            } else {
                //if there is a next player,
                if (phaseSave == StatePhase.CANNONS_PHASE) {
                    NotifyDefensiveCannon();
                } else if (phaseSave == StatePhase.SELECT_SHIELD) {
                    notifyDefensiveShield();
                }
                // we put the current player to standby and the next player to the correct phase
                phase = phaseSave;
            }
        } else if (phase == StatePhase.VALIDATE_SHIP_PHASE) {
            try {
                //auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "automatically chose a branch"));
            } catch (InvalidTurnException | InvalidStateException e) {
                //cannot happen
            }
        }
    }

    @Override
    public String createsShieldMessage() {
        return "A " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstProjectile().getFireType() + " is coming from the " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstDirection().getDirection() + " side at line " + result + ", select the shields to activate.";
    }

    @Override
    public String createsRollDiceMessage() {
        return "A " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstProjectile().getFireType() + " is coming, from the " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstDirection().getDirection() + " side, roll the dice to see where it will hit.";
    }

    @Override
    public String createsCannonsMessage() {
        return "A " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstProjectile().getFireType() + " is coming from the " + fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).getFirstDirection().getDirection() + " side at line " + result + ", select the cannons to activate.";
    }
}
