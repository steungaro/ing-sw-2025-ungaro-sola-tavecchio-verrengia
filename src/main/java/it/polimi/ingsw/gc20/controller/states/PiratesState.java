package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.components.Shield;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashSet;
import java.util.List;

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
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);
    }

    @Override
    public int shootEnemy(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        float firePower = getModel().FirePower(player, new HashSet<>(cannons), batteries);
        if (firePower > this.firePower) {
            getController().getActiveCard().playCard();
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().drawCard();
            }
            return 0;
        } else {
            manager = new FireManager(getModel(), cannonFire, player);
            return -1;
        }
    }

    @Override
    public void rollDice(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (manager == null || manager.finished()) {
            throw new IllegalStateException("Cannot roll dice when not firing");
        }
        getModel().getGame().rollDice();
        if (manager.isFirstHeavyFire()) {
            manager.fire();
        }
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), cannonFire, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    @Override
    public void chooseBranch(Player player, int col, int row) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.chooseBranch(player, col, row);
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), cannonFire, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    @Override
    public void activateShield(Player player, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(shield, battery);
        manager.fire();
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), cannonFire, getController().getPlayerByID(getCurrentPlayer()));
            }
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
        getModel().getActiveCard().playCard();
        getController().drawCard();
    }
}
