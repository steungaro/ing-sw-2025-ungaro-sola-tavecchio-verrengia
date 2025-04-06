package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cabin;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.components.Engine;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.*;

public class CombatZone0State extends State {
    private final int lostDays;
    private final int lostCrew;
    private final List<Projectile> cannonFires;
    private final Map<Player, Float> declaredFirepower;
    private final Map<Player, Integer> declaredEnginePower;
    private boolean removingCrew;
    /**
     * Default constructor
     */
    public CombatZone0State(int lostDays, int lostCrew, List<Projectile> cannonFires) {
        super();
        this.lostDays = lostDays;
        this.lostCrew = lostCrew;
        this.cannonFires = cannonFires;
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : model.getInGamePlayers()) {
            declaredFirepower.put(player, 0f);
        }
        this.removingCrew = false;
    }

    @Override
    public void automaticAction() {
        // TODO: model function to move the player
//        model.move (model.getInGamePlayers().stream()
//                .min((p1, p2) -> Integer.compare(p1.getShip().crew(), p2.getShip().crew()))
//                .get()
//        ), -lostDays);
    }

    @Override
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        declaredFirepower.put(player, model.FirePower(player, new HashSet<Cannon>(cannons), batteries));
        if (declaredFirepower.size() == controller.getOnlinePlayers()) {
            removingCrew = true;
            controller.setCurrentPlayer(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .get()
                    .getKey()
                    .getUsername());
        }
    }

    @Override
    public void loseCrew(Player player, List<Cabin> cabins) throws InvalidTurnException {
        if (!removingCrew) {
            throw new IllegalStateException("You cannot remove crew now");
        }
        if (player.getUsername().equals(controller.getCurrentPlayer())) {
            //TODO: model new methods
            // model.loseCrew(player, cabins);
            removingCrew = false;
            controller.setCurrentPlayer(controller.getFirstOnlinePlayer());
        } else {
            throw new InvalidTurnException("It's not your turn");
        }
    }

    @Override
    public String toString() {
        return "CombatZone0State";
    }

    @Override
    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        declaredEnginePower.put(player, model.EnginePower(player, engines.size(), batteries));
        if (declaredEnginePower.size() == controller.getOnlinePlayers()) {
            controller.setCurrentPlayer(declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .get()
                    .getKey()
                    .getUsername());
            // TODO: fire at people. maybe firemanager?
        }
    }
}
