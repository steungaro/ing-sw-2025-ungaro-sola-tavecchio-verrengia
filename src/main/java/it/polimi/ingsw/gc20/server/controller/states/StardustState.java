package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.AutomaticActionMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;


public class StardustState extends PlayingState {
    /**
     * Constructs a StardustState object, which represents a state in the game where players encounter stardust.
     * This constructor initializes the state with the provided game model, controller, and adventure card
     * and sets the phase to AUTOMATIC_ACTION. It also notifies players about the stardust encounter and performs
     * the automatic action associated with this state.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public StardustState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("You encountered a some stardust along your journey! You will lose some days and move backward based on the number of exposed connectors in your ship."));
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
        card.playCard();
    }

    /**
     * Executes the automatic action phase for the current state in the game.
     * This method moves players backward based on the number of exposed connectors in their ship,
     * updates player statuses, and advances the game to the draw card phase.
     * The sequence of operations is as follows:
     * - Iterates over the current in-game connected players in reverse order.
     * - Moves each player backward by a value determined by the number of exposed connectors of the player's ship.
     * - Sends updates to all players about the current state and position of each player.
     * - Notifies all in-game connected players that the draw card phase is starting.
     * - Changes the game phase to the draw card phase and plays the active card.
     * - Updates the game state to the pre-draw state.
     */
    @Override
    public void automaticAction() {
        getController().getInGameConnectedPlayers().reversed().stream()
                .map(p ->getController().getPlayerByID(p))
                .forEach(player -> getModel().movePlayer(player, -player.getShip().getAllExposed()));
        //draw a new card
        for (String player : getController().getInGameConnectedPlayers()) {
            getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player, 0, getController().getPlayerByID(player).isInGame(), getController().getPlayerByID(player).getColor(), (getController().getPlayerByID(player).getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
        }
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
