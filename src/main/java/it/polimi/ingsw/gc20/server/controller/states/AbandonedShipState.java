package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;
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
     * Default constructor
     */
    public AbandonedShipState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostCrew = card.getCrew();
        this.credits = card.getCredits();
        this.lostDays = card.getLostDays();
        //notify the first player that it's his turn with an acceptPhaseMessage and send the other players a standby message
        for (String username: getController().getInGameConnectedPlayers()) {
            if (username.equals(getCurrentPlayer())) {
                NetworkService.getInstance().sendToClient(username, new AcceptPhaseMessage("Do you want to accept the card?"));
            } else {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " turn"));
            }
        }

        this.phase = StatePhase.ACCEPT_PHASE;
    }

    @Override
    public String toString() {
        return "AbandonedShipState{ " +
                "lostCrew=" + lostCrew +
                ", credits=" + credits +
                ", lostDays=" + lostDays +
                '}';
    }

    /**
     * This method is used to accept the card and take the player to the next phase
     * @param player the player who is losing crew
     * @throws InvalidStateException if the player doesn't have enough crew to lose
     * @throws InvalidTurnException if it's not the player's turn
     */
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player has enough crew to accept the card
        if (player.getShip().crew() < lostCrew) {
            throw new InvalidStateException("You don't have enough crew to accept the card");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, credits);
        //notify the players connected to the game to update the player data
        for (Player p : getController().getPlayers()) {
            NetworkService.getInstance().sendToClient(p.getUsername(), new PlayerUpdateMessage(player.getUsername(),
                    credits,
                    true,
                    player.getColor(),
                    (player.getPosition() % getModel().getGame().getBoard().getSpaces())));
        }
        // la phase del current player diventa LOSE_CREW_PHASE,
        // others will be in standby phase communicated via a message
        phase = StatePhase.LOSE_CREW_PHASE;
        //notify the current player about the change of phase with a LoseCrewMessage
        NetworkService.getInstance().sendToClient(player.getUsername(), new LoseCrewMessage(lostCrew));
    }
    /**
     * method to remove the crew from the ship of the player whose turn it is
     * @param player the player who is losing crew
     * @param cabins the cabins to get the crew from
     * @throws InvalidStateException if the player doesn't have enough crew to lose
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, InvalidStateException, EmptyCabinException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the correct phase
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("You are not in the lose crew phase");
        }
        //remove the crew from the ship of the player
        if (cabins.size() < lostCrew) {
            throw new InvalidStateException("You didn't select enough cabins");
        }
        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        //mark the card as player and go to the standby phase
        getController().getActiveCard().playCard();
        //notify all the players that the card has been played and the next card will be drawn
        for (String username: getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
        }
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().setState(new PreDrawState(getController()));
    }

    /**
     * This method is used to end the move and discard the card
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        abandonedEndMove();
    }

    /**
     * This method is called if the current player quits the game
     * @param player player who quits
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
     * this method is called to get the crew that the player needs to lose
     * @return the number of crew that the player needs to lose
     */
    @Override
    public int getCrew(){
        return lostCrew;
    }
}
