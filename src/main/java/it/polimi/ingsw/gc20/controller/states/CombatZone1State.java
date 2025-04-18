package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.CargoException;
import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.components.Shield;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class CombatZone1State extends PlayingState {
    private final int lostDays;
    private int lostCargo;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private boolean removingCargo;
    private FireManager manager;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public CombatZone1State(GameController controller, GameModel model, AdventureCard card) {
        super(model, controller);
        this.lostDays = card.getLostDays();
        this.lostCargo = card.getLostCargo();
        this.cannonFires = card.getProjectiles();
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : getModel().getInGamePlayers()) {
            declaredFirepower.put(player, 0f);
        }
        this.removingCargo = false;

        this.manager = null;
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.automaticAction();
    }

    @Override
    public void automaticAction() {
        Player player = getController().getInGameConnectedPlayers().stream()
                        .map(p -> getController().getPlayerByID(p))
                        .min(Comparator.comparingInt(p -> p.getShip().crew()))
                        .orElseThrow(() -> new RuntimeException("Error"));
        manager = new FireManager(getModel(), cannonFires, player);
        setCurrentPlayer(player.getUsername());
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredFirepower.put(player, getModel().FirePower(player, new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)), Translator.getComponentAt(player, batteries, Battery.class)));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            getModel().movePlayer(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey()
                    , - lostDays);
            setCurrentPlayer(getController().getFirstOnlinePlayer());
        }
    }

        @Override
    public String toString() {
        return "CombatZone0State{" +
                "lostDays=" + lostDays +
                ", lostCargo=" + lostCargo +
                ", cannonFires=" + cannonFires +
                ", declaredFirepower=" + declaredFirepower +
                ", declaredEnginePower=" + declaredEnginePower +
                ", removingCargo=" + removingCargo +
                '}';
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEnginePower.put(player, getModel().EnginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class)));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            setCurrentPlayer(declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey()
                    .getUsername());
            removingCargo = true;
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
            getModel().getActiveCard().playCard();
            getController().drawCard();
        }
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        manager.fire();
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().drawCard();
        }
    }

    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        getModel().MoveCargo(player, loaded, Translator.getComponentAt(player, chFrom, CargoHold.class), Translator.getComponentAt(player, chTo, CargoHold.class));
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        getModel().MoveCargo(player, unloaded, Translator.getComponentAt(player, ch, CargoHold.class), null);
        lostCargo--;
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        if (lostCargo == 0) {
            removingCargo = false;
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            automaticAction();
        }
    }

    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        super.loseEnergy(player, battery);
        lostCargo--;
        if (player.getShip().getTotalEnergy() == 0) {
            lostCargo = 0;
        }
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.chooseBranch(player, coordinates);
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().drawCard();
        }
    }
}
