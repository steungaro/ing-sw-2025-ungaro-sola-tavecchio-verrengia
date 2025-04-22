package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.managers.Translator;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Battery;
import it.polimi.ingsw.gc20.model.components.Cannon;
import it.polimi.ingsw.gc20.model.components.CargoHold;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.List;

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
    }

    @Override
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!defeated) {
            throw new IllegalStateException("Card not defeated");
        }
        accepted = true;
    }

    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!defeated) {
            throw new IllegalStateException("Card not defeated. Cannot lose energy now");
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
        if (!defeated || !accepted) {
            throw new IllegalStateException("Card not defeated or accepted yet");
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
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn");
        }
        if (!defeated) {
            currentLostCargo--;
        }
        getModel().MoveCargo(player, unloaded, Translator.getComponentAt(player, ch, CargoHold.class), null);
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

    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException, EmptyDeckException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        float firePower = getModel().FirePower(player, new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)), Translator.getComponentAt(player, batteries, Battery.class));
        if (firePower > this.firePower) {
            getController().getActiveCard().playCard();
            defeated = true;
            currentLostCargo = 0;
            return 1;
        } else if (firePower == this.firePower) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().getActiveCard().playCard();
                getController().drawCard();
            }
            currentLostCargo = 0;
            return 0;
        } else {
            currentLostCargo = lostCargo;
            return -1;
        }
    }

    @Override
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException, EmptyDeckException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (defeated) {
            getModel().movePlayer(player, -lostDays);
            getModel().getActiveCard().playCard();
            getController().drawCard();
        } else {
            if (currentLostCargo > 0) {
                throw new IllegalStateException("Lose all your cargo before ending move");
            }
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().getActiveCard().playCard();
                getController().drawCard();
            }
        }
    }
}
