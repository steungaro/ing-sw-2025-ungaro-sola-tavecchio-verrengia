package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
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
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is used to accept the card
     * @param player the player who is accepting the card
     * @throws InvalidStateException if the game is not in the accept phase
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated yet.");
        }
        phase = StatePhase.ADD_CARGO;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to load cargo.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
        accepted = true;
    }

    /**
     * This method is used to lose energy
     * @param player the player who is losing energy
     * @param battery the battery from which the energy is lost
     * @throws InvalidStateException if the game is not in the battery phase
     * @throws InvalidTurnException if it's not the player's turn
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("You are not in the battery phase.");
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
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the add cargo phase
     * @throws CargoException if the cargo is not in the card reward list
     * @throws CargoNotLoadable if the cargo is not loadable
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new IllegalStateException("You are not in the add cargo phase.");
        }
        if (!reward.contains(loaded)) {
            throw new CargoException("Cannot load this cargo, it's not in the card reward.");
        }
        super.loadCargo(player, loaded, chTo);
        reward.remove(loaded);
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the remove cargo phase
     * @throws InvalidCargoException if the cargo is not valid
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, InvalidCargoException, ComponentNotFoundException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.REMOVE_CARGO && phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You are not in the cargo managing phase.");
        }

        super.unloadCargo(player, unloaded, ch);

        currentLostCargo--;

        //check if the player has no more cargo
        Map<CargoColor, Integer> cargo = player.getShip().getCargo();
        boolean allZero = true;
        for (Integer count: cargo.values()){
            if (count > 0) {
                allZero = false;
                break;
            }
        }
        //if all cargo is zero and there are still cargos to remove, go to remove battery phase
        if (allZero && currentLostCargo > 0 && phase == StatePhase.REMOVE_CARGO) {
            phase = StatePhase.BATTERY_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to choose a battery to lose energy from.");
        }
        getController().getMessageManager().notifyPhaseChange(phase, this);
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

    /**
     * This method is used to shoot the enemy
     * @param player the player who is shooting
     * @param cannons the cannons selected by the player
     * @param batteries the batteries selected by the player
     * @throws InvalidTurnException if it's not the player's turn
     * @throws IllegalStateException if the game is not in the cannon phase
     * @throws InvalidCannonException if the cannon is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You are not in the cannons phase.");
        }
        //translate the cannons and batteries to the actual components
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        currentLostCargo = 0;
        //calculate the firepower
        float firePower = getModel().firePower(player, cannonsComponents, batteriesComponents);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated cannons"));
        if (firePower > this.firePower) {
            phase = StatePhase.ACCEPT_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to accept or refuse the card.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
            getController().getActiveCard().playCard();
            defeated = true;
        } else if (firePower == this.firePower) {
            //draw, go to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else {
            currentLostCargo = lostCargo;
            //check if the player has no more cargo
            Map<CargoColor, Integer> cargo = player.getShip().getCargo();
            boolean allZero = true;
            for (Integer count: cargo.values()){
                if (count > 0) {
                    allZero = false;
                    break;
                }
            }

            if (allZero) {
                phase = StatePhase.BATTERY_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to choose a battery to lose energy from.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            } else {
                phase = StatePhase.REMOVE_CARGO;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to unload cargo.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        }
    }

    /**
     * This method is used to end the move
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the correct phase
     */
    @Override
    public void endMove(Player player) throws InvalidStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (defeated) {
            //move the player and draw a new card
            if (accepted) {
                getModel().movePlayer(player, -lostDays);
            }
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            if (currentLostCargo > 0) {
                throw new InvalidStateException("Lose all the cargo before ending move. You still have " + currentLostCargo + " cargo to lose.");
            }
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw a new card
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        }
    }

    @Override
    public void currentQuit(Player player) throws InvalidTurnException {
        //if the player is in the cannon phase, if he quit, we go to the next player
        if (phase == StatePhase.CANNONS_PHASE || phase == StatePhase.REMOVE_CARGO || phase == StatePhase.BATTERY_PHASE) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw new card
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else if (phase == StatePhase.ACCEPT_PHASE || phase == StatePhase.ADD_CARGO) {
            try {
                endMove(player);
            } catch (InvalidStateException | InvalidTurnException _) {
                //ignore
            }
        }
    }
    @Override
    public String createsCannonsMessage(){
        return "You are fighting smugglers, enemy firepower is " + firePower + ", select the cannons to activate.";
    }

    @Override
    public List<CargoColor> cargoReward() {
        return reward;
    }

    @Override
    public int cargoToRemove() {
        return currentLostCargo;
    }
}
