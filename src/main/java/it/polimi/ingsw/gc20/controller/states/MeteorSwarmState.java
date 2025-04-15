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

import java.util.List;

@SuppressWarnings("unused") // dynamically created by Cards
public class MeteorSwarmState extends PlayingState {
    private final List<Projectile> meteors;
    private FireManager manager;
    /**
     * Default constructor
     */
    public MeteorSwarmState(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
        this.meteors = card.getProjectiles();
        manager = new FireManager(model, meteors, controller.getPlayerByID(getCurrentPlayer()));
    }

    @Override
    public String toString() {
        return "MeteorSwarmState{" +
                "meteors=" + meteors +
                '}';
    }

    @Override
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateCannon(cannons.getFirst(), batteries.getFirst());
        do {
            manager.fire();
        } while (manager.isFirstHeavyFire());
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    
    @Override
    public void activateShield(Player player, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(shield, battery);
        do {
            manager.fire();
        } while (manager.isFirstHeavyFire());
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    @Override
    public void chooseBranch(Player player, int col, int row) throws InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.chooseBranch(player, col, row);
        while (manager.isFirstHeavyFire()) {
            manager.fire();
        }
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().drawCard();
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }
}
