package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.EndMoveConfirmMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.UpdateShipMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import org.javatuples.Pair;

import java.util.List;

/**
 * This class represents the state of the game when an Abandoned Station card is played
 * The player can:
 * - accept the card
 *      + load cargo from the reward to the player's cargo hold
 *      + unload cargo from the player's cargo hold
 *      + move cargo from one cargo hold to another
 *      + end the move
 * - discard the card (end the move)
 * if the card is not accepted by any player, or if the card has been played, a new card is drawn
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class AbandonedStationState extends CargoState {
    private final int crewNeeded;
    private final List<CargoColor> reward;
    private final int lostDays;

    /**
     * Default constructor
     */
    public AbandonedStationState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.crewNeeded = card.getCrew();
        this.reward = card.getReward();
        this.lostDays = card.getLostDays();
        this.phase = StatePhase.ACCEPT_PHASE;
    }

    @Override
    public String toString() {
        return "AbandonedStationState{ " +
                "crewNeeded=" + crewNeeded +
                ", reward=" + reward +
                ", lostDays=" + lostDays +
                '}';
    }

    /**
     * This method is used to accept the card and land on the station
     * @param player the player who is landing on the station
     * @throws IllegalStateException if the player doesn't have enough crew
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void acceptCard(Player player) throws InvalidTurnException, IllegalStateException {
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn");
        }
        if(player.getShip().crew() < crewNeeded){
            throw new IllegalStateException("You don't have enough crew to land on the station");
        }
        phase = StatePhase.ADD_CARGO;
        getController().getActiveCard().playCard();
    }

    /**
     * This method is used to load a cargo from the reward to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalStateException if the station was not boarded and if the cargo is not in the reward
     * @throws CargoException if the cargo is not in the reward
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws IllegalArgumentException, CargoException, InvalidTurnException, CargoNotLoadable, CargoFullException {
        if(getController().getActiveCard().isPlayed()){
            throw new IllegalStateException("You can't load cargo unless you are on the station");
        }
        if(!reward.contains(loaded)){
            throw new CargoException("You can't load this cargo, it's not in the reward");
        }
        reward.remove(loaded);
        super.loadCargo(player, loaded, chTo);
        for (Player p: getModel().getGame().getPlayers()) {
            NetworkService.getInstance().sendToClient(p.getUsername(), new UpdateShipMessage(getCurrentPlayer(), p.getShip(), "lost crew", null));
        }
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws IllegalArgumentException if the station was not boarded
     * @throws CargoException if the cargo is not in the player's cargo hold
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws IllegalArgumentException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if(getController().getActiveCard().isPlayed()){
            throw new IllegalStateException("You can't unload cargo unless you are on the station");
        }
        super.unloadCargo(player, unloaded, ch);
        for (Player p: getModel().getGame().getPlayers()) {
            NetworkService.getInstance().sendToClient(p.getUsername(), new UpdateShipMessage(getCurrentPlayer(), p.getShip(), "lost crew", null));
        }
    }

    /**
     * This method is used to move a cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param loaded the color of the cargo to be moved
     * @param chFrom the cargo hold from which the cargo is moved
     * @param chTo the cargo hold to which the cargo is moved
     * @throws IllegalArgumentException if the station was not boarded
     * @throws CargoException if the cargo is not in the player's cargo hold
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws IllegalArgumentException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if(getController().getActiveCard().isPlayed()){
            throw new IllegalStateException("You can't move cargo unless you are on the station");
        }
        super.moveCargo(player, loaded, chFrom, chTo);
        for (Player p: getModel().getGame().getPlayers()) {
            NetworkService.getInstance().sendToClient(p.getUsername(), new UpdateShipMessage(getCurrentPlayer(), p.getShip(), "lost crew", null));
        }
    }

    /**
     * This method is used to end the move of a player
     * If the card has been played or if nobody has accepted the card, a new card is drawn
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException{
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (getController().getActiveCard().isPlayed()) {
            getModel().movePlayer(player, -lostDays);
            for (Player p: getModel().getGame().getPlayers()) {;
                NetworkService.getInstance().sendToClient(p.getUsername(), new PlayerUpdateMessage(getCurrentPlayer(), 0, p.isInGame(), p.getColor(), p.getPosition()%getModel().getGame().getBoard().getSpaces()));
            }
            phase = StatePhase.STANDBY_PHASE;
            getController().setState(new PreDrawState(getController()));

        } else {
            String currentPlayer = getCurrentPlayer();
            nextPlayer();
            for (Player p: getModel().getGame().getPlayers()) {
                NetworkService.getInstance().sendToClient(p.getUsername(), new EndMoveConfirmMessage(currentPlayer, getCurrentPlayer()));
            }
            if (getCurrentPlayer() == null) {
                phase = StatePhase.STANDBY_PHASE;
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.ACCEPT_PHASE;
            }
        }
    }

    @Override
    public void currentQuit(Player player) throws InvalidTurnException {
        if (player.getUsername().equals(getCurrentPlayer())) {
            endMove(player);
        }
        if (getCurrentPlayer() == null) {
            getController().setState(new PreDrawState(getController()));
        }
    }
}
