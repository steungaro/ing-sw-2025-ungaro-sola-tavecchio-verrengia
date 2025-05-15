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

import static it.polimi.ingsw.gc20.server.model.cards.FireType.HEAVY_FIRE;
import static it.polimi.ingsw.gc20.server.model.cards.FireType.LIGHT_FIRE;

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

    @Override
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!getController().getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new IllegalStateException("Cannot perform this action in " + phase + " state");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);
    }

    @Override
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new IllegalStateException("Cannot perform this action in " + phase + " state");
        }
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        float firePower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        if (firePower > this.firePower) {
            getController().getActiveCard().playCard();
            phase = StatePhase.ACCEPT_PHASE;
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            //la fase andra al prossimo player gli altri in standby
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            return 0;
        } else {
            phase = StatePhase.ROLL_DICE_PHASE;
            manager = new FireManager(getModel(), cannonFire, player);
            return -1;
        }
    }

    @Override
    public int rollDice(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (manager == null || manager.finished()) {
            throw new IllegalStateException("Cannot roll dice when not firing");
        }
        getModel().getGame().rollDice();
        switch (manager.getFirstProjectile()) {
            case HEAVY_FIRE:
                phase = StatePhase.AUTOMATIC_ACTION;
                try {
                    manager.fire();
                    if (manager.finished()) {
                        nextPlayer();
                        if (getCurrentPlayer() == null) {
                            getModel().getActiveCard().playCard();
                            getController().setState(new PreDrawState(getController()));
                            phase = StatePhase.STANDBY_PHASE;
                        } else {
                            phase = StatePhase.CANNONS_PHASE;
                        }
                    } else {
                        phase = StatePhase.ROLL_DICE_PHASE;
                    }
                } catch (InvalidShipException e) {
                    phase = StatePhase.VALIDATE_SHIP_PHASE;
                }
                break;
            case LIGHT_FIRE:
                phase = StatePhase.SELECT_SHIELD;
                break;
            case null:
                nextPlayer();
                if (getCurrentPlayer() == null) {
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                    phase = StatePhase.STANDBY_PHASE;
                } else {
                    //next player needs to fight pirates
                    phase = StatePhase.CANNONS_PHASE;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cannonFire.getFirst());
        }
        return getModel().getGame().rollDice();
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.VALIDATE_SHIP_PHASE){
            throw new IllegalStateException("Cannot perform this action in " + phase + " state");
        }
        manager.chooseBranch(player, coordinates);
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
            }
        }else {
            phase = StatePhase.ROLL_DICE_PHASE;
        }
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.SELECT_SHIELD) {
            throw new IllegalStateException("Cannot perform this action in " + phase + " state");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        try {
            manager.fire();
            if (manager.finished()) {
                nextPlayer();
                if (getCurrentPlayer() == null) {
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    phase = StatePhase.CANNONS_PHASE;
                }
            } else {
                phase = StatePhase.ROLL_DICE_PHASE;
            }
        } catch (InvalidShipException e) {
            phase = StatePhase.VALIDATE_SHIP_PHASE;
        }
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (manager != null && !manager.finished()) {
            throw new IllegalStateException("You have to finish the fire before ending your turn");
        }
        if (manager != null && manager.isSplit()) {
            throw new InvalidShipException("Ship not valid");
        }
        if (!getController().getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        phase = StatePhase.STANDBY_PHASE;
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player) throws InvalidTurnException, InvalidShipException {
        endMove(player);
    }
}
