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
     * Constructs an AbandonedStationState object. This initializes the state with the provided
     * game model, controller, and adventure card, setting the required crew, reward cargo,
     * and lost days. It also sets the standby message for the current player and notifies
     * the message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
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

    @Override
    public void currentQuit(Player player) {
        try {
            endMove(player);
        } catch (InvalidTurnException e) {
            //cannot happen
        }
    }

    @Override
    public List<CargoColor> cargoReward() {
        return reward;
    }
}
