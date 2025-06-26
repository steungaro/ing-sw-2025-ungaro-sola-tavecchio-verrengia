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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
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
    private int result;

    /**
     * Constructs a PiratesState object. This initializes the state with the provided
     * game model, controller, and adventure card, setting the initial phase to CANNONS_PHASE.
     * It also configures the standby message for the current player and notifies the
     * message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public PiratesState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.firePower = card.getFirePower();
        this.cannonFire = card.getProjectiles();
        this.credits = card.getCredits();
        this.lostDays = card.getLostDays();
        phase = StatePhase.CANNONS_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is shooting at the enemy.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void acceptCard(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated.");
        }
        //get the reward of the card
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);
        getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player.getUsername(), credits, player.isInGame(), player.getColor(), (player.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
        try {
            endMove(player);
        } catch (InvalidShipException _) {
            //cannot happen
        }

    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("Cannot perform this action in " + phase + " state.");
        }
        //translate the cannons and batteries
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        //calculate the firepower
        float firePower = getModel().firePower(player, cannonsComponents, batteriesComponents);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated cannons"));
        //fight with the pirates
        if (firePower > this.firePower) {
            //if the player has defeated the pirates, he can accept the card
            phase = StatePhase.ACCEPT_PHASE;
            setStandbyMessage(getCurrentPlayer() + " has defeated the pirates.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
            getController().getActiveCard().playCard();
        } else if (firePower == this.firePower) {
            //if the player has drawn with the pirates, he has to pass the turn
            nextPlayer();
            //if there is no next player, we draw a new card
            if (getCurrentPlayer() == null) {
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage(getCurrentPlayer() + " is activating cannons to fight the pirates.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else {
            //if the player has lost against the pirates, he has to roll the dice and get shot
            phase = StatePhase.ROLL_DICE_PHASE;
            manager = new FireManager(getModel(), cannonFire, player);
            setStandbyMessage(getCurrentPlayer() + " has lost against the pirates, he is rolling the dice to see where the projectile hits.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ROLL_DICE_PHASE) {
            throw new InvalidStateException("Not in the right phase.");
        }
        //roll the dice
        result = getModel().getGame().rollDice();
        //check the type of the first projectile
        switch (manager.getFirstProjectile()) {
            case HEAVY_FIRE:
                //player cannot do anything to stop the fire
                phase = StatePhase.AUTOMATIC_ACTION;
                try {
                    //fire the projectile
                    manager.fire();
                    getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "hit by heavy fire"));
                    //check if we finished shooting
                    if (manager.finished()) {
                        //if we finished shooting, we can go to the next player
                        nextPlayer();
                        if (getCurrentPlayer() == null) {
                            //if there is no next player, we draw a new card
                            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                            getModel().getActiveCard().playCard();
                            getController().setState(new PreDrawState(getController()));
                            phase = StatePhase.DRAW_CARD_PHASE;
                        } else {
                            //if there is a next player, we can go to the cannon phase
                            phase = StatePhase.CANNONS_PHASE;
                            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                            getController().getMessageManager().notifyPhaseChange(phase, this);
                        }
                    } else {
                        phase = StatePhase.ROLL_DICE_PHASE;
                        setStandbyMessage(getCurrentPlayer() + " is rolling the dice.");
                        getController().getMessageManager().notifyPhaseChange(phase, this);
                    }
                } catch (InvalidShipException e) {
                    notifyInvalidShip(player);
                } catch (DieNotRolledException _) {
                    //cannot happen
                }
                break;
            case LIGHT_METEOR, HEAVY_METEOR:
                break;
            case LIGHT_FIRE:
                //player can choose to activate a shield or not
                phase = StatePhase.SELECT_SHIELD;
                setStandbyMessage(getCurrentPlayer() + " is selecting the shield to activate.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
                break;
        }
        return result;
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.VALIDATE_SHIP_PHASE){
            throw new InvalidStateException("Cannot perform this action in " + phase + " state.");
        }
        //choose the branch
        manager.chooseBranch(player, coordinates);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose branch"));
        //check if we finished shooting
        finishManager();
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, EnergyException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        // check if the player is in the right phase
        if (phase != StatePhase.SELECT_SHIELD) {
            throw new InvalidStateException("Not in the right phase.");
        }
        try {
            //activate the shield
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated shield"));
            //fire the projectile
            manager.fire();
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "hit by heavy fire"));
            //check if we finished shooting
            finishManager();
        } catch (InvalidShipException e){
            notifyInvalidShip(player);
        } catch (DieNotRolledException e) {
            //cannot happen
        }
    }

    /**
     * This method is called to finish the fire manager and check if we can go to the next player or not.
     * If the manager is finished, we go to the next player, otherwise we stay in the roll dice phase.
     */
    private void finishManager() {
        if (manager.finished()) {
            toNextPlayer();
        } else {
            phase = StatePhase.ROLL_DICE_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is rolling the dice");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void endMove(Player player) throws InvalidTurnException, InvalidShipException, InvalidStateException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if we are still shooting
        if (manager != null && !manager.finished()) {
            throw new InvalidStateException("You have to finish the fires before ending your turn.");
        }
        //check if the ship is valid
        if (manager != null && manager.isSplit()) {
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            throw new InvalidShipException("Ship not valid!");
        }
        //check if the card has been defeated
        if (!getController().getActiveCard().isPlayed()) {
            throw new InvalidStateException("Card not defeated.");
        }
        //draw a new card
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        phase = StatePhase.DRAW_CARD_PHASE;
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player) {
        if (phase == StatePhase.VALIDATE_SHIP_PHASE){
            try {
                //we auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose a branch"));
                if (phase != StatePhase.DRAW_CARD_PHASE){
                    phase = StatePhase.DRAW_CARD_PHASE;
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
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
                toNextPlayer();
            } catch (InvalidShipException e) {
                currentQuit(player);
            }
        }
    }

    /**
     * This method is called to go to the next player.
     * If we finished shooting, we can go to the next player, otherwise we stay in the roll dice phase.
     */
    private void toNextPlayer () {
        nextPlayer();
        //if we finished shooting, we can go to the next player
        if (getCurrentPlayer() == null) {
            //draw a new card
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            phase = StatePhase.CANNONS_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is activating cannons to fight the pirates.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * This method is called to notify the player that the ship is invalid.
     * It broadcasts an update message and changes the phase to VALIDATE_SHIP_PHASE.
     *
     * @param player the player whose ship is invalid
     */
    private void notifyInvalidShip(Player player) {
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "hit by heavy fire"));
        phase = StatePhase.VALIDATE_SHIP_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is validating the ship");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }


    @Override
    public String createsCannonsMessage(){
        return "You are fighting pirates, enemy firepower is " + firePower + ", select the cannons to use.";
    }

    @Override
    public String createsShieldMessage() {
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + "side at line" + result + ", select the shield to activate.";
    }

    @Override
    public String createsRollDiceMessage() {
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + "side, roll the dice to see where it will hit.";
    }
}
