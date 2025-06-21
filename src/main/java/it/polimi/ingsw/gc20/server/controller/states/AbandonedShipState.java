package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.List;

/**
 * This class represents the state of the game when an AbandonedShip card has been drawn.
 * The player can:
 * - lose crew (accepting card) and gain the rewards (credits and lost days)
 * - end the move (discard card)
 *    + if all players have ended their moves without accepting the card, a new card is drawn
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class AbandonedShipState extends PlayingState {
    private final int lostCrew;
    private final int credits;
    private final int lostDays;

    /**
     * Constructs an AbandonedShipState object. This initializes the state with the provided
     * game model, controller, and adventure card, setting the initial phase to ACCEPT_PHASE.
     * It also configures the standby message for the current player and notifies the
     * message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public AbandonedShipState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostCrew = card.getCrew();
        this.credits = card.getCredits();
        this.lostDays = card.getLostDays();

        this.phase = StatePhase.ACCEPT_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is choosing to accept or refuse the Abandoned Ship card. ");
        // notify the players connected to the game to update the player data
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player has enough crew to accept the card
        if (player.getShip().crew() < lostCrew) {
            throw new InvalidStateException("You don't have enough crew to accept the card, you need at least " + lostCrew + " crew members.");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);

        PlayerUpdateMessage message = new PlayerUpdateMessage(player.getUsername(),
                credits,
                true,
                player.getColor(),
                (player.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces());
        //notify all the players of the update of the player
        getController().getMessageManager().broadcastUpdate(message);

        // others will be in the standby phase communicated via a message
        phase = StatePhase.LOSE_CREW_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is choosing the cabins to lose crew from.");
        //notify the current player about the change of phase with a LoseCrewMessage
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, InvalidStateException, EmptyCabinException, ComponentNotFoundException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the correct phase
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("Player not in the lose crew phase.");
        }
        //remove the crew from the ship of the player
        if (cabins.size() < lostCrew) {
            throw new InvalidStateException("Invalid number of cabins selected, you need to select at least " + lostCrew + " cabins.");
        }
        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "lost crew"));

        //mark the card as player and go to the standby phase
        getController().getActiveCard().playCard();
        phase = StatePhase.DRAW_CARD_PHASE;
        //notify all the players that the card has been played and the next card will be drawn
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());

        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void endMove(Player player) throws InvalidTurnException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        abandonedEndMove();
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
    public int getCrew(){
        return lostCrew;
    }
}
