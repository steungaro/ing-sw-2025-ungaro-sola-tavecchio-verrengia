package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
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
 * - the player can activate the cannon or the shield
 * - heavu fire are automatically activated
 */
public class PiratesState extends PlayingState {
    private final List<Projectile> cannonFire;
    private final int firePower;
    private final int credits;
    private final int lostDays;
    private FireManager manager;
    /**
     * Default constructor
     */
    public PiratesState(GameController gc, GameModel gm, int firePower, List<Projectile> cannonFire, int credits, int lostDays) {
        super(gm, gc);
        this.firePower = firePower;
        this.cannonFire = cannonFire;
        this.credits = credits;
        this.lostDays = lostDays;
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
        //TODO getModel().move(player, -lostDays);
        //TODO getModel().addCredits(player, credits);
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
            while (!manager.isFirstHeavyFire()) {
                manager.fire();
            }
            if (manager.finished()) {
                getController().drawCard();
            }
            return -1;
        }
    }

    @Override
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateCannon(cannons.getFirst(), batteries.getFirst());
        do {
            manager.fire();
        } while (!manager.isFirstHeavyFire());
        if (manager.finished()) {
            getController().drawCard();
        }
    }

    @Override
    public void activateShield(Player player, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(shield, battery);
        do {
            manager.fire();
        } while (!manager.isFirstHeavyFire());
        if (manager.finished()) {
            getController().drawCard();
        }
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (manager != null && !manager.finished()) {
            throw new IllegalStateException("You have to finish the fire before ending your turn");
        }
        if (!getController().getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        getController().drawCard();
    }
}
