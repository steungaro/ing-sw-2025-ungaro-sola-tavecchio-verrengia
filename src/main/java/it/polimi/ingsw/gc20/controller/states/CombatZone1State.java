package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.exceptions.CargoException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

public class CombatZone1State extends CargoState {
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
    public CombatZone1State(GameController controller, GameModel model, AdventureCard card) {
        super(controller, model);
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
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredFirepower.put(player, getModel().FirePower(player, new HashSet<>(cannons), batteries));
        if (declaredFirepower.size() == getController().getInGameConnectedPlayers().size()) {
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
    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEnginePower.put(player, getModel().EnginePower(player, engines.size(), batteries));
        if (declaredEnginePower.size() == getController().getInGameConnectedPlayers().size()) {
            Player p = declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey();
            removingCargo = true;
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
        } while (manager.isFirstHeavyFire());
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().drawCard();
        }
    }

    @Override
    public void moveCargo(Player player, CargoColor loaded, CargoHold chFrom, CargoHold chTo) throws IllegalStateException, InvalidTurnException, CargoException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        super.moveCargo(player, loaded, chFrom, chTo);
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        getModel().MoveCargo(player, unloaded, ch, null);
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
    public void loseEnergy(Player player, Battery battery) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!removingCargo) {
            throw new IllegalStateException("Not in removing cargo state");
        }
        if (player.getShip().getCargo().values().stream().mapToInt(v -> v).sum() != 0) {
            throw new IllegalStateException("Cannot lose energy if having cargo available");
        }
        List<Battery> batteries = new ArrayList<>();
        batteries.add(battery);
        getModel().removeEnergy(player, batteries);
        lostCargo--;
        if (player.getShip().getTotalEnergy() == 0) {
            lostCargo = 0;
        }
    }
}
