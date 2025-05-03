package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class CombatZone0State extends PlayingState {
    private final int lostDays;
    private int lostCrew;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private FireManager manager;
    //enumeration that represents the state of the game
    private enum phase {
        CANNON,
        CREW,
        ENGINE,
        SHIELD,
        FIRE,
        BRANCH
    }
    private phase currentPhase;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public CombatZone0State(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostDays = card.getLostDays();
        this.lostCrew = card.getCrew();
        this.cannonFires = card.getProjectiles();
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : getModel().getInGamePlayers()) {
            declaredFirepower.put(player, 0f);
        }
        currentPhase = phase.ENGINE;
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
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        if(currentPhase != phase.CANNON) {
            throw new IllegalStateException("You cannot activate cannons now");
        }
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        Set<Cannon> cannonsComponents = new HashSet<>();
        if((Set<Cannon>) Translator.getComponentAt(player, cannons, Cannon.class)!=null)
            cannonsComponents.addAll((Set<Cannon>) Translator.getComponentAt(player, cannons, Cannon.class));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        declaredFirepower.put(player, getModel().FirePower(player, cannonsComponents, batteriesComponents));
        if (declaredFirepower.size() == getController().getInGameConnectedPlayers().size()) {
            Player p = declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey();
            currentPhase = phase.FIRE;
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
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, EmptyCabinException {
        if (currentPhase != phase.CREW) {
            throw new IllegalStateException("You cannot remove crew now");
        }
        if (player.getUsername().equals(getCurrentPlayer())) {
            getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
            if (player.getShip().crew() == 0) {
                lostCrew = 0;
                currentPhase = phase.CANNON;
            } else if (cabins.size() != lostCrew) {
                lostCrew -= cabins.size();
                currentPhase = phase.CANNON;
            }
            if (lostCrew == 0) {
                currentPhase = phase.CANNON;
                setCurrentPlayer(getController().getFirstOnlinePlayer());
            }
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
                '}';
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DieNotRolledException {
        if(currentPhase != phase.ENGINE) {
            throw new IllegalStateException("You cannot activate engines now");
        }
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        declaredEnginePower.put(player, getModel().EnginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class)));
        if (declaredEnginePower.size() == getController().getInGameConnectedPlayers().size()) {
            Player p = declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey();
            currentPhase = phase.CREW;
            setCurrentPlayer(p.getUsername());
            /*while (manager.isFirstHeavyFire()) {
                manager.fire();
            }*/
        }
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        manager.fire();
        if (manager.finished()) {
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
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
            getController().setState(new PreDrawState(getController()));
        }
    }

    public void currentQuit(Player player) {
        nextPlayer();
    }
}