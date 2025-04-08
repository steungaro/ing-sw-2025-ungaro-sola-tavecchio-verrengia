package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.FireManager;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

public class CombatZone0State extends PlayingState {
    private final int lostDays;
    private int lostCrew;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private boolean removingCrew;
    private FireManager manager;
    /**
     * Default constructor
     */
    public CombatZone0State(GameController gc, GameModel gm, int lostDays, int lostCrew, List<Projectile> cannonFires) {
        super(gm, gc);
        this.lostDays = lostDays;
        this.lostCrew = lostCrew;
        this.cannonFires = cannonFires;
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : getModel().getInGamePlayers()) {
            declaredFirepower.put(player, 0f);
        }
        this.removingCrew = false;
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
        getModel().movePlayer(getController().getInGameConnectedPlayers().stream()
                        .map(p -> getController().getPlayerByID(p))
                        .min(Comparator.comparingInt(p -> p.getShip().crew()))
                        .orElseThrow(() -> new RuntimeException("Error"))
        , -lostDays);
    }

    @Override
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredFirepower.put(player, getModel().FirePower(player, new HashSet<>(cannons), batteries));
        if (declaredFirepower.size() == getController().getInGameConnectedPlayers().size()) {
            removingCrew = true;
            super.setCurrentPlayer(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey()
                    .getUsername());
        }
    }

    @Override
    public void loseCrew(Player player, List<Cabin> cabins) throws InvalidTurnException {
        if (!removingCrew) {
            throw new IllegalStateException("You cannot remove crew now");
        }
        if (player.getUsername().equals(getCurrentPlayer())) {
            getModel().loseCrew(player, cabins);
            if (player.getShip().crew() == 0) {
                getController().defeated(player.getUsername());
            } else if (cabins.size() != lostCrew) {
                lostCrew -= cabins.size();
            } else if (lostCrew == 0) {
                removingCrew = false;
            }
            setCurrentPlayer(getController().getFirstOnlinePlayer());
        } else {
            throw new InvalidTurnException("It's not your turn");
        }
    }

    @Override
    public String toString() {
        return "CombatZone0State{" +
                "lostDays=" + lostDays +
                ", lostCrew=" + lostCrew +
                ", cannonFires=" + cannonFires +
                ", declaredFirepower=" + declaredFirepower +
                ", declaredEnginePower=" + declaredEnginePower +
                ", removingCrew=" + removingCrew +
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
            manager = new FireManager(getModel(), cannonFires, p);
            setCurrentPlayer(p.getUsername());
            while (manager.isFirstHeavyFire()) {
                manager.fire();
            }
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
            getController().drawCard();
        }
    }
}
