package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.AutomaticActionMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;

/**
 * Represents the EpidemicState in the game, which is a specific playing state where
 * an epidemic occurs, affecting all players' ships. This state handles the automatic
 * action of infecting players and managing the consequences of the epidemic.
 * It is dynamically created during the game and integrates with the game model, controller,
 * and adventure card mechanics.
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class EpidemicState extends PlayingState {

    /**
     * Constructs an EpidemicState object, which represents a state in the game where an epidemic occurs.
     * This constructor initializes the state with the provided game model, controller, and adventure card
     * and sets the phase to AUTOMATIC_ACTION. It also notifies players about the epidemic and performs
     * the automatic action associated with this state.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public EpidemicState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        //notify the players that the epidemic is starting, and an automatic action is going to be performed
        getController().getMessageManager().broadcastUpdate(new AutomaticActionMessage(getAutomaticActionMessage()));
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
    }
    @Override
    public void automaticAction() {
        //apply the epidemic effect to all players in the game
        getModel().getInGamePlayers().stream()
                .filter(p -> getController().getInGameConnectedPlayers().contains(p.getUsername()))
                .forEach(p -> p.getShip().epidemic());
        //notify all the players with all the ship updates
        for (Player p: getController().getModel().getInGamePlayers()) {
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(p.getUsername(), p.getShip(), "epidemic"));
        }

        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //effect ended, draw a new card
        phase = StatePhase.DRAW_CARD_PHASE;
        //notify all the players that the epidemic effect is over, and we wait for a new card
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public String getAutomaticActionMessage() {
        return "An epidemic is spreading! You will lose 1 crew member for each populated adjacent cabin.";
    }
}
