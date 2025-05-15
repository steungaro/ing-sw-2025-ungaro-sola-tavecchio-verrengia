package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.ActivatedPowerMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.UpdateShipMessage;
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
import it.polimi.ingsw.gc20.server.network.NetworkService;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class    CombatZone1State extends CargoState {
    private final int lostDays;
    private int lostCargo;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private boolean removingCargo;
    private FireManager manager;
    //enumeration that represents the state of the game
    private StatePhase phase;
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
        this.phase = StatePhase.CANNONS_PHASE;
    }

    @Override
    public void automaticAction() {
        Player player = getController().getInGameConnectedPlayers().stream()
                .map(p -> getController().getPlayerByID(p))
                .min(Comparator.comparingInt(p -> p.getShip().crew()))
                .orElseThrow(() -> new RuntimeException("Error"));
        manager = new FireManager(getModel(), cannonFires, player);
        setCurrentPlayer(player.getUsername());
        phase = StatePhase.ROLL_DICE_PHASE;
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if(phase != StatePhase.CANNONS_PHASE) {
            throw new IllegalStateException("Not in cannon phase");
        }
        Set<Cannon> cannonsComponents = new HashSet<>();
        List<Battery> batteriesComponents = new ArrayList<>();
        if((Set<Cannon>) Translator.getComponentAt(player, cannons, Cannon.class)!=null)
            cannonsComponents.addAll((Set<Cannon>) Translator.getComponentAt(player, cannons, Cannon.class));
        if((List<Battery>) Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll((List<Battery>) Translator.getComponentAt(player, batteries, Battery.class));
        float firepower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        declaredFirepower.put(player, firepower);

        for (Player p : getModel().getInGamePlayers()) {
            NetworkService.getInstance().sendToClient(p.getUsername(), new ActivatedPowerMessage(player.getUsername(), firepower, "firepower"));
            NetworkService.getInstance().sendToClient(p.getUsername(), new UpdateShipMessage(player.getUsername(), player.getShip(), "used energies", null));
        }
        // TODO endMoveMessage
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
            phase = StatePhase.ENGINES_PHASE;
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
        if(!phase.equals(StatePhase.ENGINES_PHASE)) {
            throw new IllegalStateException("Not in engine phase");
        }

        List<Battery> batteriesComponents = new ArrayList<>();
        if(Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        declaredEnginePower.put(player, getModel().EnginePower(player, engines.size(), batteriesComponents));
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
            phase = StatePhase.REMOVE_CARGO;
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
        switch (manager.getFirstProjectile()) {
            case LIGHT_FIRE, LIGHT_METEOR:
                phase = StatePhase.SELECT_SHIELD;
                break;
            case HEAVY_METEOR:
                phase = StatePhase.CANNONS_PHASE;
                break;
            case HEAVY_FIRE:
                phase = StatePhase.AUTOMATIC_ACTION;
                try {
                    manager.fire();
                } catch (InvalidShipException e) {
                    phase = StatePhase.VALIDATE_SHIP_PHASE;
                }
                break;
            case null:
                getModel().getActiveCard().playCard();
                phase = StatePhase.STANDBY_PHASE;
                getController().setState(new PreDrawState(getController()));
                break;
        }
        return getModel().getGame().rollDice();
    }



    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if(!phase.equals(StatePhase.SELECT_SHIELD)) {
            throw new IllegalStateException("Not in shield phase");
        }
        manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        try {
            manager.fire();
            phase = StatePhase.ROLL_DICE_PHASE;
        } catch (InvalidShipException e) {
            phase = StatePhase.VALIDATE_SHIP_PHASE;
        }
    }

    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws IllegalStateException, InvalidTurnException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        throw new InvalidTurnException("cannot move cargo when you lose cargo");
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
        Map<CargoColor, Integer> cargo = player.getShip().getCargo();
        boolean allZero = true;
        for (Integer count: cargo.values()){
            if (count > 0) {
                allZero = false;
                break;
            }
        }
        //if all cargo is zero and there are still cargo to remove go to remove battery phase
        lostCargo--;
        if (allZero && lostCargo > 0) {
            phase = StatePhase.BATTERY_PHASE;
        }
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
            phase = StatePhase.AUTOMATIC_ACTION;
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
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new IllegalStateException("Not in battery phase");
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
        if(!phase.equals(StatePhase.VALIDATE_SHIP_PHASE)) {
            throw new IllegalStateException("Not in branch phase");
        }
        manager.chooseBranch(player, coordinates);
        if (manager.finished()) {
            phase = StatePhase.STANDBY_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            phase = StatePhase.ROLL_DICE_PHASE;
        }
    }

    public void currentQuit(Player player){
        if(phase.equals(StatePhase.VALIDATE_SHIP_PHASE)) {
            try {
                chooseBranch(player, new Pair<>(-1, -1));
                phase = StatePhase.STANDBY_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            catch (InvalidTurnException e) {
                //ignore
            }
        }
        else {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
        }
    }
}
