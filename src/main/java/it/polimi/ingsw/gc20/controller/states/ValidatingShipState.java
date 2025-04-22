package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.DeadAlienException;
import it.polimi.ingsw.gc20.exceptions.InvalidAlienPlacement;
import it.polimi.ingsw.gc20.model.components.AlienColor;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class ValidatingShipState extends State {
    private final Map<Player, Boolean> validShips = new HashMap<>();
    private final Map<Player, Boolean> readyToFly = new HashMap<>();

    /**
     * Default constructor
     */
    public ValidatingShipState(GameModel model) {
        super(model);
        for (Player player : model.getInGamePlayers()) {
            validShips.put(player, false);
            readyToFly.put(player, model.getLevel() == 0); // if level 0, alien is considered added
        }
    }

    @Override
    public String toString() {
        return "ValidatingShipState";
    }

    @Override
    public boolean isShipValid(Player player) {
        if (getModel().shipValidating(player)) {
            validShips.put(player, true);
            return true;
        }
        return false;
    }

    @Override
    public void removeComp(Player player, Component component) throws DeadAlienException {
        if (validShips.get(player)) {
            throw new IllegalArgumentException("Cannot remove component from valid ship");
        }
        getModel().removeComponent(component, player);
    }

    @Override
    public boolean allShipsReadyToFly() {
        return validShips.values().stream().allMatch(Boolean::booleanValue) && readyToFly.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public boolean allShipsValidated() {
        return validShips.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public void addAlien(Player player, AlienColor color, Cabin cabin) throws InvalidAlienPlacement {
        if (!validShips.get(player)) {
            throw new IllegalArgumentException("Cannot add alien to invalid ship");
        }
        if (getModel().getLevel() < 2) {
            throw new IllegalArgumentException("Aliens can only be added in level 2");
        }
        getModel().setAlien(color, cabin, player);
    }

    @Override
    public void readyToFly(Player player) {
        if (!validShips.get(player)) {
            throw new IllegalArgumentException("Cannot fly with invalid ship");
        }
        readyToFly.put(player, true);
    }

    @Override
    public void initAllShips() {
        if (!allShipsReadyToFly()) {
            throw new IllegalArgumentException("Cannot initialize ships: some ships are not ready to fly");
        }
        for (Player player : getModel().getInGamePlayers()) {
            getModel().addPieces(player);
        }
    }
}
