package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.CargoHold;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class CombatZone1State extends CargoState {
    private final int lostDays;
    private int lostCargo;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private boolean removingCargo;
    private FireManager manager;
    //enumeration that represents the state of the game
    private enum phase {
        CANNON,
        ENGINE,
        FIRE,
        CARGO
    }
    private phase currentPhase;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public CombatZone1State(GameModel model, GameController controller, AdventureCard card) {
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
        currentPhase = phase.CANNON;
    }

    @Override
    public void automaticAction() {
        Player player = getController().getInGameConnectedPlayers().stream()
                .map(p -> getController().getPlayerByID(p))
                .min(Comparator.comparingInt(p -> p.getShip().crew()))
                .orElseThrow(() -> new RuntimeException("Error"));
        manager = new FireManager(getModel(), cannonFires, player);
        setCurrentPlayer(player.getUsername());
        currentPhase = phase.FIRE;
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if(!currentPhase.equals(phase.CANNON)) {
            throw new IllegalStateException("Not in cannon phase");
        }
        declaredFirepower.put(player, getModel().FirePower(player, new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)), Translator.getComponentAt(player, batteries, Battery.class)));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //remove from declaredFirePower the players that are not in getInGameConnectedPlayers
            declaredFirepower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p.getUsername()));

            //get the player with the minimum declaredFirePower and make hit lose flight days
            getModel().movePlayer(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey()
                    , - lostDays);
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            currentPhase = phase.ENGINE;
        }
    }

        @Override
    public String toString() {
        return "CombatZone1State{" +
                "lostDays=" + lostDays +
                ", lostCargo=" + lostCargo +
                ", cannonFires=" + cannonFires +
                ", declaredFirepower=" + declaredFirepower +
                ", declaredEnginePower=" + declaredEnginePower +
                ", removingCargo=" + removingCargo +
                '}';
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidEngineException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if(!currentPhase.equals(phase.ENGINE)) {
            throw new IllegalStateException("Not in engine phase");
        }
        declaredEnginePower.put(player, getModel().EnginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class)));
        nextPlayer();
        if (getCurrentPlayer() == null) {
            declaredEnginePower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p.getUsername()));

            setCurrentPlayer(declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey()
                    .getUsername());
            removingCargo = true;
            currentPhase = phase.CARGO;
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
        if (manager.isFirstHeavyFire()) {
            manager.fire();
        }
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
        return getModel().getGame().rollDice();
    }



    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if(!currentPhase.equals(phase.FIRE)) {
            throw new IllegalStateException("Not being shot at");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        manager.fire();
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
    }

    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws IllegalStateException, InvalidTurnException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        getModel().MoveCargo(player, loaded, Translator.getComponentAt(player, chFrom, CargoHold.class), Translator.getComponentAt(player, chTo, CargoHold.class));
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoNotLoadable, CargoFullException, InvalidCargoException {
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
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, EnergyException {
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
        if(!currentPhase.equals(phase.FIRE)) {
            throw new IllegalStateException("Not in branch phase");
        }
        manager.chooseBranch(player, coordinates);
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
    }

    public void currQuit(Player player){
        if(currentPhase.equals(phase.FIRE)) {
            try {
                chooseBranch(player, new Pair<>(-1, -1));
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            catch (InvalidTurnException e) {
                //ignore
            }
        }
        else {
            nextPlayer();
        }
    }
}
