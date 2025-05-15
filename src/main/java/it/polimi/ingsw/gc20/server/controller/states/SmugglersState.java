package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.CargoHold;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.*;

/**
 * @author GC20
 * This state manages a Smugglers card. A player, in their turn, will call shootEnemy and can win (1), tie (0), lose (-1):
 * - when winning, a player can accept the card or end the move (after accepting the card the player can load or unload cargo and then end move)
 * - when tieing, a new player is picked
 * - when losing, player has to unload/move cargo until the lost cargo is 0, then call endMove
 * - additional loseEnergy function may be called when a player has no more cargo to lose
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class SmugglersState extends CargoState {
    private final int lostCargo;
    private final int firePower;
    private final int lostDays;
    private final List<CargoColor> reward;
    private boolean defeated;
    private boolean accepted;
    private int currentLostCargo;
    /**
     * Default constructor
     */
    public SmugglersState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostCargo = card.getLostCargo();
        this.firePower = card.getFirePower();
        this.lostDays = card.getLostDays();
        this.reward = card.getReward();
        defeated = false;
        accepted = false;
        phase = StatePhase.CANNONS_PHASE;
    }

    @Override
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new IllegalStateException("Card not defeated");
        }
        accepted = true;
        phase = StatePhase.ADD_CARGO;
    }

    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new IllegalStateException("You are not in the battery phase");
        }
        super.loseEnergy(player, battery);
        currentLostCargo--;
        if (player.getShip().getTotalEnergy() == 0) {
            currentLostCargo = 0;
        }
    }

    /**
     * This method is used to load a cargo from a cargo hold to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalArgumentException if it's not the player's turn
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new IllegalStateException("You are not in the add cargo phase");
        }
        if (!reward.contains(loaded)) {
            throw new CargoException("Not in the card reward");
        }
        getModel().addCargo(player, loaded, Translator.getComponentAt(player, chTo, CargoHold.class));
        reward.remove(loaded);
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws IllegalArgumentException if it's not the player's turn
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn");
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
        currentLostCargo--;
        if (allZero && currentLostCargo > 0 && phase == StatePhase.REMOVE_CARGO) {
            phase = StatePhase.BATTERY_PHASE;
        }
    }

    @Override
    public String toString() {
        return "SmugglersState{ " +
                "lostCargo=" + lostCargo +
                ", firePower=" + firePower +
                ", lostDays=" + lostDays +
                ", reward=" + reward +
                '}';
    }

    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new IllegalStateException("You are not in the cannons phase");
        }
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        float firePower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        if (firePower > this.firePower) {
            getController().getActiveCard().playCard();
            phase = StatePhase.ACCEPT_PHASE;
            defeated = true;
            currentLostCargo = 0;
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            }
            currentLostCargo = 0;
            return 0;
        } else {
            phase = StatePhase.REMOVE_CARGO;
            currentLostCargo = lostCargo;
            return -1;
        }
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (defeated) {
            getModel().movePlayer(player, -lostDays);
            phase = StatePhase.STANDBY_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            if (currentLostCargo > 0) {
                throw new IllegalStateException("Lose all your cargo before ending move");
            }
            nextPlayer();
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
            }
        }
    }

    @Override
    public void currentQuit(Player player) throws InvalidTurnException {
        currentLostCargo = 0;
        endMove(player);
    }
}
