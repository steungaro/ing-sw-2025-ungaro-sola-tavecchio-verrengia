package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
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
 * if any player does not accept the card, or if the card has been played, a new card is drawn
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
        setStandbyMessage("Waiting for " + getCurrentPlayer() + "'s action.");
        //notify the first player that it's his turn with an acceptPhaseMessage and send the other players a standby message
        this.phase = StatePhase.ACCEPT_PHASE;
        getController().getMessageManager().notifyPhaseChange(phase, this);
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
     * @throws InvalidStateException if the player doesn't have enough crew
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void acceptCard(Player player) throws InvalidTurnException, InvalidStateException {
        //check if the player is the current player
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player has enough crew to accept the card
        if(player.getShip().crew() < crewNeeded){
            throw new InvalidStateException("You don't have enough crew to land on the station, you need at least " + crewNeeded + " crew members.");
        }
        //set the phase to the next phase
        phase = StatePhase.ADD_CARGO;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to load cargo.");
        //notify the current player of the new phase
        getController().getMessageManager().notifyPhaseChange(phase, this);
        //mark the card as played
        getController().getActiveCard().playCard();
    }

    /**
     * This method is used to load a cargo from the reward to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws IllegalStateException if the station was not boarded, and if the cargo is not in the reward
     * @throws CargoException if the cargo cannot be loaded,
     * @throws InvalidTurnException if it's not the player's turn
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws InvalidStateException, CargoException, InvalidTurnException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        //check if we are in the correct phase
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("Cannot load cargo in this state.");
        }
        //check if the player is the current player
        if(!player.getUsername().equals(getCurrentPlayer())){
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the cargo the player is trying to load is in the reward
        if(!reward.contains(loaded)){
            throw new CargoNotLoadable("You can't load this cargo, it's not in the reward.");
        }
        reward.remove(loaded);
        super.loadCargo(player, loaded, chTo);
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidCargoException if the cargo is not in the player's cargo hold
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, InvalidCargoException, ComponentNotFoundException{
        //check if we are in the correct phase
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't unload cargo in this state.");
        }
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        super.unloadCargo(player, unloaded, ch);
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is used to move a cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param loaded the color of the cargo to be moved
     * @param chFrom the cargo hold from which the cargo is moved
     * @param chTo the cargo hold to which the cargo is moved
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidCargoException if the cargo is not in the cargo hold from which the player is moving it
     * @throws CargoNotLoadable if the cargo cannot be loaded
     * @throws CargoFullException if the cargo hold is full
     *
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws InvalidStateException, InvalidTurnException, InvalidCargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        //check if we are in the correct phase
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't move cargo in this state.");
        }
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        super.moveCargo(player, loaded, chFrom, chTo);
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method is used to end the move of a player
     * If the card has been played or if nobody has accepted the card, a new card is drawn
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the card has been played
        if (getController().getActiveCard().isPlayed()) {
            //if the card has been played, the player who played it loses days
            getModel().movePlayer(player, -lostDays);
            //notify all the player of the player update
            PlayerUpdateMessage pum = new PlayerUpdateMessage(player.getUsername(), 0, true, player.getColor(), (player.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces());
            getController().getMessageManager().broadcastUpdate(pum);
            //change the phase to standby phase
            phase = StatePhase.DRAW_CARD_PHASE;
            //notify all the players that the card has been played and the next card will be drawn
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            //draw a new card
            getController().setState(new PreDrawState(getController()));
        } else {
            abandonedEndMove();
        }
    }

    /**
     * This method is called when a player quits the game
     * @param player who quits the game
     */
    @Override
    public void currentQuit(Player player) {
        try {
            endMove(player);
        } catch (InvalidTurnException e) {
            //cannot happen
        }
    }

    /**
     * This method is used to get the cargo reward
     * @return the cargo reward
     */
    @Override
    public List<CargoColor> cargoReward() {
        return reward;
    }
}
