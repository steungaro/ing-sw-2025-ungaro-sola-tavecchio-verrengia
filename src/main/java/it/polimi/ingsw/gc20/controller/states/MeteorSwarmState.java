package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.components.Shield;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.List;

@SuppressWarnings("unused") // dynamically created by Cards
public class MeteorSwarmState extends PlayingState {
    private final List<Projectile> meteors;
    private FireManager manager;
    /**
     * Default constructor
     */
    public MeteorSwarmState(GameModel model, GameController controller, AdventureCard card) {
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
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, EnergyException{
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (cannons.isEmpty() || batteries.isEmpty()) {
            return;
        }
        manager.activateCannon(Translator.getComponentAt(player, cannons.getFirst(), Cannon.class), Translator.getComponentAt(player, batteries.getFirst(), Battery.class));
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().setState(new PreDrawState(getController()));
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    @Override
    public int rollDice(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (manager == null || manager.finished()) {
            throw new IllegalStateException("Cannot roll dice when not firing");
        }
        return getModel().getGame().rollDice();
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, DieNotRolledException, EmptyDeckException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        manager.fire();
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.chooseBranch(player, coordinates);
        if (manager.finished()) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                manager = new FireManager(getModel(), meteors, getController().getPlayerByID(getCurrentPlayer()));
            }
        }
    }
}
