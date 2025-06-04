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
import it.polimi.ingsw.gc20.server.network.NetworkService;
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
        for (String username: getController().getInGameConnectedPlayers()) {
            if (username.equals(getCurrentPlayer())) {
                //send the player the cannon fire
                NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
            } else {
                //send the player a standby message
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
            }
        }
        phase = StatePhase.CANNONS_PHASE;
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated");
        }
        for (String username: getController().getInGameConnectedPlayers()) {
            if (username.equals(getCurrentPlayer())) {
                NetworkService.getInstance().sendToClient(username, new AddCargoMessage(reward));
            } else {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage( player.getUsername() + " accepted the card"));
            }
        }
        accepted = true;
        phase = StatePhase.ADD_CARGO;
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("You are not in the battery phase");
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new IllegalStateException("You are not in the add cargo phase");
        }
        if (!reward.contains(loaded)) {
            throw new CargoException("Not in the card reward");
        }
        super.loadCargo(player, loaded, chTo);
        reward.remove(loaded);
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.REMOVE_CARGO && phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You are not in the cargo managing phase");
        }
        super.unloadCargo(player, unloaded, ch);
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

    /**
     * This method is used to shoot the enemy
     * @param player the player who is shooting
     * @param cannons the cannons selected by the player
     * @param batteries the batteries selected by the player
     * @return 1 if the player wins, 0 if it's a tie, -1 if the player loses
     * @throws InvalidTurnException if it's not the player's turn
     * @throws IllegalStateException if the game is not in the cannon phase
     * @throws InvalidCannonException if the cannon is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
    public void activateCannons (Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You are not in the cannons phase");
        }
        //translate the cannons and batteries to the actual components
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));

        //calculate the firepower
        float firePower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        if (firePower > this.firePower) {
            for (String username: getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new AcceptPhaseMessage("Smugglers defeated, do you want to accept the card?"));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage(player.getUsername() + " defeated the smugglers"));
                }
            }
            //won, the player can accept the card
            getController().getActiveCard().playCard();
            phase = StatePhase.ACCEPT_PHASE;
            defeated = true;
            currentLostCargo = 0;
        } else if (firePower == this.firePower) {
            //draw, go to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw a new card
                for (String username: getController().getInGameConnectedPlayers()) {
                    NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
                }
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                for (String username: getController().getInGameConnectedPlayers()) {
                    if (username.equals(getCurrentPlayer())) {
                        //send the player the cannon fire
                        NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                    } else {
                        //send the player a standby message
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                    }
                }
            }
            currentLostCargo = 0;
        } else {
            for (String username: getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new RemoveCargoMessage(lostCargo));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage(player.getUsername() + " lost the fight"));
                }
            }
            //lost, the player has to lose cargo
            phase = StatePhase.REMOVE_CARGO;
            currentLostCargo = lostCargo;
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
            for (String username: getController().getInGameConnectedPlayers()) {
                NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
            }
            phase = StatePhase.DRAW_CARD_PHASE;
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            if (currentLostCargo > 0) {
                throw new InvalidStateException("Lose all your cargo before ending move");
            }
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw a new card
                for (String username: getController().getInGameConnectedPlayers()) {
                    NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
                }
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                for (String username: getController().getInGameConnectedPlayers()) {
                    if (username.equals(getCurrentPlayer())) {
                        //send the player the cannon fire
                        NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                    } else {
                        //send the player a standby message
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                    }
                }
                phase = StatePhase.CANNONS_PHASE;
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
                for (String username: getController().getInGameConnectedPlayers()) {
                    NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
                }
                phase = StatePhase.DRAW_CARD_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                for (String username: getController().getInGameConnectedPlayers()) {
                    if (username.equals(getCurrentPlayer())) {
                        //send the player the cannon fire
                        NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                    } else {
                        //send the player a standby message
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                    }
                }
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
        return "You are fighting smugglers, enemy firepower is " + firePower + ", select the cannons and batteries to use";
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
