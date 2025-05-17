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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author GC20
 * During the PiratesState, the player can shoot the enemy declaring the firepower, if the result is 1, the player has defeated the enemy, if it is 0, the player has to pass the turn, if it is -1, the player has lost
 * In case of winning:
 * - the player can accept the card or endMove
 * In case of losing:
 * - the player will roll dice (if the first fire is a heavy fire, it will be automatically activated otherwise the player has to activate a shield (even null))
 * - if a InvalidShipException is thrown, the player has to validate the ship before doing anything else
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class PiratesState extends PlayingState {
    private final List<Projectile> cannonFire;
    private final int firePower;
    private final int credits;
    private final int lostDays;
    private FireManager manager;
    /**
     * Default constructor
     */
    public PiratesState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.firePower = card.getFirePower();
        this.cannonFire = card.getProjectiles();
        this.credits = card.getCredits();
        this.lostDays = card.getLostDays();
        phase = StatePhase.CANNONS_PHASE;
    }

    @Override
    public String toString() {
        return "PiratesState";
    }

    /**
     * this method is called when the player has won against the pirates and can accept the card
     * @param player the player that is accepting the card
     * @throws InvalidStateException if the player is not in the right phase
     * @throws InvalidTurnException if the player is not the current player
     */
    @Override
    public void acceptCard(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated");
        }
        //get the reward of the card
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);
    }

    /**
     * this method is called when the player has lost against the pirates and has to shoot the enemy
     * @param player the player that is shooting the enemy
     * @param cannons the cannons that the player is using
     * @param batteries the batteries that the player is using
     * @return 1 if the player has defeated the enemy, 0 if the player has to pass the turn, -1 if the player has lost
     * @throws InvalidStateException if the player is not in the right phase
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidCannonException if the player is using an invalid cannon
     * @throws EnergyException if the player doesn't have enough energy to shoot
     */
    @Override
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("Cannot perform this action in " + phase + " state");
        }
        //translate the cannons and batteries
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        //calculate the firepower
        float firePower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        //fight with the pirates
        if (firePower > this.firePower) {
            //if the player has defeated the pirates, he can accept the card
            getController().getActiveCard().playCard();
            phase = StatePhase.ACCEPT_PHASE;
            return 1;
        } else if (firePower == this.firePower) {
            //if the player has drawn with the pirates, he has to pass the turn
            nextPlayer();
            //if there is no next player, we draw a new card
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            return 0;
        } else {
            //if the player has lost against the pirates, he has to roll the dice and get shot
            phase = StatePhase.ROLL_DICE_PHASE;
            manager = new FireManager(getModel(), cannonFire, player);
            return -1;
        }
    }


    /**
     * this method is called when the player has lost against the pirates and has to roll the dice
     * @param player the player that is rolling the dice
     * @return the result of the dice
     * @throws InvalidStateException if the player is not in the right phase
     * @throws InvalidTurnException if the player is not the current player
     */
    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ROLL_DICE_PHASE) {
            throw new InvalidStateException("not in the right phase");
        }
        //roll the dice
        int result = getModel().getGame().rollDice();
        //check the type of the first projectile
        switch (manager.getFirstProjectile()) {
            case HEAVY_FIRE:
                //player cannot do anything to stop the fire
                phase = StatePhase.AUTOMATIC_ACTION;
                try {
                    //fire the projectile
                    manager.fire();
                    //check if we finished shooting
                    if (manager.finished()) {
                        //if we finished shooting, we can go to the next player
                        nextPlayer();
                        if (getCurrentPlayer() == null) {
                            //if there is no next player, we draw a new card
                            getModel().getActiveCard().playCard();
                            getController().setState(new PreDrawState(getController()));
                            phase = StatePhase.STANDBY_PHASE;
                        } else {
                            //if there is a next player, we can go to the cannon phase
                            phase = StatePhase.CANNONS_PHASE;
                        }
                    } else {
                        //if we didn't finish shooting, we can go to the roll dice phase
                        phase = StatePhase.ROLL_DICE_PHASE;
                    }
                } catch (InvalidShipException e) {
                    phase = StatePhase.VALIDATE_SHIP_PHASE;
                } catch (DieNotRolledException _) {
                    //cannot happen
                }
                break;
            case LIGHT_METEOR:
                break;
            case HEAVY_METEOR:
                break;
            case LIGHT_FIRE:
                //player can choose to activate a shield or not
                phase = StatePhase.SELECT_SHIELD;
                break;
            case null:
                //we go to the next player
                nextPlayer();
                if (getCurrentPlayer() == null) {
                    //draw a new card
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                    phase = StatePhase.STANDBY_PHASE;
                } else {
                    //the next player needs to fight pirates
                    phase = StatePhase.CANNONS_PHASE;
                }
                break;
        }
        return result;
    }

    /**
     * this method is called when the player has to choose a branch
     * @param player the player that is choosing the branch
     * @param coordinates the coordinates of the branch
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidStateException if the player is not in the right phase
     */
    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.VALIDATE_SHIP_PHASE){
            throw new InvalidStateException("Cannot perform this action in " + phase + " state");
        }
        //choose the branch
        manager.chooseBranch(player, coordinates);
        //check if we finished shooting
        if (manager.finished()) {
            //if we finished shooting, we can go to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw a new card
                phase = StatePhase.STANDBY_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                //if there is a next player, we can go to the cannon phase
                phase = StatePhase.CANNONS_PHASE;
            }
        }else {
            //if we didn't finish shooting, we can go to the roll dice phase
            phase = StatePhase.ROLL_DICE_PHASE;
        }
    }

    /**
     * this method is called when the player has to activate a shield
     * @param player the player that is activating the shield
     * @param shield the shield that the player is activating
     * @param battery the battery that the player is using
     * @throws InvalidStateException if the player is not in the right phase
     * @throws InvalidTurnException if the player is not the current player
     * @throws EnergyException if the player doesn't have enough energy to activate the shield
     */
    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidTurnException, InvalidStateException, EnergyException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        // check if the player is in the right phase
        if (phase != StatePhase.SELECT_SHIELD) {
            throw new InvalidStateException("not in the right phase");
        }
        try {
            //activate the shield
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            try {
                //fire the projectile
                manager.fire();
                //check if we finished shooting
                if (manager.finished()) {
                    nextPlayer();
                    //if we finished shooting, we can go to the next player
                    if (getCurrentPlayer() == null) {
                        //draw a new card
                        phase = StatePhase.STANDBY_PHASE;
                        getModel().getActiveCard().playCard();
                        getController().setState(new PreDrawState(getController()));
                    } else {
                        //if there is a next player, we can go to the cannon phase
                        phase = StatePhase.CANNONS_PHASE;
                    }
                } else {
                    //if we didn't finish shooting, we can go to the roll dice phase
                    phase = StatePhase.ROLL_DICE_PHASE;
                }
            } catch (InvalidShipException e) {
                phase = StatePhase.VALIDATE_SHIP_PHASE;
            } catch (DieNotRolledException _) {
                //cannot happen
            }
        } catch (InvalidShipException e){
            phase = StatePhase.VALIDATE_SHIP_PHASE;
        }
    }

    /**
     * this method is called when the player has to end the move
     * @param player the player that is ending the move
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidShipException if the ship is not valid
     * @throws IllegalStateException if the game is not in the right state
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException, InvalidShipException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if we are still shooting
        if (manager != null && !manager.finished()) {
            throw new InvalidStateException("You have to finish the fire before ending your turn");
        }
        //check if the ship is valid
        if (manager != null && manager.isSplit()) {
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            throw new InvalidShipException("Ship not valid");
        }
        //check if the card has been defeated
        if (!getController().getActiveCard().isPlayed()) {
            throw new InvalidStateException("Card not defeated");
        }
        //draw a new card
        phase = StatePhase.STANDBY_PHASE;
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player) throws InvalidTurnException, InvalidShipException {
        if (phase == StatePhase.VALIDATE_SHIP_PHASE){
            try {
                //we auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                if (phase != StatePhase.STANDBY_PHASE){
                    phase = StatePhase.STANDBY_PHASE;
                    //if we are not in the standby phase, we can draw a new card
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                }
            } catch (InvalidTurnException | InvalidStateException _) {
                //ignore
            }
        } else {
            try {
                endMove(player);
            } catch (InvalidStateException | InvalidTurnException _) {
                nextPlayer();
                if (getCurrentPlayer() == null) {
                    //draw a new card
                    phase = StatePhase.STANDBY_PHASE;
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    //if there is a next player, we can go to the cannon phase
                    phase = StatePhase.CANNONS_PHASE;
                }
            } catch (InvalidShipException e) {
                currentQuit(player);
            }
        }
    }
}
